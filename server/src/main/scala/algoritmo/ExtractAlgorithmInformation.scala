package algoritmo

import java.sql.Date
import java.time.LocalDate

import algoritmo.AssignmentOperation.{InfoForAlgorithm, InfoReq, PreviousSequence}
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, SettimanaN, SettimanaS}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceRichiesta, InstanceRisultato, InstanceStoricoContratto}
import dbfactory.setting.Table.{GiornoTableQuery, RichiestaTableQuery}
import emitter.ConfigEmitter
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait ExtractAlgorithmInformation{
  def getAllData(algorithmExecute:AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm]
}
object ExtractAlgorithmInformation extends ExtractAlgorithmInformation {
  //TODO tornare tutti conducenti per terminale incluso il suo contratto id
  //TODO tornare tutti turni esistenti
  //TODO tornare quantità di giorni trascorsi dal ultimo libero di ogni conducente col suo turno tornare anche secuenza domeniche
  //TODO tornare tutti conducenti con assenza
  //TODO tornare tutta la richiesta teorica per il periodo a fare eseguire
  //
  def apply():ExtractAlgorithmInformation ={
    emitter.start()
    this
  }

  val FIRST_SEQUENCE_4 = (1,2)
  val SECOND_SEQUENCE_4 = (1,4)
  val THIRD_SEQUENCE_4 = (2,3)
  val FOURTH_SEQUENCE_4 = (3,4)
  val ANOTHER_SEQUENCE_4 = (3,0)
  val ANOTHER2_SEQUENCE_4 = (3,1)
  //SEQUENZA MESE 5 DOMENICHE
  val FIRST_SEQUENCE_5 = (1,2)
  val SECOND_SEQUENCE_5 = (1,5)
  val THIRD_SEQUENCE_5 = (2,3)
  val FOURTH_SEQUENCE_5 = (3,4)
  val FIFTH_SEQUENCE_5 = (4,5)
  val ANOTHER_SEQUENCE_5 = (3,0)
  val ANOTHER2_SEQUENCE_5 = (3,1)

  private val DEFAULT_INIT_DAY = 0
  private val emitter=ConfigEmitter()
  private val DEFAULT_ASSIGNED = 0

  override def getAllData(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm] =
    for{
      absence<-InstanceAssenza.operation().execQueryFilter(persona=>(persona.id,persona.dataInizio,persona.dataFine),
        filter=>filter.dataInizio>=algorithmExecute.dateI && filter.dataFine<=algorithmExecute.dateF)
      allRequest<-getTheoricalRequest(algorithmExecute,infoForAlgorithm)
      previousSequence<-getAllPreviousSequence(algorithmExecute,infoForAlgorithm)
    }yield infoForAlgorithm.copy(absence = absence,allRequest = allRequest,previousSequence = previousSequence)

  private def getTheoricalRequest(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm): Future[Option[List[InfoReq]]] = {
    emitter.sendMessage("costruendo richiesta teorica")
    joinBetweenGiornoAndRichiesta(infoForAlgorithm.theoricalRequest,infoForAlgorithm.shift,algorithmExecute)

  }
  private def joinBetweenGiornoAndRichiesta(theoricalRequest: List[RichiestaTeorica], shift: List[Turno],algorithmExecute: AlgorithmExecute):Future[Option[List[InfoReq]]]={
    val join = for{
      richiesta<-RichiestaTableQuery.tableQuery()
      giorno <- GiornoTableQuery.tableQuery()
      if(richiesta.richiestaTeoricaId.inSet(theoricalRequest.flatMap(_.idRichiestaTeorica.toList))
        && richiesta.turnoId.inSet(shift.flatMap(_.id.toList)) && richiesta.giornoId===giorno.id)
    }yield(richiesta,giorno)
    InstanceRichiesta.operation().execJoin(join).collect{
      case Some(result) =>emitter.sendMessage("join with result")
        createInfoDay(result.sortBy(value=>(value._1.richiestaTeoricaId,value._2.idGiornoSettimana)),algorithmExecute,theoricalRequest)
    }
  }

