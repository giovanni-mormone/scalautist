package algoritmo

import java.sql.Date

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, SettimanaN, SettimanaS}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstancePersona, InstanceRegola, InstanceRichiestaTeorica, InstanceTerminale}
import dbfactory.operation.TurnoOperation
import dbfactory.setting.Table.{PersonaTableQuery, StoricoContrattoTableQuery}
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
trait Algoritmo {

  /**
   * When this method is called, we call database for extract all information that the algorithm needs.
   * First of all we verify that information it's send, are be correct, verify that terminals that send existing in database,
   * that the group in list contains all two dates, that the normal week and special week contains a ruler that exist in
   * database and that the date that contains special week are be within time frame that run algorithm
   * @param algorithmExecute case class that contains all information for algorithm, this information is: init date,
   *                         finish date, id of terminals, list of group (Optional), normal week (Optional),
   *                         special week (Optional) and Three saturday ruler
   * @return Future of Option of Int that specified status of operation, this code can be :
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if all condition are satisfied
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if time frame have a problem, this can be:
   *                                                    time frame less that 28 days, dates to the contrary
   *         [[messagecodes.StatusCodes.ERROR_CODE2]] if list with terminal contains some terminal that not exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE3]] if group contains some error, this can be:
   *                                                  group with one date, date in group outside time frame, ruler in
   *                                                  group not exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE4]] if normal week contains some error, this can be:
   *                                                  idDay not correspond to day in week, ruler in week not exist in
   *                                                  database, shift in week not exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE5]] if special week contains some error, this can be:
   *                                                  idDay not correspond to day in week, ruler in week not exist in
   *                                                  database, shift in week not exist in database or date in week
   *                                                  is outside to time frame
   *         [[messagecodes.StatusCodes.ERROR_CODE6]] if time frame not contains theorical request
   *         [[messagecodes.StatusCodes.ERROR_CODE7]] if some terminal not contains drivers
   *         [[messagecodes.StatusCodes.ERROR_CODE8]] if not exist shift in database
   *
   */
  def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute):Future[Option[Int]]
}
object Algoritmo extends Algoritmo{

  private val MINIMUM_DAYS = 28
  private val future:Int=>Future[Option[Int]]=code=>Future.successful(Some(code))
  private val getRuler:List[Int]=>Future[Option[List[Regola]]]=idRegola=>InstanceRegola.operation().selectFilter(_.id.inSet(idRegola))
  private val getShift:Future[Option[List[Turno]]]=TurnoOperation.selectAll
  private val ENDED_DAY_OF_WEEK = 7
  private val FIRST_DAY_OF_WEEK = 1
  private val MINIMUM_DATE_FOR_GROUP=2
  private val verifyDateInt:Int=>Boolean=idDay => idDay<=ENDED_DAY_OF_WEEK && idDay>=FIRST_DAY_OF_WEEK
  private val verifyDate:(Date,Date,Date)=>Boolean=(day,initDay,finishDay) => {
    day.compareTo(initDay)>=0 && day.compareTo(finishDay)<=0 && getDayNumber(day)!=ENDED_DAY_OF_WEEK
  }
  final case class InfoForAlgorithm(shift: List[Turno],theoricalRequest:List[RichiestaTeorica],persons:List[(Int,Persona)],absence:Option[List[(Int,Date,Date)]]=None,allRequest:Option[List[InfoReq]]=None)
  final case class InfoReq(idShift:Int,request:Int,assigned:Int,idDay:Int,data:Date,idTerminal:Int)

