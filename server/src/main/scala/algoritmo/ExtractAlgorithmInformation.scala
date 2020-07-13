package algoritmo

import java.sql.Date
import java.time.LocalDate

import algoritmo.AssignmentOperation._
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, SettimanaN, SettimanaS}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceRichiesta, InstanceRisultato}
import dbfactory.setting.Table.{GiornoTableQuery, RichiestaTableQuery}
import _root_.emitter.ConfigEmitter
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

  private val DEFAULT_INIT_DAY = 0
  private val emitter=ConfigEmitter()
  private val DEFAULT_ASSIGNED = 0
  private val DEFAULT_SEQUENCE = 1

  override def getAllData(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm] =
    for{
      absence<-InstanceAssenza.operation().execQueryFilter(persona=>(persona.personaId,persona.dataInizio,persona.dataFine),
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
      && risultato.data.inSet(allSundayMonthAndEndWeek(sunday,endOfMont))).collect {
      case Some(result) => iterateIdPerson(result,infoForAlgorithm.persons.flatMap(_._2.matricola.toList),previousMonthDate(algorithmExecute.dateI))
      case None =>iterateIdPerson(List.empty,infoForAlgorithm.persons.flatMap(_._2.matricola.toList),previousMonthDate(algorithmExecute.dateI))
    }
  }

   private def allSundayMonthAndEndWeek(sunday:Date,finalDayMont:Date):List[Date]={
     @scala.annotation.tailrec
     def _allSunday(sunday:Date,allSunday:List[Date]=List.empty):List[Date]= sunday match {
       case date if date.compareTo(finalDayMont) < 0 =>_allSunday(subtract(date, 7), allSunday :+ date)
       case date if date.compareTo(finalDayMont) == 0 => allSunday ::: createListDayBetween(subtract(date,-6),date).sortBy(_.getTime)
       case date if date.compareTo(finalDayMont)>0 => (allSunday ::: createListDayBetween(subtract(date,-13),subtract(date,-7))
         :::createListDayBetween(subtract(date,-6),finalDayMont)).distinct.sortBy(_.getTime)
     }
     _allSunday(sunday)
   }

  @scala.annotation.tailrec //verificar getdaynumber
  def searchEndFreeDay(date:List[Date],endDayMonth:Date):Int= date match {
    case firstDate:: next if endDayMonth.compareTo(firstDate)==0 =>searchEndFreeDay(next,subtract(endDayMonth,-1))
    case _::_ =>getDayNumber(endDayMonth)
    case Nil =>6
  }

  private def createPreviousSequence(id:Int,data:Date,result : List[Risultato]):PreviousSequence= result match {
    case  res =>constructPreviousSequence(id,data,res)
    case Nil => PreviousSequence(id,DEFAULT_SEQUENCE,getDayNumber(endOfMonth(data)))

  }
 private def constructPreviousSequence(id:Int,data:Date,result : List[Risultato]):PreviousSequence={
   @scala.annotation.tailrec
   def _sundayMonth(sunday:Date, endDayWeek:Date, number:Int=0):Int=sunday match {
     case date if date.compareTo(endDayWeek)==0=>number+1
     case date =>_sundayMonth(date,subtract(endDayWeek,7),number+1)
   }
   val sunday=result.filter(value=>isSunday(value.data)).distinctBy(_.data).map(_.data).sortBy(_.getTime)
     .map(date=>_sundayMonth(date,getEndDayWeek(data))) //controllar si tiene mas de3 domingos trabajos y el mes es 4
   val allSunday = allSundayMonth(getEndDayWeek(data),endOfMonth(data)).sortBy(_.getTime).map(date=>_sundayMonth(date,getEndDayWeek(data)))
   verifySunday(sunday,allSunday,id,result,data)
 }

 private def verifySunday(sunday:List[Int],allSunday:List[Int],id:Int,result:List[Risultato],data:Date):PreviousSequence={
   val endFreeDay=searchEndFreeDay(result.map(_.data).distinct.sortWith(_.getTime>_.getTime),endOfMonth(data))
   sunday.length match {
     case x if x>3 && allSunday.length==4=>PreviousSequence(id,DEFAULT_SEQUENCE,endFreeDay)
     case x if x>4 && allSunday.length==5=>PreviousSequence(id,DEFAULT_SEQUENCE,endFreeDay)
     case _ if sunday.isEmpty=>PreviousSequence(id,DEFAULT_SEQUENCE,getDayNumber(endOfMonth(data)))
     case _ =>
       val sequence = allSunday.filter(x=> !sunday.contains(x))
       val idSequence = selectIdSequence(allSunday,sequence)
       PreviousSequence(id,idSequence,endFreeDay)
   }
 }
  private val searchSequence:(Int,(Int,(Int,Int)),(Int,Int))=>Int={
    case (idSequence,sequences,previousSunday) if sequences._2.equals(previousSunday)=> idSequence+sequences._1
    case (idSequence,_,_) =>  idSequence
  }

  private def selectIdSequence(listDate:List[Int],sundayFree:List[Int]):Int= (listDate.length,sundayFree) match {
    case (4,List(first,second)) =>sequences4.foldLeft(0)((idSequence,sequences)=>searchSequence(idSequence,sequences,(first,second)))
    case (5,List(first,second)) =>sequences5.foldLeft(0)((idSequence,sequences)=>searchSequence(idSequence,sequences,(first,second)))
  }

  private def iterateIdPerson(result : List[Risultato],idPerson:List[Int],data:Date):Option[List[PreviousSequence]]= {
    @scala.annotation.tailrec
    def _iterateIdPerson(idPerson: List[Int], previousSequence: List[PreviousSequence] = List.empty): Option[List[PreviousSequence]] = idPerson match {
      case ::(id, next) => _iterateIdPerson(next, previousSequence :+ createPreviousSequence(id,data,result.filter(_.personaId == id)))
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
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val thirdDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,24))
  val secondDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,15))
  val firstDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,10))
  val secondDateGroup3: Date =Date.valueOf(LocalDate.of(2020,9,16))
  val firstDateGroup3: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val thirdDateGroup2: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup,thirdDateGroup),1),GruppoA(2,List(firstDateGroup2,secondDateGroup2,secondDateGroup3),2))
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
