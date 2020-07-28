package dbfactory.operation

import java.sql.Date

import algoritmo.AssignmentOperation.{Info, InfoDay}
import caseclass.CaseClassDB.{Presenza, Risultato, Turno}
import caseclass.CaseClassHttpMessage._
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona, InstancePresenza, InstanceRisultato, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{PersonaTableQuery, RisultatoTableQuery, StoricoContrattoTableQuery, TurnoTableQuery}
import dbfactory.util.Helper._
import messagecodes.StatusCodes
import persistence.ConfigEmitterPersistence
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._
import utils.EmitterHelper

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
  def getResultAlgorithm(idTerminal:Int,dateI:Date,dateF:Date):Future[(Option[List[ResultAlgorithm]],List[Date])]

  /**
   * method that save result for algorithm in database
   * @param result all information that algorithm to generate
   * @param dataI init time period of the algorithm, this date serve to know if exist info that need to be replace
   * @return Future of Option of Int that represent status of Operation this result can ben:
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] operation finished successfully
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if insertAllBatch have a problem
   */
  def verifyAndSaveResultAlgorithm(result: List[Info],dataI:Date): Future[Option[Int]]

  /**
   * Method that represent control to result for verify if exist old info
   * @param idTerminal all terminal that we want run
   * @param dataI date that represent init time period
   * @param dataF date that represent ended time period
   * @return Future of List of Option with status code of operation that can be:
   *         [[messagecodes.StatusCodes.INFO_CODE1]] if not exist info in this time period
   *         [[messagecodes.StatusCodes.INFO_CODE2]] if exist a time period next to dataF
   *         [[messagecodes.StatusCodes.INFO_CODE3]] if exist new driver or minus driver in terminal that we want to run
   *         [[messagecodes.StatusCodes.INFO_CODE4]] if only re-write info in the time period
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if not exist drivers for some terminal
   */
  def verifyOldResult(idTerminal:List[Int],dataI:Date,dataF:Date): Future[List[Option[Int]]]
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
            turni <- InstanceTurno.operation().selectFilter(filter => filter.id.inSet(listTurni.toList.flatten))
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
      result <- InstanceRisultato.operation().execQueryFilter(risultato=>(risultato.data,risultato.id),
        risultato=>risultato.id===idRisultato)
      persona <- InstancePersona.operation().execQueryFilter(persona=>persona.id,persona=>persona.id===idPersona)
      finalResult<- (result,persona) match{
        case (None, _) => Future.successful(Some(StatusCodes.ERROR_CODE1))
        case (_, None) => Future.successful(Some(StatusCodes.ERROR_CODE2))
        case (_, _) => update(idRisultato,idPersona)
      }
    }yield {
      EmitterHelper.notificationExtraordinary(idPersona,result)
      finalResult
    }

  private def update(idRisultato:Int,idPersona:Int): Future[Option[Int]] =
    InstanceRisultato.operation().execQueryUpdate(risultato=>risultato.personeId,risultato=>risultato.id===idRisultato,
      idPersona).result()

  override def getResultAlgorithm(idTerminal:Int,dateI: Date, dateF: Date): Future[(Option[List[ResultAlgorithm]],List[Date])] = {
    val joinPersonContract = for{
      person<- PersonaTableQuery.tableQuery()
      contract <- StoricoContrattoTableQuery.tableQuery()
      if(person.id===contract.personaId && person.terminaleId===Option(idTerminal)
        && ((contract.dataInizio<=dateI && contract.dataFine>=dateF)
        || (contract.dataInizio>=dateI && contract.dataFine<=dateF)
        || (contract.dataFine>=dateI && contract.dataFine<=dateF)
        || (contract.dataInizio>=dateI && contract.dataInizio<=dateF)
        || (contract.dataInizio<=dateF && !contract.dataFine.isDefined)))
    }yield(person.id,person.terminaleId,contract.dataInizio,contract.dataFine)

    InstancePersona.operation().execJoin(joinPersonContract)
      .flatMap {
        case Some(result) => getResultByPerson(result,dateI,dateF).collect(x=>(x,createListDayBetween(dateI,dateF)))
        case None =>Future.successful((None,List.empty))
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

  private def zipFuture(driver:InfoPerson,infoResult:InfoForResult,result:Future[Option[List[ResultAlgorithm]]]): Future[Option[List[ResultAlgorithm]]] =
    result.zip(createInfoDates(infoResult.result.filter(_.personaId==driver.idPersona),infoResult,driver.idPersona,driver)
      .collect(value=>ResultAlgorithm(driver.idPersona,driver.terminal,value.sortBy(value=>(value.date,value.turno,value.turno2))))).map{
      case (option, algorithm) => Option(option.toList.flatten:+algorithm)
    }


  private val differenceBetweenDate:Long=>Long=>Long=finishDate=>initDate=>finishDate-initDate

  private def createInfoDates(result:List[Risultato],infoResult:InfoForResult,idPerson:Int,driver:InfoPerson):Future[List[InfoDates]]=
    InstanceAssenza.operation().execQueryFilter(assenza=>(assenza.dataInizio,assenza.dataFine),
      filter=>((filter.dataInizio<=infoResult.dateInit && filter.dataFine>=infoResult.dateFinish)
        ||(filter.dataInizio>=infoResult.dateInit && filter.dataFine<=infoResult.dateFinish)
        ||(filter.dataFine>=infoResult.dateInit && filter.dataFine<=infoResult.dateFinish)
        ||(filter.dataInizio>=infoResult.dateInit && filter.dataInizio<=infoResult.dateFinish)) && filter.personaId===idPerson).collect {
      case Some(value) =>
        insertInfoShiftInInfoDates(result,infoResult,driver,insertAllAbsence(value,infoResult))
     case None => insertInfoShiftInInfoDates(result,infoResult,driver)
    }




  private def insertInfoShiftInInfoDates(result:List[Risultato], infoResult:InfoForResult, driver:InfoPerson, infoAbsence:List[InfoDates]=List.empty):List[InfoDates]={
    @scala.annotation.tailrec
    def _insertInfoShiftInInfoDates(infoResult:InfoForResult, infoAbsence:List[InfoDates]):List[InfoDates]= infoResult.result match {
      case first::second::third::next if first.data.compareTo(second.data)==0 && second.data.compareTo(third.data)==0 =>
        _insertInfoShiftInInfoDates(infoResult.copy(result = next),infoAbsence:+infoDateWithThreeShift(first.data)(first.turnoId,second.turnoId,third.turnoId)(infoResult))
      case first::second::next if first.data.compareTo(second.data)==0 =>
        _insertInfoShiftInInfoDates(infoResult.copy(result = next),infoAbsence:+infoDateWithTwoShift(first.data)(first.turnoId,second.turnoId)(infoResult))
      case first::next  => _insertInfoShiftInInfoDates(infoResult.copy(result = next),infoAbsence:+infoDateWithOneShift(first.data)(first.turnoId)(infoResult))
      case Nil =>infoAbsence
    }

    val orderResult=result.filter(day => !infoAbsence.exists(x => x.date.compareTo(day.data) == 0)).sortBy(value => (value.data, value.personaId, value.turnoId))
    val resultContract = verifyContract(driver.initContract,driver.finishContract,infoResult)
    val allFreeDay = assignFreeDay(infoAbsence:::resultContract,orderResult,infoResult.dateInit,infoResult.dateFinish)
    _insertInfoShiftInInfoDates(infoResult.copy(result=orderResult),allFreeDay:::infoAbsence)

  }

  def assignFreeDay(infoDates: List[InfoDates], orderResult: List[Risultato],dateInit: Date,dateFinish:Date):List[InfoDates] = {
    createListDayBetween(dateInit,dateFinish).filter(day=> !infoDates.exists(_.date.compareTo(day)==0) && !orderResult.exists(_.data.compareTo(day)==0))
      .map(day=>InfoDates(day,FREE_DAY_VALUE,Some(FREE_DAY_VALUE)))
  }

  def verifyContract(initContract:Date,finishContract:Date,infoResult:InfoForResult): List[InfoDates] ={
    var allDayWithoutContract:List[InfoDates]=List.empty
    if(initContract.compareTo(infoResult.dateInit)>0)
      allDayWithoutContract = assignDayWithoutContract(infoResult,initContract)

    if(finishContract.compareTo(infoResult.dateFinish)<0)
      allDayWithoutContract = allDayWithoutContract:::assignDayWithoutContractFinish(infoResult,finishContract)
    allDayWithoutContract
  }

  def assignDayWithoutContract(infoResult: InfoForResult,initContract:Date): List[InfoDates] = {
        createListDayBetween(infoResult.dateInit,initContract).map(day=>InfoDates(day,WITHOUT_CONTRACT,Some(WITHOUT_CONTRACT)))
  }

  def assignDayWithoutContractFinish(infoResult: InfoForResult,finish:Date): List[InfoDates] = {
    createListDayBetween(finish,infoResult.dateFinish).map(day=>InfoDates(day,WITHOUT_CONTRACT,Some(WITHOUT_CONTRACT)))
  }

  private val infoDateWithThreeShift: Date=>(Int,Int,Int)=>InfoForResult=>InfoDates=date=>(shift,shift2,shift3)=>infoResult=>InfoDates(date,
    selectNameTurno(infoResult)(shift),Some(selectNameTurno(infoResult)(shift2)),Some(selectNameTurno(infoResult)(shift3)))

  private val infoDateWithTwoShift: Date=>(Int,Int)=>InfoForResult=>InfoDates=date=>(shift,shift2)=>infoResult=>InfoDates(date,
    selectNameTurno(infoResult)(shift),Some(selectNameTurno(infoResult)(shift2)))

  private val infoDateWithOneShift: Date=>Int=>InfoForResult=>InfoDates=date=>shift=>infoResult=>InfoDates(date,
    selectNameTurno(infoResult)(shift))

  private def insertAllAbsence(listAbsence:List[(Date,Date)],infoResult:InfoForResult): List[InfoDates] =
    listAbsence.map(value => if(value._1.compareTo(infoResult.dateInit)<0)(infoResult.dateInit,value._2) else value)
        .map(value =>if(value._2.compareTo(infoResult.dateFinish)>0)(value._1,infoResult.dateFinish) else value)
        .map(date=>date._1->differenceBetweenDate(date._2.toLocalDate.toEpochDay)(date._1.toLocalDate.toEpochDay))
        .flatMap(date_diff=>(DEFAULT_INIT to date_diff._2).map(value=>date_diff._1.toLocalDate.plusDays(value))
        .map(day=>InfoDates(Date.valueOf(day),ABSENCE_VALUE,Some(ABSENCE_VALUE))))

  private val selectNameTurno:InfoForResult=>Int=>String=infoResult=>idTurno=>
    infoResult.turno.filter(id => id._1 == idTurno) match {
      case List(turno) =>turno._2
      case Nil =>WITHOUT_SHIFT
    }

  override def verifyOldResult(idTerminal:List[Int],dataI:Date,dataF:Date): Future[List[Option[Int]]]={
    Future.sequence(idTerminal.map(id=>verifyOldResult(id,dataI,dataF)))
  }

  private def verifyOldResult(idTerminal:Int,dataI:Date,dataF:Date): Future[Option[Int]]={
      InstancePersona.operation().execQueryFilter(x=>x,x=>Option(x.terminaleId).isDefined).flatMap {
        case Some(value) =>
          val driverTerminal = value.filter(_.idTerminale.contains(idTerminal))
          InstanceRisultato.operation().selectFilter(x=> x.data>=dataI && x.personeId.inSet(driverTerminal.flatMap(_.matricola.toList))).collect {
            case Some(result) if result.exists(date=>date.data.compareTo(subtract(dataF,1))==0)=>Some(StatusCodes.INFO_CODE2)
            case Some(result) if result.map(_.personaId).distinct.length>driverTerminal.flatMap(_.matricola.toList).length ||
              result.map(_.personaId).distinct.length<driverTerminal.flatMap(_.matricola.toList).length =>Some(StatusCodes.INFO_CODE3)
            case None =>Some(StatusCodes.INFO_CODE1)
            case _ =>Some(StatusCodes.INFO_CODE4)
          }
        case None => Future.successful(Some(StatusCodes.ERROR_CODE1))
      }

  }
   private val infoCopy:Info=>Info=inf=>inf.copy(infoDay= inf.infoDay.filter(x=> !x.absence && !x.freeDay))

   private def insertPresenza(result: List[Info],dataI:Date)={
      val finalResult = result.map(infoCopy).flatMap(x =>{
        x.infoDay.collect{
          case InfoDay(data, Some(shift), None, None,_,_)=>x.idDriver.toList.map(id=>Presenza(data,id,shift,isStraordinario = false))
          case InfoDay(data, Some(shift), Some(shift2), None,_,_)=>x.idDriver.toList.flatMap(id=>List(Presenza(data,id,shift,isStraordinario = false),
            Presenza(data,id,shift2,isStraordinario = false)))
          case InfoDay(data, Some(shift), Some(shift2), Some(straordinario),_,_)=>
            x.idDriver.toList.flatMap(id=>List(Presenza(data,id,shift,isStraordinario = false),Presenza(data,id,shift2,isStraordinario = false),Presenza(data,id,straordinario,isStraordinario = true)))
          case InfoDay(data, Some(shift),None, Some(straordinario),_,_) =>
            x.idDriver.toList.flatMap(id=>List(Presenza(data,id,shift,isStraordinario = false),Presenza(data,id,straordinario,isStraordinario = true)))
        }

      }).flatten
      InstancePresenza.operation().selectFilter(res=>res.data>=dataI && res.personeId.inSet(finalResult.map(_.personaId).distinct)).flatMap {
        case Some(value) =>presenzeForUpdate(value,finalResult)
        case None =>insertAllBatchPresenza(finalResult)
      }
    }
  override def verifyAndSaveResultAlgorithm(result: List[Info],dataI:Date): Future[Option[Int]] = {
    val finalResult = result.map(infoCopy).flatMap(x=>{
      x.infoDay.collect{
        case InfoDay(data, Some(shift), None, None,_,_)=>x.idDriver.toList.map(id=>Risultato(data,id,shift))
        case InfoDay(data, Some(shift), Some(shift2), None,_,_)=>x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,shift2)))
        case InfoDay(data, Some(shift), Some(shift2), Some(straordinario),_,_)=>
          x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,shift2),Risultato(data,id,straordinario)))
        case InfoDay(data, Some(shift),None, Some(straordinario),_,_) =>
          x.idDriver.toList.flatMap(id=>List(Risultato(data,id,shift),Risultato(data,id,straordinario)))
      }
    }).flatten
    insertPresenza(result,dataI)
    InstanceRisultato.operation().selectFilter(res=>res.data>=dataI && res.personeId.inSet(finalResult.map(_.personaId).distinct)).flatMap {
      case Some(value) =>resultForUpdate(value,finalResult)
      case None =>insertAllBatchResult(finalResult)
    }
  }

  private def resultForUpdate(oldR: List[Risultato],newR: List[Risultato]): Future[Option[Int]] ={
    insertAllBatchResult(newR).flatMap {
      case Some(StatusCodes.SUCCES_CODE) => deleteAllRecursive(oldR)
      case _ => Future.successful(Option(StatusCodes.ERROR_CODE1))
    }
  }
  private def deleteAllRecursive(oldR: List[Risultato])={
    val idForDelete = oldR.flatMap(_.idRisultato.toList)
    val myTake = if(idForDelete.length>1000) 1000 else idForDelete.length
    def _deleteAllRecursive(delete:List[Int],rest:List[Int]):Future[Option[Int]]= rest match {
      case Nil => deleteAll(delete).flatMap{
        case Some(value) if value>0  => Future.successful(Option(StatusCodes.SUCCES_CODE))
        case _ =>_deleteAllRecursive(delete,rest)
      }
      case ::(_, _) => deleteAll(delete).flatMap{
        case Some(value) if value>0 => val (delete,resto) = rest.splitAt(myTake)
          _deleteAllRecursive(delete,resto)
        case _ =>_deleteAllRecursive(delete,rest)
      }
    }
    val (delete,rest) = idForDelete.splitAt(myTake)
    _deleteAllRecursive(delete,rest)
  }

  private def insertAllBatchResult(finalResult: List[Risultato]): Future[Option[Int]] = {
    super.insertAllBatch(finalResult).collect {
      case Some(_) => Some(StatusCodes.SUCCES_CODE)
      case None =>Some(StatusCodes.ERROR_CODE1)
    }
  }

  private def presenzeForUpdate(oldR: List[Presenza],newR: List[Presenza]): Future[Option[Int]] ={
    insertAllBatchPresenza(newR).flatMap {
      case Some(StatusCodes.SUCCES_CODE) => deleteAllRecursivePresenza(oldR)
      case _ => Future.successful(Option(StatusCodes.ERROR_CODE1))
    }
  }
  private def deleteAllRecursivePresenza(oldR: List[Presenza])={
    val idForDelete = oldR.flatMap(_.idPresenza.toList)
    val myTake = if(idForDelete.length>1000) 1000 else idForDelete.length
    def _deleteAllRecursive(delete:List[Int],rest:List[Int]):Future[Option[Int]]= rest match {
      case Nil => PresenzaOperation.deleteAll(delete).flatMap{
        case Some(value) if value>0  => Future.successful(Option(StatusCodes.SUCCES_CODE))
        case _ =>_deleteAllRecursive(delete,rest)
      }
      case ::(_, _) =>PresenzaOperation.deleteAll(delete).flatMap{
        case Some(value) if value>0 => val (delete,resto) = rest.splitAt(myTake)
          _deleteAllRecursive(delete,resto)
        case _ =>_deleteAllRecursive(delete,rest)
      }
    }
    val (delete,rest) = idForDelete.splitAt(myTake)
    _deleteAllRecursive(delete,rest)
  }

  private def insertAllBatchPresenza(finalResult: List[Presenza]): Future[Option[Int]] = {
    PresenzaOperation.insertAllBatch(finalResult).collect {
      case Some(_) => Some(StatusCodes.SUCCES_CODE)
      case None =>Some(StatusCodes.ERROR_CODE1)
    }
  }
}