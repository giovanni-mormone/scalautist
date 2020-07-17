package dbfactory.operation

import java.sql.Date

import algoritmo.AssignmentOperation.{Info, InfoDay}
import caseclass.CaseClassDB.{Risultato, Turno}
import caseclass.CaseClassHttpMessage._
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona, InstanceRisultato, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{PersonaTableQuery, RisultatoTableQuery, StoricoContrattoTableQuery, TurnoTableQuery}
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
  def verifyAndSaveResultAlgorithm(result: List[Info],dataI:Date,dataF:Date): Future[Option[Int]]
}

object RisultatoOperation extends RisultatoOperation {
  private val DRIVER_CODE: Int = 3
  private val WITHOUT_SHIFT: String = ""
  private val DEFAULT_INIT: Long =0
  private val ABSENCE_VALUE: String ="Assenza"
  private val FREE_DAY_VALUE: String ="Libero"
  private val DEFAULT_TERMINAL: String =""
  private val WITHOUT_CONTRACT: String = "Senza Contratto"
  private val WITHOUT_CONTRACT_VALUE=1
  private val DATE_IN_ABSENCE=2
  private val FREE_DAY = 3
  private val NORMAL_CASE = 4

  def verifyResult(idRisultato: Int): Future[Option[Int]] = {
    select(idRisultato).collect{
      case Some(_) => Option(StatusCodes.SUCCES_CODE)
      case None =>Option(StatusCodes.ERROR_CODE1)
    }
  }


  private final case class Shift(day: Date, name: String)
  private final case class InfoForResult(idPerson:List[(Int,Option[Int],Date,Option[Date])],dateInit:Date,dateFinish:Date,result:List[Risultato]=Nil,personWithTerminal:List[(Int, Option[String])]=Nil,turno:List[(Int,String)]=Nil)
  private final case class InfoPerson(idPersona:Int,terminal:String,initContract:Date,finishContract:Date)
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

  private def update(idRisultato:Int,idPersona:Int): Future[Option[Int]] =
    InstanceRisultato.operation().execQueryUpdate(risultato=>risultato.personeId,risultato=>risultato.id===idRisultato,
      idPersona).result()

  override def getResultAlgorithm(idTerminal:Int,dateI: Date, dateF: Date): Future[Option[List[ResultAlgorithm]]] = {
    val joinPersonContract = for{
      person<- PersonaTableQuery.tableQuery()
      contract <- StoricoContrattoTableQuery.tableQuery()
      if(person.id===contract.personaId && person.terminaleId===Option(idTerminal)
        && ((contract.dataInizio<=dateI && contract.dataFine>=dateF)
        || (contract.dataInizio>=dateI && contract.dataFine<=dateF)
        || (contract.dataFine>=dateI && contract.dataFine<=dateF)
        || (contract.dataInizio>=dateI && contract.dataInizio<=dateF)
        || (contract.dataInizio<=dateF && Some(contract.dataFine).isEmpty)))
    }yield(person.id,person.terminaleId,contract.dataInizio,contract.dataFine)

    InstancePersona.operation().execJoin(joinPersonContract)
      .flatMap {
        case Some(result) =>getResultByPerson(result,dateI,dateF)
        case None =>Future.successful(None)
      }
  }

  private def getResultByPerson(idPerson:List[(Int,Option[Int],Date,Option[Date])],dateI: Date, dateF: Date): Future[Option[List[ResultAlgorithm]]] =
    InstanceRisultato.operation()
      .selectFilter(risultato=>risultato.data>=dateI && risultato.data<=dateF && risultato.personeId.inSet(idPerson.map(_._1)))
      .flatMap {
        case Some(result) =>getIdTerminalAndName(idPerson.map(value=>(value._1,value._2)),InfoForResult(idPerson,dateI,dateF,result))
        case None =>Future.successful(None)
      }

  private def getIdTerminalAndName(idPersonTerminal:List[(Int,Option[Int])],infoResult:InfoForResult): Future[Option[List[ResultAlgorithm]]] =
    InstanceTerminale.operation()
      .execQueryFilter(terminal=>(terminal.id,terminal.nomeTerminale),filter=>filter.id.inSet(idPersonTerminal.flatMap(_._2.toList)))
      .flatMap(terminal => getIdShiftAndName(infoResult.copy(personWithTerminal = createPersonAndTerminal(idPersonTerminal, terminal.toList.flatten))))