  private def createInfoDay(resultJoin:List[(Richiesta,Giorno)],algorithmExecute: AlgorithmExecute,theoricalRequest: List[RichiestaTeorica]):Option[List[InfoReq]]={
    val resultJoinWithTerminal=createResultJoinWithTerminal(resultJoin,theoricalRequest)
    @scala.annotation.tailrec
    def _createInfoDay(theoricalRequest: List[RichiestaTeorica],infoReq: List[InfoReq]=List.empty):Option[List[InfoReq]]= theoricalRequest match {
      case ::(request, next) => _createInfoDay(next,createListInfoDay(resultJoinWithTerminal
        .filter(id=>request.idRichiestaTeorica.contains(id._2.richiestaTeoricaId)),algorithmExecute):::infoReq)
      case Nil => Option(infoReq)
    }
    _createInfoDay(theoricalRequest)
  }
  private def createListInfoDay(resultJoin:List[(Int, Richiesta, Giorno)],algorithmExecute: AlgorithmExecute):List[InfoReq]={
    (DEFAULT_INIT_DAY until computeDaysBetweenDates(algorithmExecute.dateI,algorithmExecute.dateF))
      .flatMap(value=>{
        val day = subtract(algorithmExecute.dateI,value)
        emitter.sendMessage(s"costruendo infoReq for day $day")
        createInfoDayCaseClass(resultJoin,day)
      }).toList
  }
  private def createInfoDayCaseClass(resultJoin:List[(Int, Richiesta, Giorno)],day:Date):List[InfoReq]={
    @scala.annotation.tailrec
    def _createInfoDayCaseClass(resultJoin:List[(Int, Richiesta, Giorno)], listInfoReq:List[InfoReq]=List.empty):List[InfoReq]= resultJoin match {
      case ::(reqTerminalAndDay, next) =>_createInfoDayCaseClass(next,listInfoReq:+
        InfoReq(reqTerminalAndDay._2.turnoId,reqTerminalAndDay._3.quantita,
          DEFAULT_ASSIGNED,reqTerminalAndDay._3.idGiornoSettimana,day,reqTerminalAndDay._1))
      case Nil =>listInfoReq
    }
    val dayInWeek  = if(getDayNumber(day)!=0)getDayNumber(day) else 7
    _createInfoDayCaseClass(resultJoin.filter(value=>value._3.idGiornoSettimana==dayInWeek))
  }
  private def createResultJoinWithTerminal(resultJoin:List[(Richiesta,Giorno)],theoricalRequest: List[RichiestaTeorica]): List[(Int, Richiesta, Giorno)] =
    resultJoin.map(result=> (theoricalRequest.find(theorical => theorical.idRichiestaTeorica.contains(result._1.richiestaTeoricaId)) match {
      case Some(value) => value.terminaleId
      case None =>0
    },result._1,result._2))

  private def getAllPreviousSequence(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm): Future[Option[List[PreviousSequence]]] ={
    val (sunday,endOfMont) = (getEndDayWeek(previousMonthDate(algorithmExecute.dateI)),endOfMonth(previousMonthDate(algorithmExecute.dateI)))
    InstanceRisultato.operation().selectFilter(risultato=>risultato.personeId.inSet(infoForAlgorithm.persons.flatMap(_._2.matricola.toList))
      && risultato.data.inSet(allSundayMonth(sunday,endOfMont))).collect {
      case Some(result) => iterateIdPerson(result,infoForAlgorithm.persons.flatMap(_._2.matricola.toList))
      case None =>iterateIdPerson(List.empty,infoForAlgorithm.persons.flatMap(_._2.matricola.toList))
    }
  }

