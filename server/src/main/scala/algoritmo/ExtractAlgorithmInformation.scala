package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation._
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, SettimanaN, SettimanaS}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceDisponibilita, InstanceRichiesta, InstanceRisultato}
import dbfactory.setting.Table.{GiornoTableQuery, RichiestaTableQuery}
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._
import utils.EmitterHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ExtractAlgorithmInformation{
  def getAllData(algorithmExecute:AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm]
}
object ExtractAlgorithmInformation extends ExtractAlgorithmInformation {

  def apply():ExtractAlgorithmInformation ={
    emitter.start()
    this
  }

  private val DEFAULT_INIT_DAY = 0
  private val emitter: ConfigEmitter =ConfigEmitter("info_algorithm")
  private val DEFAULT_ASSIGNED = 0
  private val DEFAULT_SEQUENCE = 1


  override def getAllData(algorithmExecute: AlgorithmExecute, infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm] =
    for{
      absence<-InstanceAssenza.operation().execQueryFilter(persona=>(persona.personaId,persona.dataInizio,persona.dataFine),
        filter=>filter.dataInizio>=algorithmExecute.dateI && filter.dataFine<=algorithmExecute.dateF)
      allRequest<-getTheoricalRequest(algorithmExecute,infoForAlgorithm)
      allAvailability <- getAvailabilityFixed(infoForAlgorithm)
      previousSequence<-getAllPreviousSequence(algorithmExecute,infoForAlgorithm)
    }yield infoForAlgorithm.copy(absence = absence,allRequest = allRequest,previousSequence = previousSequence,allAvailability = allAvailability)

  private def getTheoricalRequest(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm): Future[Option[List[InfoReq]]] = {
    emitter.sendMessage("costruendo richiesta teorica")
    joinBetweenGiornoAndRichiesta(infoForAlgorithm.theoricalRequest,infoForAlgorithm.shift,algorithmExecute)

  }
  private def joinBetweenGiornoAndRichiesta(theoricalRequest: List[RichiestaTeorica], shift: List[Turno],algorithmExecute: AlgorithmExecute):Future[Option[List[InfoReq]]]={
    EmitterHelper.emitForAlgorithm(EmitterHelper.getFromKey("extract-request"))
    val join = for{
      richiesta<-RichiestaTableQuery.tableQuery()
      giorno <- GiornoTableQuery.tableQuery()
      if(richiesta.richiestaTeoricaId.inSet(theoricalRequest.flatMap(_.idRichiestaTeorica.toList))
        && richiesta.turnoId.inSet(shift.flatMap(_.id.toList)) && richiesta.giornoId===giorno.id)
    }yield(richiesta,giorno)
    InstanceRichiesta.operation().execJoin(join).collect{
      case Some(result) =>
        createInfoDay(result.sortBy(value=>(value._1.richiestaTeoricaId,value._2.idGiornoSettimana)),algorithmExecute,theoricalRequest)
    }
  }

  private def createInfoDay(resultJoin:List[(Richiesta,Giorno)],algorithmExecute: AlgorithmExecute,theoricalRequest: List[RichiestaTeorica]):Option[List[InfoReq]]={
    EmitterHelper.emitForAlgorithm(EmitterHelper.getFromKey("construct-request"))
    val resultJoinWithTerminal=createResultJoinWithTerminal(resultJoin,theoricalRequest)
    @scala.annotation.tailrec
    def _createInfoDay(theoricalRequest: List[RichiestaTeorica],infoReq: List[InfoReq]=List.empty):Option[List[InfoReq]]= theoricalRequest match {
      case ::(request, next) => _createInfoDay(next,createListInfoDay(resultJoinWithTerminal
        .filter(id=>request.idRichiestaTeorica.contains(id._2.richiestaTeoricaId)),request):::infoReq)
      case Nil => Option(constructInfoReq(infoReq,algorithmExecute))
    }
    _createInfoDay(theoricalRequest)
  }