  private def createPersonAndTerminal(idPersonTerminal:List[(Int,Option[Int])],terminal:List[(Int,String)]): List[(Int, Option[String])] =
    idPersonTerminal.map(personAndTerminal=>
      personAndTerminal._1->terminal.filter(idTerminal=>personAndTerminal._2.contains(idTerminal._1)).map(_._2).headOption)

  private def getIdShiftAndName(infoResult:InfoForResult):Future[Option[List[ResultAlgorithm]]]=
    InstanceTurno.operation()
      .execQueryFilter(turno=>(turno.id,turno.nomeTurno),filter=>filter.id.inSet(infoResult.result.map(_.turnoId).distinct))

      .flatMap(turno=> createResultAlgorithm(infoResult.copy(turno=turno.toList.flatten)))
  private def extractDate(date:Option[Date],defaultDate:Date): Date = date match {
    case Some(value) => value
    case None =>defaultDate
  }

  private def createResultAlgorithm(infoResult:InfoForResult):Future[Option[List[ResultAlgorithm]]]={
    @scala.annotation.tailrec
    def _createResultAlgorithm(idPerson:List[InfoPerson], finalResult:Future[Option[List[ResultAlgorithm]]]=Future.successful(None)):Future[Option[List[ResultAlgorithm]]] = idPerson match {
      case ::(head, next) => _createResultAlgorithm(next,zipFuture(head,infoResult,finalResult))
      case Nil =>finalResult
    }
    val infoPerson= infoResult.idPerson.map(value=>InfoPerson(value._1,infoResult.personWithTerminal.filter(person => person._1 == value._1)
      .flatMap(_._2.toList) match {
      case List(element) => element
      case Nil =>DEFAULT_TERMINAL
    },value._3,extractDate(value._4,infoResult.dateFinish)))
    _createResultAlgorithm(infoPerson)
  }

  private def zipFuture(head:InfoPerson,infoResult:InfoForResult,result:Future[Option[List[ResultAlgorithm]]]): Future[Option[List[ResultAlgorithm]]] =
    result.zip(createInfoDates(infoResult.result.filter(_.personaId==head.idPersona),infoResult,head.idPersona)
      .collect(value=>ResultAlgorithm(head.idPersona,head.terminal,value.sortBy(value=>(value.date,value.turno,value.turno2))))).map{
      case (option, algorithm) => Option(option.toList.flatten:+algorithm)
    }


  private val differenceBetweenDate:Long=>Long=>Long=finishDate=>initDate=>finishDate-initDate

  private def createInfoDates(result:List[Risultato],infoResult:InfoForResult,idPerson:Int):Future[List[InfoDates]]=
    InstanceAssenza.operation().execQueryFilter(assenza=>(assenza.dataInizio,assenza.dataFine),
      filter=>((filter.dataInizio<=infoResult.dateInit && filter.dataFine>=infoResult.dateFinish)
        ||(filter.dataInizio>=infoResult.dateInit && filter.dataFine<=infoResult.dateFinish)
        ||(filter.dataFine>=infoResult.dateInit && filter.dataFine<=infoResult.dateFinish)
        ||(filter.dataInizio>=infoResult.dateInit && filter.dataInizio<=infoResult.dateFinish)) && filter.personaId===idPerson).collect {
      case Some(value) =>
        insertInfoShiftInInfoDates(result,infoResult,insertAllAbsence(value,infoResult))
     case None => insertInfoShiftInInfoDates(result,infoResult)
    }

