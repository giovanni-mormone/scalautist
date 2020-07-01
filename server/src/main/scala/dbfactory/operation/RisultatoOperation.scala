package dbfactory.operation

import java.sql.Date

import caseclass.CaseClassDB.{Risultato, Turno}
import caseclass.CaseClassHttpMessage.{InfoDates, InfoHome, InfoShift, ResultAlgorithm, ShiftDay}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona, InstanceRisultato, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{RisultatoTableQuery, TurnoTableQuery}
import dbfactory.util.Helper._
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

/**
 * @author Francesco Cassano
 *
 * Allows to perform operation on RisultatoSet table
 */
trait RisultatoOperation extends OperationCrud[Risultato]{

  /**
   * It returns data on an employee's daily work shifts
   *
   * @param idUser
   *               employee id
   * @param date
   *             day to return
   * @return
   *         Future of Option of [[InfoHome]] that contains information about shift
   */
  def getTurniInDate(idUser: Int, date: Date): Future[Option[InfoHome]]

  /**
   * Returns all data on an employee's work shifts in the next 7 days from selected date
   *
   * @param idUser
   *               employee id
   * @param date
   *             initial day for week count
   * @return
   *         Future of Option of [[InfoShift]] that contains information on shifts of the week
   */
  def getTurniSettimanali(idUser: Int, date: Date): Future[Option[InfoShift]]

  /**
   * Method which allow update absence of the driver with another driver that contains availability.
   * may be verified if driver that are received, really contains availability in this idRisultato
   *
   * @param idRisultato represent point where absence exist
   * @param idPersona person that replace the driver absenced
   * @return Future of Option with status code of operation
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if operation of update finish with success
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if idRisultato not exist
   *         [[messagecodes.StatusCodes.ERROR_CODE2]] if idPersona not exist
   */
  def updateAbsence(idRisultato:Int,idPersona:Int):Future[Option[Int]]

  /**
   * method that return information of the algorithm in a specific time
   * @param dateI init date from where collect information
   * @param dateF finish date, last day for collect information
   * @param idTerminal represent id of a terminal in database
   * @return Future of Option of List of ResultAlgorithm, for specific information to ResultAlgorithm view
   *         [[caseclass.CaseClassHttpMessage.ResultAlgorithm]]
   */
  def getResultAlgorithm(idTerminal:Int,dateI:Date,dateF:Date):Future[Option[List[ResultAlgorithm]]]

}

object RisultatoOperation extends RisultatoOperation {
  val DRIVER_CODE: Int = 3
  val ONLY_ONE_SHIFT = 1
  val WITHOUT_SHIFT = ""

  def verifyResult(idRisultato: Int): Future[Option[Int]] = {
    select(idRisultato).collect{
      case Some(_) => Option(StatusCodes.SUCCES_CODE)
      case None =>Option(StatusCodes.ERROR_CODE1)
    }
  }


  private case class Shift(day: Date, name: String)
  private case class InfoForResult(dateInit:Date,dateFinish:Date,result:List[Risultato]=Nil,personWithTerminal:List[(Int, Option[String])]=Nil,turno:List[(Int,String)]=Nil)
  private val future: Int => Future[Option[List[Int]]] = idUser => InstancePersona.operation().execQueryFilter(field => field.ruolo,
                                      filter => filter.id === idUser && filter.ruolo === DRIVER_CODE)

  override def getTurniInDate(idUser: Int, date: Date): Future[Option[InfoHome]] = {
    InstanceAssenza.operation().execQueryFilter(field => field.id, filter => filter.personaId === idUser &&
                                                filter.dataInizio <= date && filter.dataFine >= date).flatMap {
      case None =>
        future(idUser).flatMap{
          case Some(_) => for {
            listTurni <- InstanceRisultato.operation().execQueryFilter(field => field.turnoId,
              filter => filter.data === date && filter.personeId === idUser)
            turni <- InstanceTurno.operation().selectFilter(filter => filter.id.inSet(listTurni.getOrElse(List.empty[Int])))
            disponibilita <- DisponibilitaOperation.getDisponibilita(idUser, getWeekNumber(date))
          } yield Some(InfoHome(turni.getOrElse(List.empty[Turno]), disponibilita))
          case _ => Future.successful(None)
        }
      case _ => Future.successful(None)
    }
  }