  override def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    verifyData(algorithmExecute).collect{
      case Some(StatusCodes.SUCCES_CODE) =>Some(StatusCodes.SUCCES_CODE)
      case value =>value
    }
  }

  private def verifyData(algorithmExecute: AlgorithmExecute): Future[Option[Int]] ={
    computeDaysBetweenDates(algorithmExecute.dateI,algorithmExecute.dateF)match {
      case value if value>=MINIMUM_DAYS =>getShift.flatMap {
        case Some(shift) => verifyTerminal(algorithmExecute,shift)
        case None =>future(StatusCodes.ERROR_CODE8)
      }
      case _ =>future(StatusCodes.ERROR_CODE1)
    }
  }

  private def verifyTerminal(algorithmExecute: AlgorithmExecute,shift:List[Turno]): Future[Option[Int]] = {
    algorithmExecute.idTerminal match {
      case Nil =>future(StatusCodes.ERROR_CODE2)
      case _ => InstanceTerminale.operation().selectFilter(_.id.inSet(algorithmExecute.idTerminal)).flatMap {
        case Some(value) if value.length==algorithmExecute.idTerminal.length=>verifyGroup(algorithmExecute,shift)
        case _ =>future(StatusCodes.ERROR_CODE2)
      }
    }
  }

  private def verifyGroup(algorithmExecute: AlgorithmExecute,shift:List[Turno]): Future[Option[Int]] = {
    @scala.annotation.tailrec
    def _verifyGroup(groups:List[GruppoA]):Future[Option[Int]] = groups match {
      case ::(head, next) if head.date.length>=MINIMUM_DATE_FOR_GROUP
        && head.date.forall(date=>verifyDate(date,algorithmExecute.dateI,algorithmExecute.dateF))=>_verifyGroup(next)
      case Nil =>verifyNormalWeek(algorithmExecute,shift)
      case _ =>future(StatusCodes.ERROR_CODE3)
    }
    algorithmExecute.gruppo match {
      case Some(groups) => getRuler(groups.map(_.regola)).flatMap {
        case Some(value) if value.length==groups.map(_.regola).distinct.length=>   _verifyGroup(groups)
        case _ =>future(StatusCodes.ERROR_CODE3)
      }
      case None =>verifyNormalWeek(algorithmExecute,shift)
    }
  }
  private val verifyRuler:List[Regola]=>List[Int]=>List[Int]=>List[Turno]=>Boolean=ruler=>weekWithRuler=>idShift=>shift=>{
    ruler.length==weekWithRuler.length && idShift.forall(turno=>shift.exists(idTurno=>idTurno.id.contains(turno)))
  }
  private def verifyNormalWeek(algorithmExecute: AlgorithmExecute,shift:List[Turno]): Future[Option[Int]] = {
    @scala.annotation.tailrec
    def _verifyNormalWeek(normalWeek:List[SettimanaN]):Future[Option[Int]] = normalWeek match {
      case ::(head, next) if verifyDateInt(head.idDay) =>_verifyNormalWeek(next)
      case Nil =>verifySpecialWeek(algorithmExecute,shift)
      case _ =>future(StatusCodes.ERROR_CODE4)
    }
    algorithmExecute.settimanaNormale match {
      case Some(normalWeek) => getRuler(normalWeek.map(_.regola)).flatMap {
        case Some(value) if verifyRuler(value)(normalWeek.map(_.regola))(normalWeek.map(_.turnoId))(shift)=>
                _verifyNormalWeek(normalWeek)
        case _ =>  future(StatusCodes.ERROR_CODE4)
      }
      case None =>verifySpecialWeek(algorithmExecute,shift)
    }
  }

  private def verifySpecialWeek(algorithmExecute: AlgorithmExecute,shift: List[Turno]): Future[Option[Int]] = {
    @scala.annotation.tailrec
    def _verifySpecialWeek(specialWeek:List[SettimanaS]):Future[Option[Int]] = specialWeek match {
      case ::(head, next) if verifyDateInt(head.idDay)
        && verifyDate(head.date,algorithmExecute.dateI,algorithmExecute.dateF) =>_verifySpecialWeek(next)
      case Nil =>verifyTheoricalRequest(algorithmExecute,shift)
      case _ =>future(StatusCodes.ERROR_CODE5)
    }
    algorithmExecute.settimanaSpeciale match {
      case Some(specialWeek) => getRuler(specialWeek.map(_.regola)).flatMap {
        case Some(value) if verifyRuler(value)(specialWeek.map(_.regola))(specialWeek.map(_.turnoId))(shift)=>
          _verifySpecialWeek(specialWeek)
        case _ =>  future(StatusCodes.ERROR_CODE5)
      }
      case None =>verifyTheoricalRequest(algorithmExecute,shift)
    }
  }

  private def verifyTheoricalRequest(algorithmExecute: AlgorithmExecute,shift: List[Turno]): Future[Option[Int]] = {
    InstanceRichiestaTeorica.operation().selectFilter(richiesta=>richiesta.dataInizio<=algorithmExecute.dateI
      && richiesta.dataFine>=algorithmExecute.dateF && richiesta.terminaleId.inSet(algorithmExecute.idTerminal)).flatMap {
        case Some(theoricalRequest) if theoricalRequest.length==algorithmExecute.idTerminal.length=>
          getPersonByTerminal(algorithmExecute,shift,theoricalRequest)
        case _ =>future(StatusCodes.ERROR_CODE6)
      }
  }

  private def getPersonByTerminal(algorithmExecute: AlgorithmExecute,shift: List[Turno],theoricalRequest:List[RichiestaTeorica]):Future[Option[Int]] ={
    val joinPersona = for{
      persona<-PersonaTableQuery.tableQuery()
      contratto<-StoricoContrattoTableQuery.tableQuery()
      if persona.id===contratto.personaId && algorithmExecute.idTerminal.contains(persona.terminaleId)
    }yield (contratto.contrattoId,persona)
    InstancePersona.operation().execJoin(joinPersona).collect {
      case Some(person) =>
        val infoForAlgorithm=ExtractAlgorithmInformation().getAllData(algorithmExecute,InfoForAlgorithm(shift,theoricalRequest,person))
        AssignmentOperation().initOperationAssignment(algorithmExecute,infoForAlgorithm)
        Some(StatusCodes.SUCCES_CODE)
      case None =>Some(StatusCodes.ERROR_CODE7)
    }
  }




}