  private def insertInfoShiftInInfoDates(result:List[Risultato],infoResult:InfoForResult,infoAbsence:List[InfoDates]=List.empty):List[InfoDates]={
    @scala.annotation.tailrec
    def _insertInfoShiftInInfoDates(infoResult:InfoForResult, infoAbsence:List[InfoDates], date:Option[Date]):List[InfoDates]= infoResult.result match {
      case first::second::third::next if first.data==second.data && second.data==third.data =>
        _insertInfoShiftInInfoDates(infoResult.copy(result = next),verifyResultThreeShift(infoResult,infoAbsence,date,verifyDate(infoResult,infoAbsence,date)),date.map(subtract(_,1)))
      case first::second::next if first.data==second.data =>
        _insertInfoShiftInInfoDates(infoResult.copy(result = next),verifyResultTwoShift(infoResult,infoAbsence,date,verifyDate(infoResult,infoAbsence,date)),date.map(subtract(_,1)))
      case _::next  => _insertInfoShiftInInfoDates(infoResult.copy(result = next),verifyResultOneShift(infoResult,infoAbsence,date,verifyDate(infoResult,infoAbsence,date)),date.map(subtract(_,1)))
      case Nil =>infoAbsence
    }
    _insertInfoShiftInInfoDates(infoResult.copy(result=result.sortBy(value=>(value.data,value.personaId,value.turnoId))),infoAbsence, result.headOption.map(_.data))
  }

  def obtainValueDate(date:Option[Date]): Date =date match {
    case Some(value) => value
    case None => Date.valueOf("")
  }