  override def getTurniSettimanali(idUser: Int, date: Date): Future[Option[InfoShift]] = {
    val dateEnd = nextWeek(date)
    future(idUser).flatMap {
      case Some(_) =>
        InstanceAssenza.operation().execQueryFilter(field => (field.dataInizio, field.dataFine),
          filter => filter.personaId === idUser &&
            ((filter.dataInizio <= dateEnd && filter.dataInizio >= date) ||
              (filter.dataInizio <= date && filter.dataFine >= date))
        ).flatMap {

          case Some(dateL) => for {
              turni <- getTurnoOfDays(idUser, date, dateEnd)
              disponibilita <- DisponibilitaOperation.getDisponibilita(idUser, getWeekNumber(date))
            } yield Some(InfoShift(turni.toList.flatten.filter(
                turno => dateL.exists(date => date._1.compareTo(turno.day) > 0 || date._2.compareTo(turno.day) < 0)
            ).map(turno => ShiftDay(turno.day.toLocalDate.getDayOfWeek.getValue, turno.name)), disponibilita))
          case None => for {
              turni <- getTurnoOfDays(idUser, date, dateEnd)
              disponibilita <- DisponibilitaOperation.getDisponibilita(idUser, getWeekNumber(date))
            } yield Some(InfoShift(turni.toList.flatten.map(turno => ShiftDay(turno.day.toLocalDate.getDayOfWeek.getValue, turno.name)), disponibilita))
        }
      case _ => Future.successful(None)
    }
  }

  /**
   * Returns turno of the day in a week
   *
   * @param idUser
   *               employee
   * @param initDate
   *                 date of start of the period to search
   * @param endDate
   *                date of end of the period to search
   * @return
   */
  private def getTurnoOfDays(idUser: Int, initDate: Date, endDate: Date): Future[Option[List[Shift]]] = {
    val filter = for{
      turni <- RisultatoTableQuery.tableQuery() join TurnoTableQuery.tableQuery() on (_.turnoId ===_.id)
              if turni._1.data >= initDate && turni._1.data < endDate && turni._1.personeId === idUser
    } yield (turni._1.data, turni._2.fasciaOraria)

    InstanceRisultato.operation().execJoin(filter).collect{
      case Some(shifts) => Some(shifts.map(shift => Shift(shift._1, shift._2)))
      case _ => None
    }
  }

  override def updateAbsence(idRisultato:Int,idPersona:Int):Future[Option[Int]]=
    for{
      result <- InstanceRisultato.operation().execQueryFilter(risultato=>risultato.id,
        risultato=>risultato.id===idRisultato)
      persona <- InstancePersona.operation().execQueryFilter(persona=>persona.id,persona=>persona.id===idPersona)
      finalResult<- (result,persona) match{
        case (None, _) => Future.successful(Some(StatusCodes.ERROR_CODE1))
        case (_, None) => Future.successful(Some(StatusCodes.ERROR_CODE2))
        case (_, _) => update(idRisultato,idPersona)
      }
    }yield finalResult

  private def update(idRisultato:Int,idPersona:Int): Future[Option[Int]] ={
    InstanceRisultato.operation().execQueryUpdate(risultato=>risultato.personeId,risultato=>risultato.id===idRisultato,
      idPersona).result()
  }

  override def getResultAlgorithm(idTerminal:Int,dateI: Date, dateF: Date): Future[Option[List[ResultAlgorithm]]] = {
    InstancePersona.operation().execQueryFilter(persona=>persona.id, filter=>Option(idTerminal)==filter.terminaleId)
      .flatMap {
        case Some(result) =>getResultByPerson(result,dateI,dateF)
        case None =>Future.successful(None)
      }
  }