  private def createListInfoDay(resultJoin:List[(Int, Richiesta, Giorno)],richiesta: RichiestaTeorica):List[InfoReq]={
    (DEFAULT_INIT_DAY until computeDaysBetweenDates(richiesta.dataInizio,richiesta.dataFine))
      .flatMap(value=>{
        val day = subtract(richiesta.dataInizio,value)
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

  private def constructInfoReq(infoReq: List[InfoReq], algorithmExecute: AlgorithmExecute):List[InfoReq]=
    algorithmExecute.settimanaSpeciale match {
      case Some(specialWeek) =>
        algorithmExecute.settimanaNormale match {
        case Some(normalWeek) =>
          val infoReqForSpecialWeek = infoReq.partition(res=>specialWeek.exists(x => x.date.compareTo(res.data)==0 && x.turnoId == res.idShift))
          modifiedNormalWeek(normalWeek,infoReqForSpecialWeek._2):::modifiedSpecialWeek(specialWeek,infoReqForSpecialWeek._1)
        case None => modifiedSpecialWeek(specialWeek,infoReq)
      }
      case None => algorithmExecute.settimanaNormale match {
        case Some(normalWeek) => modifiedNormalWeek(normalWeek,infoReq)
        case None =>infoReq
      }
    }

  private def modifiedSpecialWeek(specialWeek: List[SettimanaS], infoReq: List[InfoReq]): List[InfoReq] ={
    @scala.annotation.tailrec
    def _modifiedSpecialWeek(listIndex:List[(Int,Int)], infoReq: List[InfoReq]):List[InfoReq] = listIndex match {
      case ::(index, next) => _modifiedSpecialWeek(next,infoReq.updated(index._2,infoReq(index._2).copy(request = infoReq(index._2).request+index._1)))
      case Nil =>infoReq
    }
    EmitterHelper.emitForAlgorithm(EmitterHelper.getFromKey("modified-special-week"))
  _modifiedSpecialWeek(specialWeek.map(res=>(res.quantita,infoReq.indexWhere(x=>x.idShift==res.turnoId && x.idDay==res.idDay && x.data.compareTo(res.date)==0))),infoReq)

  }

  private  def modifiedNormalWeek(normalWeek: List[SettimanaN], infoReq: List[InfoReq]): List[InfoReq]={
    @scala.annotation.tailrec
    def _modifiedNormalWeek(listIndex:List[(Int,Int)], infoReq: List[InfoReq]):List[InfoReq] = listIndex match {
      case ::(index, next) => _modifiedNormalWeek(next,infoReq.updated(index._2,infoReq(index._2).copy(request = infoReq(index._2).request+index._1)))
      case Nil =>infoReq
    }
    EmitterHelper.emitForAlgorithm(EmitterHelper.getFromKey("modified-normal-week"))
    val t = normalWeek.flatMap(res=>{
      infoReq.foldLeft((List[(Int,Int)](),0)){
        case (index,element) if element.idShift==res.turnoId && element.idDay==res.idDay=>(index._1:+(res.quantita,index._2),index._2+1)
        case (index,_)=>(index._1,index._2+1)
      }._1
    })
    _modifiedNormalWeek(t,infoReq)

  }
  final case class DisponibilitaFixed(idDisponibile:Int,idDay1:Int,idDay2:Int)

  def getAvailabilityFixed(infoForAlgorithm: InfoForAlgorithm):Future[Option[List[DisponibilitaFixed]]] = {
    val day = List("Domenica", "Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato")
    val fixedDriver = infoForAlgorithm.persons.filter(x=> infoForAlgorithm.allContract.toList.flatten.exists(res=>res.turnoFisso && res.idContratto.contains(x._1.contrattoId)))
    InstanceDisponibilita.operation().selectFilter(x=>x.id.inSet(fixedDriver.flatMap(_._2.disponibilita.toList))).collect {
      case Some(value) => Some(value.flatMap(res=>res.idDisponibilita.toList.map(id=>DisponibilitaFixed(id,day.indexWhere(_.equals(res.giorno1)),day.indexWhere(_.equals(res.giorno2))))))
      case None => None
    }
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

  @scala.annotation.tailrec
  def searchEndFreeDay(date:List[Date],endDayMonth:Date):Int= date match {
    case firstDate:: next if endDayMonth.compareTo(firstDate)==0 =>searchEndFreeDay(next,subtract(endDayMonth,-1))
    case _::_ =>getDayNumber(endDayMonth)
    case Nil =>6
  }

  private def createPreviousSequence(id:Int,data:Date,result : List[Risultato]):PreviousSequence= result match {
    case  res if res.nonEmpty =>constructPreviousSequence(id,data,res)
    case Nil => PreviousSequence(id,DEFAULT_ASSIGNED,getDayNumber(endOfMonth(data)))

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
   result match {
     case _::_ =>
       val endFreeDay =  searchEndFreeDay(result.map(_.data).distinct.sortWith(_.getTime>_.getTime),endOfMonth(data))
       createPreviousSequence(sunday,allSunday,id,endFreeDay,data)
     case Nil =>createPreviousSequence(sunday,allSunday,id,DEFAULT_INIT_DAY,data)
   }

 }
  private def createPreviousSequence(sunday:List[Int],allSunday:List[Int],id:Int,endFreeDay:Int,data:Date) = {
    sunday.length match {
      case length if length>3 && allSunday.length==4=>PreviousSequence(id,DEFAULT_SEQUENCE,endFreeDay)
      case length if length>4 && allSunday.length==5=>PreviousSequence(id,DEFAULT_SEQUENCE,endFreeDay)
      case length if (length==1 && allSunday.length==4) || (length==2 && allSunday.length==5) =>PreviousSequence(id,DEFAULT_ASSIGNED,endFreeDay)
      case _ if sunday.isEmpty=>PreviousSequence(id,DEFAULT_ASSIGNED,getDayNumber(endOfMonth(data)))
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