  private val infoDateWithoutContractThreeShift: Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date) , WITHOUT_CONTRACT,Some(WITHOUT_CONTRACT),Some(WITHOUT_CONTRACT))
  private val infoDateWithFreeDayThreeShift:Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date), FREE_DAY_VALUE,Some(FREE_DAY_VALUE),Some(FREE_DAY_VALUE))
  private val infoDateWithThreeShift: Option[Date]=>(Int,Int,Int)=>InfoForResult=>InfoDates=date=>(shift,shift2,shift3)=>infoResult=>InfoDates(obtainValueDate(date),
    selectNameTurno(infoResult)(shift),Some(selectNameTurno(infoResult)(shift2)),Some(selectNameTurno(infoResult)(shift3)))

  private val infoDateWithoutContractTwoShift: Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date),WITHOUT_CONTRACT,Some(WITHOUT_CONTRACT))
  private val infoDateWithFreeDayTwoShift: Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date), FREE_DAY_VALUE,Some(FREE_DAY_VALUE))
  private val infoDateWithTwoShift: Option[Date]=>(Int,Int)=>InfoForResult=>InfoDates=date=>(shift,shift2)=>infoResult=>InfoDates(obtainValueDate(date),
    selectNameTurno(infoResult)(shift),Some(selectNameTurno(infoResult)(shift2)))

  private val infoDateWithoutContractOneShift: Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date), WITHOUT_CONTRACT)
  private val infoDateWithFreeDayOneShift: Option[Date]=>InfoDates=date=>InfoDates(obtainValueDate(date), FREE_DAY_VALUE)
  private val infoDateWithOneShift: Option[Date]=>Int=>InfoForResult=>InfoDates=date=>shift=>infoResult=>InfoDates(obtainValueDate(date),
    selectNameTurno(infoResult)(shift))

  def verifyResultThreeShift(infoResult:InfoForResult, infoAbsence:List[InfoDates],date:Option[Date],info:Int):List[InfoDates]= (infoResult.result,info) match {
    case (_,WITHOUT_CONTRACT_VALUE) => infoAbsence:+infoDateWithoutContractThreeShift(date)
    case (_,DATE_IN_ABSENCE)=> infoAbsence
    case (_,FREE_DAY) => infoAbsence:+infoDateWithFreeDayThreeShift(date)
    case (first::second::third::_,NORMAL_CASE) =>infoAbsence:+
      infoDateWithThreeShift(date)(first.turnoId,second.turnoId,third.turnoId)(infoResult)
  }

  def verifyResultTwoShift(infoResult:InfoForResult, infoAbsence:List[InfoDates],date:Option[Date],info:Int):List[InfoDates]= (infoResult.result,info) match {
    case (_,WITHOUT_CONTRACT_VALUE) =>
     infoAbsence:+infoDateWithoutContractTwoShift(date)
    case (_,DATE_IN_ABSENCE)=> infoAbsence
    case (_,FREE_DAY) => infoAbsence:+infoDateWithFreeDayTwoShift(date)
    case (first::second::_,NORMAL_CASE) => infoAbsence:+
      infoDateWithTwoShift(date)(first.turnoId,second.turnoId)(infoResult)

  }

  def verifyResultOneShift(infoResult:InfoForResult, infoAbsence:List[InfoDates],date:Option[Date],info:Int):List[InfoDates]= (infoResult.result,info) match {
    case (_,WITHOUT_CONTRACT_VALUE) => infoAbsence:+infoDateWithoutContractOneShift(date)
    case (_,DATE_IN_ABSENCE)=>infoAbsence
    case (_,FREE_DAY) =>infoAbsence:+infoDateWithFreeDayOneShift(date)
    case (first::_,NORMAL_CASE) =>infoAbsence:+
      infoDateWithOneShift(date)(first.turnoId)(infoResult)

  }

  def verifyDate(infoResult:InfoForResult, infoAbsence:List[InfoDates],date:Option[Date]): Int = (infoResult.result,date) match {
    case (first::_,Some(date)) if infoResult.idPerson.filter(_._1==first.personaId).exists(dat=>date.compareTo(dat._3)<0
        || dat._4.exists(internalDate=> date.compareTo(internalDate)>0)) => WITHOUT_CONTRACT_VALUE
    case (first::_,_) if infoAbsence.exists(day=>day.date==first.data)=>DATE_IN_ABSENCE
    case (first::_,Some(date)) if computeDaysBetweenDates(date,first.data)>1
      && !infoAbsence.exists(day=>day.date==first.data)  =>  FREE_DAY
    case  _ =>  NORMAL_CASE
  }


  private def insertAllAbsence(listAbsence:List[(Date,Date)],infoResult:InfoForResult): List[InfoDates] =
    listAbsence.map(value => if(value._1.compareTo(infoResult.dateInit)<0)(infoResult.dateInit,value._2) else value)
        .map(value =>if(value._2.compareTo(infoResult.dateFinish)>0)(value._1,infoResult.dateFinish) else value)
        .map(date=>date._1->differenceBetweenDate(date._2.toLocalDate.toEpochDay)(date._1.toLocalDate.toEpochDay))
        .flatMap(date_diff=>(DEFAULT_INIT to date_diff._2).map(value=>date_diff._1.toLocalDate.plusDays(value))
        .map(day=>InfoDates(Date.valueOf(day),ABSENCE_VALUE,Some(ABSENCE_VALUE))))

  private val selectNameTurno:InfoForResult=>Int=>String=infoResult=>idTurno=>
    infoResult.turno.filter(id => id == idTurno) match {
      case List(turno) =>turno._2
      case Nil =>WITHOUT_SHIFT
    }
 
  override def verifyAndSaveResultAlgorithm(result: List[Info],dataI:Date,dataF:Date): Future[Option[Int]] = {
    val finalResult = result.map(inf=>inf.copy(infoDay= inf.infoDay.filter(x=> !x.absence && !x.freeDay))).flatMap(x=>{
      x.infoDay.flatMap{
        case InfoDay(data, Some(shift), None, None,_,_)=>x.idDriver.toList.map(id=>Risultato(data,id,shift))
        case InfoDay(data, Some(shift), Some(shift2), None,_,_)=>x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,shift2)))
        case InfoDay(data, Some(shift), Some(shift2), Some(straordinario),_,_)=>
          x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,shift2),Risultato(data,id,straordinario)))
        case InfoDay(data, Some(shift),None, Some(straordinario),_,_) =>
          x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,straordinario)))
      }
    })
    InstanceRisultato.operation().selectFilter(res=>res.data>=dataI).flatMap {
      case Some(value) =>resultForUpdate(value,finalResult)
      case None =>insertAll(finalResult)
    }
  }

  private def resultForUpdate(oldR: List[Risultato],newR: List[Risultato]): Future[Option[Int]] ={
    insertAll(newR).flatMap {
      case Some(StatusCodes.SUCCES_CODE) => deleteAll(oldR.flatMap(_.idRisultato.toList))
      case Some(StatusCodes.ERROR_CODE1) => Future.successful(Option(StatusCodes.ERROR_CODE1))
    }
  }

  private def insertAll(element: List[Risultato]): Future[Option[Int]] = {
    super.insertAll(element).collect {
      case Some(value) =>Some(StatusCodes.SUCCES_CODE)
      case None =>Some(StatusCodes.ERROR_CODE1)
    }
  }
}
