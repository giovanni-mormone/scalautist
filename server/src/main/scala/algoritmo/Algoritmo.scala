package algoritmo

import java.sql.Date

import caseclass.CaseClassDB.{Regola, Turno}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, SettimanaN, SettimanaS}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona, InstanceRegola, InstanceRichiestaTeorica, InstanceTerminale, InstanceTurno}
import dbfactory.operation.TurnoOperation
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
   */
  def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute):Future[Option[Int]]
}
object Algoritmo extends Algoritmo{
  //TODO DETO DA GIANNI! assegnare prima i liberi ai 5x2 sabato e domenica libero
  //TODO assegnare domeniche 6x1 indipendente sia fisso o rotatorio
  //TODO se ci sono assegniamo i gruppi per ogni gruppo assegna tutti un libero controllare in che data serve di piu
  //TODO che lavorare la persona prima di assegnare il libero del gruppo
  //TODO assegnare tutti turni prima fissi dopo rotatorio, verificare InfoReq quando si sta assegnando
  //TODO verificare se regola tre sabato e attiva, se e cosi e il conducente ha 3 sabati di seguito lavorando,
  //TODO il 3 e libero, se il 3 e insieme a una domenica libera allora lo avrà il 2 e cosi via via
  //TODO assegnare i liberi ai 6x1 fissi, controllare quantita di giorni senza libero. in piu guardare se ce una settimana normale o speciale
  //TODO due liberi non possono essere insieme, due liberi devono avere una distanza minima di 2 giorni
  //TODO assegnare liberi ai 6x1 rotatorio, controllare InfoReq per vedere quanti possono essere liberi in quel turno in quella data


  private val MINIMUM_DAYS = 28
  private val future:Int=>Future[Option[Int]]=code=>Future.successful(Some(code))
  val getRuler:List[Int]=>Future[Option[List[Regola]]]=idRegola=>InstanceRegola.operation().selectFilter(_.id.inSet(idRegola))
  val getShift:List[Int]=>Future[Option[List[Turno]]]=idShift=>InstanceTurno.operation().selectFilter(_.id.inSet(idShift))
  private val ENDED_DAY_OF_WEEK = 7
  private val FIRST_DAY_OF_WEEK = 1
  private val MINIMUM_DATE_FOR_GROUP=2
  private val verifyDateInt:Int=>Boolean=idDay => idDay<=ENDED_DAY_OF_WEEK && idDay>=FIRST_DAY_OF_WEEK
  private val verifyDate:(Date,Date,Date)=>Boolean=(day,initDay,finishDay) => {
    day.compareTo(initDay)>=0 && day.compareTo(finishDay)<=0 && getDayNumber(day)!=ENDED_DAY_OF_WEEK
  }