  private def getResultByPerson(idPerson:List[Int],dateI: Date, dateF: Date)={
    InstanceRisultato.operation()
      .selectFilter(risultato=>risultato.data>=dateI && risultato.data<=dateF && risultato.personeId.inSet(idPerson))
      .flatMap {
        case Some(result) =>getTerminalAndShiftForDriver(InfoForResult(dateI,dateF,result))
        case None =>Future.successful(None)
      }
  }
  private def getTerminalAndShiftForDriver(infoResult:InfoForResult): Future[Option[List[ResultAlgorithm]]] ={
    InstancePersona.operation()
      .execQueryFilterDistinct(persona=>(persona.id,persona.terminaleId),persona=>persona.id.inSet(infoResult.result.map(_.personaId)))
      .flatMap {
        case Some(value) => getIdTerminalAndName(value,infoResult)
        case None =>Future.successful(None)
      }
  }

  private def getIdTerminalAndName(idPersonTerminal:List[(Int,Option[Int])],infoResult:InfoForResult): Future[Option[List[ResultAlgorithm]]] ={
    InstanceTerminale.operation()
      .execQueryFilter(terminal=>(terminal.id,terminal.nomeTerminale),filter=>filter.id.inSet(idPersonTerminal.flatMap(_._2.toList)))
      .flatMap {
        case Some(terminal) => getIdShiftAndName(infoResult.copy(personWithTerminal =createPersonAndTerminal(idPersonTerminal,terminal)))
        case None =>Future.successful(None)
      }
  }

  private def createPersonAndTerminal(idPersonTerminal:List[(Int,Option[Int])],terminal:List[(Int,String)]): List[(Int, Option[String])] ={
    idPersonTerminal.map(personAndTerminal=>
      personAndTerminal._1->terminal.filter(idTerminal=>personAndTerminal._2.contains(idTerminal._1)).map(_._2).headOption)
  }

  private def getIdShiftAndName(infoResult:InfoForResult):Future[Option[List[ResultAlgorithm]]]={
    InstanceTurno.operation()
      .execQueryFilter(turno=>(turno.id,turno.nomeTurno),filter=>filter.id.inSet(infoResult.result.map(_.turnoId).distinct))
      .collect {
          case Some(turno) => createResultAlgorithm(infoResult.copy(turno=turno))
          case None =>None
    }
  }
  //TODO controlar que pasa si una lista de infoday esta vacia
  private def createResultAlgorithm(infoResult:InfoForResult):Option[List[ResultAlgorithm]]=
      Option(infoResult.personWithTerminal.map(personaWithTerminal=>
        ResultAlgorithm(personaWithTerminal._1,personaWithTerminal._2.toString,
          createInfoDates(infoResult.result.filter(_.personaId==personaWithTerminal._1),infoResult))))

  private def createInfoDates(result:List[Risultato],infoResult:InfoForResult):List[InfoDates]=
    result.map(_.turnoId).distinct.length match{
    case ONLY_ONE_SHIFT =>
      result.headOption.map(info=>InfoDates(infoResult.dateInit,infoResult.dateFinish,selectNameTurno(info.turnoId,infoResult))).toList
    case _ =>
      val initShift = result match {
        case ::(head, _) => head.turnoId
        case Nil => 0
      }
      constructInfoDates(result,infoResult,initShift)
    }

  private def constructInfoDates(result:List[Risultato],infoResult:InfoForResult,initShift:Int):List[InfoDates]=
    result match {
      case _ :: _  => @scala.annotation.tailrec
      def _constructInfoDates(result:List[Risultato], initDate:Date,finishDate:Date, initShift:Int, listDates:List[InfoDates]=Nil):List[InfoDates]= result match{
        case head :: next if head.turnoId==initShift => _constructInfoDates(next,initDate,head.data,initShift,listDates:::List.empty)
        case head :: next => _constructInfoDates(next,head.data,head.data,head.turnoId,InfoDates(infoResult.dateInit,finishDate,selectNameTurno(initShift,infoResult))::listDates)
        case Nil =>listDates
      }
        _constructInfoDates(result,infoResult.dateInit,infoResult.dateInit,initShift)
      case Nil =>Nil
    }

  private def selectNameTurno(idTurno:Int,infoResult:InfoForResult):String=
    infoResult.turno.filter(id => id._1 == idTurno) match {
      case List(turno) =>turno._2
      case Nil =>WITHOUT_SHIFT
    }

}