   private def allSundayMonth(sunday:Date,finalDayMont:Date):List[Date]={
     @scala.annotation.tailrec
     def _allSunday(sunday:Date,allSunday:List[Date]=List.empty):List[Date]= sunday match {
       case date if date.compareTo(finalDayMont) < 0 =>_allSunday(subtract(date, 7), allSunday :+ date)
       case date if date.compareTo(finalDayMont) == 0 => allSunday ::: createListDayBetween(subtract(date,-6),date).sortBy(_.getTime)
       case date if date.compareTo(finalDayMont)>0 => (allSunday ::: createListDayBetween(subtract(date,-13),subtract(date,-7))
         :::createListDayBetween(subtract(date,-6),finalDayMont)).distinct.sortBy(_.getTime)
     }
     _allSunday(sunday)
   }
  //  final case class PreviousSequence(idDriver:Int,sequenza:Int,distanceFreeDay:Int)
  //SEQUENZA MESE 4 DOMENICHE
  val returnSundayWork:List[Date]=>(Int,Int)=date=>{
    sundayWork(date,(0,0))
  }
  private val sundayWork:(List[Date],(Int,Int))=>(Int,Int)={
    case (head::next,_) if isFirstSunday(head)=>sundayWork(next,(1,2))
    case (head::next,_) if isSecondSunday(head)=>sundayWork(next,(1,2))
    case (head::next,_) if isThirdSunday(head)=>sundayWork(next,(1,2))
    case (head::next,_) if isFourthSunday(head)=>sundayWork(next,(1,2))
    case (_,sequence)=>sequence
  }
  private def searchEndFreeDay(date:List[Date]):Int={
    @scala.annotation.tailrec
    def _searchEndFreeDay(date:List[Date], day:Int=0):Int= date match {
      case firstDate::secondDate:: next if subtract(firstDate,-1).compareTo(secondDate)==0=>_searchEndFreeDay(secondDate::next,day=day+1)
      case _::_ =>day
      case Nil =>day
    }
    _searchEndFreeDay(date)
  }
  private def createPreviousSequence(id:Int,result : List[Risultato]):PreviousSequence={
    val sunday=result.filter(value=>isSunday(value.data)).distinctBy(_.data)
    val sundayWorkVal = if(sunday.length>=3)(3,0) else returnSundayWork(sunday.map(_.data))
    createPreviousSequenceMonth4Sunday(sundayWorkVal,searchEndFreeDay(result.map(_.data).distinct.sortWith(_.getTime>_.getTime)),id)
  }
  private  def createPreviousSequenceMonth4Sunday(sundays:(Int,Int),endedWeek:Int,id:Int):PreviousSequence= sundays match {
    case FIRST_SEQUENCE_4 => PreviousSequence(id,1,endedWeek)
    case SECOND_SEQUENCE_4 =>PreviousSequence(id,2,endedWeek)
    case THIRD_SEQUENCE_4 => PreviousSequence(id,3,endedWeek)
    case FOURTH_SEQUENCE_4 => PreviousSequence(id,4,endedWeek)
    case ANOTHER_SEQUENCE_4 => PreviousSequence(id,0,endedWeek)
    case ANOTHER2_SEQUENCE_4 => PreviousSequence(id,0,endedWeek)
    case _ => PreviousSequence(id,0,endedWeek)
  }
  private def iterateIdPerson(result : List[Risultato],idPerson:List[Int]):Option[List[PreviousSequence]]= {
    @scala.annotation.tailrec
    def _iterateIdPerson(idPerson: List[Int], previousSequence: List[PreviousSequence] = List.empty): Option[List[PreviousSequence]] = idPerson match {
      case ::(id, next) => _iterateIdPerson(next, previousSequence :+ createPreviousSequence(id,result.filter(_.personaId == id)))
      case Nil => Option(previousSequence)
    }
    _iterateIdPerson(idPerson)
  }
}
object t extends App{
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,9,30))
  val terminals=List(15)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,11))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2))
  val specialWeek = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,7,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,7,8))))
  val threeSaturday=false
  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  Algoritmo.shiftAndFreeDayCalculus(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>println(":)")
  }
  while (true){}
}