  override def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    verifyData(algorithmExecute).collect{
      case Some(StatusCodes.SUCCES_CODE) =>
        getAllData(algorithmExecute)
        Some(StatusCodes.SUCCES_CODE)
      case value =>value
    }
  }
  private def assignSaturdayAndSunday5x2()={

  }
  private def assignSunday6x1()={

  }
  private def assignGroup()={

  }
  private def assignShiftFixed()={

  }
  private def assignShiftRotary()={

  }
  private def rulerThirfSaturday()={

  }
  private def assignFreeDayFixed6x1()={

  }
  private def assignFreeDayRotary6x1()={

  }

  private def verifyData(algorithmExecute: AlgorithmExecute): Future[Option[Int]] ={
    computeDaysBetweenDates(algorithmExecute.dateI,algorithmExecute.dateF)match {
      case value if value>=MINIMUM_DAYS => verifyTerminal(algorithmExecute)
      case _ =>Future.successful(Some(StatusCodes.ERROR_CODE1))
    }
  }

  private def verifyTerminal(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    algorithmExecute.idTerminal match {
      case Nil =>future(StatusCodes.ERROR_CODE2)
      case _ => InstanceTerminale.operation().selectFilter(_.id.inSet(algorithmExecute.idTerminal)).flatMap {
        case Some(value) if value.length==algorithmExecute.idTerminal.length=>verifyGroup(algorithmExecute)
        case _ =>future(StatusCodes.ERROR_CODE2)
      }
    }
  }

  private def verifyGroup(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    @scala.annotation.tailrec //colocar que las datas tienen que estar dentro el periodo di run
    def _verifyGroup(groups:List[GruppoA]):Future[Option[Int]] = groups match {
      case ::(head, next) if head.date.length>=MINIMUM_DATE_FOR_GROUP
        && head.date.forall(date=>verifyDate(date,algorithmExecute.dateI,algorithmExecute.dateF))=>_verifyGroup(next)
      case Nil =>verifyNormalWeek(algorithmExecute)
      case _ =>future(StatusCodes.ERROR_CODE3)
    }
    algorithmExecute.gruppo match {
      case Some(groups) => getRuler(groups.map(_.regola)).flatMap {
        case Some(value) if value.length==groups.map(_.regola).distinct.length=>   _verifyGroup(groups)
        case _ =>future(StatusCodes.ERROR_CODE3)
      }
      case None =>verifyNormalWeek(algorithmExecute)
    }
  }


  private def verifyNormalWeek(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    @scala.annotation.tailrec
    def _verifyNormalWeek(normalWeek:List[SettimanaN]):Future[Option[Int]] = normalWeek match {
      case ::(head, next) if verifyDateInt(head.idDay) =>_verifyNormalWeek(next)
      case Nil =>verifySpecialWeek(algorithmExecute)
      case _ =>future(StatusCodes.ERROR_CODE4)
    }
    algorithmExecute.settimanaNormale match {
      case Some(normalWeek) => getRuler(normalWeek.map(_.regola)).flatMap {
        case Some(value) if value.length==normalWeek.map(_.regola).distinct.length=>getShift(normalWeek.map(_.turnoId).distinct)
            .flatMap {
              case Some(value) if value.length==normalWeek.map(_.turnoId).distinct.length=>_verifyNormalWeek(normalWeek)
              case _ => future(StatusCodes.ERROR_CODE4)
            }
        case _ =>  future(StatusCodes.ERROR_CODE4)
      }
      case None =>verifySpecialWeek(algorithmExecute)
    }
  }

  private def verifySpecialWeek(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    @scala.annotation.tailrec
    def _verifySpecialWeek(specialWeek:List[SettimanaS]):Future[Option[Int]] = specialWeek match {
      case ::(head, next) if verifyDateInt(head.idDay)
        && verifyDate(head.date,algorithmExecute.dateI,algorithmExecute.dateF) =>_verifySpecialWeek(next)
      case Nil =>verifyTheoricalRequest(algorithmExecute)
      case _ =>future(StatusCodes.ERROR_CODE5)
    }
    algorithmExecute.settimanaSpeciale match {
      case Some(specialWeek) => getRuler(specialWeek.map(_.regola)).flatMap {
        case Some(value) if value.length==specialWeek.map(_.regola).distinct.length=> getShift(specialWeek.map(_.turnoId).distinct)
          .flatMap {
            case Some(value) if value.length==specialWeek.map(_.turnoId).distinct.length=>_verifySpecialWeek(specialWeek)
            case _ => future(StatusCodes.ERROR_CODE5)
          }
        case _ =>  future(StatusCodes.ERROR_CODE5)
      }
      case None =>verifyTheoricalRequest(algorithmExecute)
    }
  }

  private def verifyTheoricalRequest(algorithmExecute: AlgorithmExecute): Future[Option[Int]] = {
    InstanceRichiestaTeorica.operation().selectFilter(richiesta=>richiesta.dataInizio<=algorithmExecute.dateI
    && richiesta.dataFine>=algorithmExecute.dateF && richiesta.terminaleId.inSet(algorithmExecute.idTerminal))
      .collect {
        case Some(value) if value.length==algorithmExecute.idTerminal.length=>Some(StatusCodes.SUCCES_CODE)
        case _ =>Some(StatusCodes.ERROR_CODE6)
      }
  }

  final case class InfoReq(idShift:Int,request:Int,assigned:Int,idDay:Int,data:Date)
  final case class InfoDay(data:Date,shift:Int,shift2:Option[Int],straordinario:Option[Int])
  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])

  //TODO tornare tutti conducenti per terminale
  //TODO tornare tutti turni esistenti
  //TODO tornare quantita di giorni trascorsi dal ultimo libero di ogni conducente col suo turno
  //TODO tornare tutti conducenti con assenza
  //TODO tornare tutta la richiesta teorica per il periodo a fare eseguire
  //
  private def getAllData(algorithmExecute: AlgorithmExecute)={
      for{
        driverByTerminal<-InstancePersona.operation().selectFilter(t=>algorithmExecute.idTerminal.contains(t.terminaleId))
        turni<-TurnoOperation.selectAll
        assenza<-InstanceAssenza.operation().execQueryFilter(persona=>persona.id,
          filter=>filter.dataInizio>=algorithmExecute.dateI && filter.dataFine<=algorithmExecute.dateF)
      }yield driverByTerminal
  }
}
