package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation.InfoForAlgorithm
import algoritmo.ExtractAlgorithmInformation.DisponibilitaFixed
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA}
import dbfactory.operation.RisultatoOperation
import utils.DateConverter._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
trait AssignmentOperation{
  def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]):Unit
}

object AssignmentOperation extends AssignmentOperation {

  private val FULL_AND_PART_TIME_5X2 = List(1, 2, 3, 4)
  final case class InfoForAlgorithm(shift: List[Turno], theoricalRequest: List[RichiestaTeorica],
                                    persons: List[(StoricoContratto, Persona)], allContract: Option[List[Contratto]] = None, absence: Option[List[(Int, Date, Date)]] = None,
                                    allRequest: Option[List[InfoReq]] = None, previousSequence: Option[List[PreviousSequence]] = None, allAvailability:Option[List[DisponibilitaFixed]]=None)

  final case class InfoReq(idShift: Int, request: Int, assigned: Int, idDay: Int, data: Date, idTerminal: Int)
  final case class PreviousSequence(idDriver: Int, sequenza: Int, distanceFreeDay: Int)
  final case class InfoDay(data: Date, shift: Option[Int] = None, shift2: Option[Int] = None, straordinario: Option[Int] = None, freeDay: Boolean = false, absence: Boolean = false)
  final case class Info(idDriver: Option[Int], idTerminal:  Option[Int], isFisso: Boolean, tipoContratto: Int, infoDay: List[InfoDay])

  private val emitter = ConfigEmitter("info_algorithm")
  private val FIRST_RULER=1
  private val SECOND_RULER=2
  private val THIRD_RULER=3
  //A POSTO
  override def initOperationAssignment(algorithmExecute: AlgorithmExecute, infoForAlgorithm: Future[InfoForAlgorithm]): Unit = {
    infoForAlgorithm.foreach(info => {
      emitter.sendMessage("Iniziando processo di assegnazione")
      emitter.sendMessage("Separando Terminali")
      info.theoricalRequest.map(terminal => {
        val personaFilter = info.persons.filter(_._2.idTerminale.contains(terminal.terminaleId))
        info.copy(persons = personaFilter,
          allRequest = info.allRequest.map(_.filter(_.idTerminal == terminal.terminaleId)),
          theoricalRequest = info.theoricalRequest.filter(_.terminaleId == terminal.terminaleId),
          previousSequence = info.previousSequence.map(_.filter(value => personaFilter.map(_._2.matricola).exists(id => id.contains(value.idDriver)))))
      }).foreach(info => startAlgorithm(info, algorithmExecute))
    })
  }
  //A POSTO
  private def startAlgorithm(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute): Unit = Future {
    val (driver5x2, driver6x1) = infoForAlgorithm.persons.partition(idPerson => FULL_AND_PART_TIME_5X2.contains(idPerson._1.contrattoId))
    infoForAlgorithm.absence match {
      case Some(value) => emitter.sendMessage("Assign absence")
        assignAbsence(driver5x2.map(v => (v._1.contrattoId,v._2)),infoForAlgorithm.allContract,value)
        .zip(assignAbsence(driver6x1.map(v => (v._1.contrattoId,v._2)),infoForAlgorithm.allContract,value))
        .map{
          case (listInfo5x2, listInfo6x1) => assignSundayAndSaturday(infoForAlgorithm,algorithmExecute,driver5x2,driver6x1,listInfo5x2,listInfo6x1)
        }
      case None => assignSundayAndSaturday(infoForAlgorithm,algorithmExecute,driver5x2,driver6x1)
    }
  }

  def assignSundayAndSaturday(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute, driver5x2: List[(StoricoContratto, Persona)], driver6x1: List[(StoricoContratto, Persona)], listInfo5x2: List[Info] = List.empty, listInfo6x1: List[Info] = List.empty): Unit = {
    emitter.sendMessage("Assign Saturday and Sunday 5x2 and Sunday 6x1")
    assignSaturdayAndSunday5x2(infoForAlgorithm.allContract, algorithmExecute, driver5x2.map(v => (v._1.contrattoId,v._2)),listInfo5x2)
      .zip(assignSunday6x1(infoForAlgorithm, algorithmExecute, driver6x1,listInfo6x1)).map{
      case (driver5x2, driver6x1) => groupForDriver(infoForAlgorithm,algorithmExecute,driver5x2:::driver6x1)
    }
  }

  //A POSTO
  private def assignAbsence(drivers: List[(Int,Persona)],contracts: Option[List[Contratto]],absence: List[(Int, Date,Date)]): Future[List[Info]] = Future{

    @scala.annotation.tailrec
    def _absignAbsence(drivers: List[(Int, Persona)], result: List[Info] = List.empty): List[Info] = drivers match {
      case ::(driver, next) if absence.exists(x => driver._2.matricola.contains(x._1)) => _absignAbsence(next, result :+ Info(driver._2.matricola,
        driver._2.idTerminale,isFisso(contracts, driver._1), driver._1, _iterateDate(driver._2.matricola)))
      case ::(_,next) => _absignAbsence(next,result)
      case Nil => result
    }

    def _iterateDate(driverId: Option[Int]): List[InfoDay] = {
      absence.filter(id=>driverId.contains(id._1)).map(absence => createListDayBetween(absence._2, absence._3))
        .flatMap(res => res.map(date => InfoDay(date, absence =true)))
    }
    _absignAbsence(drivers)
  }
  //A POSTO
  private def assignSaturdayAndSunday5x2(allContract: Option[List[Contratto]], algorithmExecute: AlgorithmExecute, driver: List[(Int, Persona)], result: List[Info]): Future[List[Info]] = Future{
    @scala.annotation.tailrec
    def _assignSaturdayAndSunday(drivers: List[(Int, Persona)], date: Date, info: List[Info] = List.empty): List[Info] =  drivers match {
      case ::(driver, next) => _assignSaturdayAndSunday(next, date, upsertListInfo(info, List(Info(driver._2.matricola, driver._2.idTerminale,
        isFisso =  isFisso(allContract,driver._1), driver._1, _date(date)))))
      case Nil => info
    }

    @scala.annotation.tailrec
    def _date(date: Date, infoDay: List[InfoDay] = List.empty): List[InfoDay] =
      if(date.compareTo(algorithmExecute.dateF) < 0 && getEndDayWeek(date).compareTo(algorithmExecute.dateF) < 0 )
        _date(subtract(getEndDayWeek(date), 1),
          infoDay ::: List(InfoDay(getEndDayWeek(date), freeDay = true), InfoDay(subtract(getEndDayWeek(date), -1), freeDay = true)))
      else infoDay.sortBy(_.data)

    _assignSaturdayAndSunday(driver, algorithmExecute.dateI,result)

  }

  private val isFisso: (Option[List[Contratto]], Int) => Boolean = (allContract,idContratto) => allContract.map(_.filter(_.idContratto.contains(idContratto)).map(_.turnoFisso)).toList.flatten match {
    case List(isFisso) => isFisso
  }

  private val isPartTime: (Option[List[Contratto]], Int) => Boolean = (allContract,idContratto) => allContract.map(_.filter(_.idContratto.contains(idContratto)).map(_.partTime)).toList.flatten match {
    case List(isPartTime) => isPartTime
  }

  def allSundayMonth(sunday:Date,finalDayMont:Date):List[Date]={
    @scala.annotation.tailrec
    def _allSunday(sunday:Date,allSunday:List[Date]=List.empty): List[Date] = sunday match {
      case date if date.compareTo(finalDayMont) < 0 =>_allSunday(subtract(date, 7), allSunday :+ date)
      case date if date.compareTo(finalDayMont) == 0 => allSunday :+ date
      case date if date.compareTo(finalDayMont) > 0 => allSunday
    }
    _allSunday(sunday)
  }
 //A Posto
  private def assignSunday6x1(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute, driver: List[(StoricoContratto, Persona)], result: List[Info]): Future[List[Info]] = Future{

    @scala.annotation.tailrec
    def iterateDate(dataI: Date, driverGroup: List[(Int,List[(Int,Persona)])], previousSequence: Option[List[PreviousSequence]], result: List[Info] = List.empty):List[Info] = dataI match{
      case x if x.compareTo(startMonthDate(algorithmExecute.dateF)) == 0 =>
        iterateDriversGroups(driverGroup,x, previousSequence = previousSequence,result = result)._1
      case date => val resultIterateMap = iterateDriversGroups(driverGroup,date, previousSequence, result = result)
        iterateDate(nextMonthDate(date),driverGroup, resultIterateMap._2, resultIterateMap._1)
    }

    @scala.annotation.tailrec
    def iterateDriversGroups(driverGroups: List[(Int,List[(Int,Persona)])],date: Date,previousSequence: Option[List[PreviousSequence]], assigned: Map[Int,Int] = Map(1 ->0, 2 -> 0, 3 -> 0, 4 -> 0, 5-> 0),result: List[Info] = List.empty): (List[Info], Option[List[PreviousSequence]]) = driverGroups match {
      case ::(group, next) =>
        val previousSundays = allSundayMonth(getEndDayWeek(previousMonthDate(date)),endOfMonth(previousMonthDate(date)))
        val sundays = allSundayMonth(getEndDayWeek(date),endOfMonth(date))
        val assignment = assignBalancedSundays(group._2,sundays,previousSundays,infoForAlgorithm.allContract,previousSequence,assigned)
        iterateDriversGroups(next,date,assignment._3,assignment._2, upsertListInfo(result, assignment._1))
      case Nil => (result, previousSequence)
    }

    iterateDate(algorithmExecute.dateI,partitionAndUnion(infoForAlgorithm,driver),infoForAlgorithm.previousSequence, result)
   }
  //A POSTO
  private def partitionAndUnion(infoForAlgorithm: InfoForAlgorithm,drivers: List[(StoricoContratto, Persona)])={
    val (fissi,rotatori) = drivers.groupBy(_._1.contrattoId).toList.partition(driver => infoForAlgorithm.allContract.toList.flatten
      .exists(contract => contract.idContratto.contains(driver._1) && contract.turnoFisso))

    val(partTime,fullTime) = fissi.partition(fisso => infoForAlgorithm.allContract.toList.flatten
      .exists(contract => contract.idContratto.contains(fisso._1) && contract.partTime))

    rotatori.map(rotatorio => rotatorio._1 -> rotatorio._2.map(contractAndDriver =>
      (contractAndDriver._1.contrattoId,contractAndDriver._2))):::createGroupForShift(partTime):::createGroupForShift(fullTime)
  }
  //A POSTO
  private def createGroupForShift(drivers: List[(Int, List[(StoricoContratto, Persona)])]):List[(Int, List[(Int, Persona)])] =
    drivers.flatMap(_._2.groupBy(_._1.turnoId).toList).map(driverMap=>driverMap._1.head->driverMap._2
      .map(contractAndDriver=>(contractAndDriver._1.contrattoId,contractAndDriver._2)))

  // A POSTO
  private def groupForDriver(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute,assigned:List[Info]): Unit ={
    emitter.sendMessage("Assign Group for Driver")
    algorithmExecute.gruppo match {
      case Some(group) =>
        val resultAssignGroup = assignGroupDriver(infoForAlgorithm,group,assigned)
        assignShifts(infoForAlgorithm,algorithmExecute.dateI,algorithmExecute.dateF,resultAssignGroup)
      case None =>assignShifts(infoForAlgorithm,algorithmExecute.dateI,algorithmExecute.dateF,assigned)
    }
  }
  //A POSTO
  private def assignGroupDriver(infoForAlgorithm: InfoForAlgorithm,listGroup:List[GruppoA],assigned:List[Info]): List[Info] ={
    @scala.annotation.tailrec
    def _assignGroupDriver(driverGroups:List[(Int,List[(Int,Persona)])], group:GruppoA,  dateGroup:Map[Date,Int], result:List[Info]=List.empty):List[Info]= driverGroups match {
      case ::(driverGroup, next) =>
        val resultAssignGroup= iteraDriver(driverGroup,group,assigned,dateGroup)
        _assignGroupDriver(next,group,resultAssignGroup._2,result:::resultAssignGroup._1)
      case Nil =>result
    }
    @scala.annotation.tailrec
    def _iteraGroup(listGroup:List[GruppoA], result:List[Info]=List.empty):List[Info]= listGroup match {
      case ::(group, next) =>
        val driverGroup=partitionAndUnion(infoForAlgorithm,infoForAlgorithm.persons).sortWith(_._1>_._1)
        val resultAssingGroup = _assignGroupDriver(driverGroup,group,group.date.map(date=>date->0).toMap)
         _iteraGroup(next,upsertListInfo(result,resultAssingGroup))
      case Nil =>result
    }
    upsertListInfo(assigned,_iteraGroup(listGroup))
  }

  private val conditionAssignGroup:(Date,Date)=>Boolean=(date,date1)=>
    !conditionGroup1And3(date,date1)

  private val conditionAssignGroup2:(Date,Date)=>Boolean=(date,date1)=>
    (subtract(date,1).compareTo(date1)==0
      || subtract(date,-1).compareTo(date1)==0  || subtract(date,2).compareTo(date1)!=0  || subtract(date,-2).compareTo(date1)!=0
      || date.compareTo(date1)==0)

  private val conditionAssignGroup3:(Date,Date)=>Boolean=(date,date1)=>
    conditionGroup1And3(date,date1)

  private val conditionGroup1And3:(Date,Date)=>Boolean=(date,date1)=>
    (subtract(date,1).compareTo(date1)==0
      || subtract(date,-1).compareTo(date1)==0  || subtract(date,2).compareTo(date1)==0  || subtract(date,-2).compareTo(date1)==0
      || date.compareTo(date1)==0)

  private val filterAndVerify:(Int,List[Info],Date)=>Boolean=(regola,assigned,groupDate)=> assigned.exists(res=>
      res.infoDay.map(_.data).partition(date => date.compareTo(startMonthDate(groupDate)) >= 0 &&
        date.compareTo(endOfMonth(groupDate)) <= 0)._1.forall(date => regola match {
        case FIRST_RULER=>conditionAssignGroup(groupDate,date)
        case SECOND_RULER=>conditionAssignGroup2(groupDate,date)
        case THIRD_RULER=>conditionAssignGroup3(groupDate,date)
      }))


  private def filterDayToAssignDriver(group:GruppoA,assigned:List[Info]):(Int,List[Date])={
    @scala.annotation.tailrec
    def _filterDayAssign(dateList:List[Date], result:List[Date]=List.empty):(Int,List[Date])= dateList match {
      case ::(date, next) if filterAndVerify(group.regola,assigned,date) => _filterDayAssign(next,result:+date)
      case ::(_, next)=> _filterDayAssign(next,result)
      case Nil if result.isEmpty =>(group.date.length,group.date)
      case Nil => (result.length,result)
    }
    _filterDayAssign(group.date)
  }
  // A POSTO
  private def iteraDriver(drivers:(Int,List[(Int,Persona)]),group:GruppoA,assigned:List[Info],dateGroup:Map[Date,Int]):(List[Info],Map[Date,Int])={

    val assignableDays:List[(Persona,(Int,List[Date]))]=drivers._2.map(res=>res._2->
      filterDayToAssignDriver(group,assigned.filter(driver=>res._2.matricola.equals(driver.idDriver)))).sortWith(_._2._1<_._2._1)
    @scala.annotation.tailrec
    def _iteraDriver(assignableDays:List[(Persona,List[Date])],dateGroup:Map[Date,Int], result:List[Info]=List.empty):(List[Info],Map[Date,Int]) = assignableDays match {
      case ::(assignableDay, next) =>
        val date = dateGroup.toList.sortWith(_._2 < _._2).find(date => assignableDay._2.exists(_.compareTo(date._1) == 0)) match {
                    case Some(value) => value._1
                  }
        val dateGroupUpdate =dateGroup.updated(date, dateGroup.getOrElse(date,0) + 1)
        _iteraDriver(next,dateGroupUpdate,result :+ Info(assignableDay._1.matricola, None, isFisso = false, 0, List(InfoDay(date, freeDay = true))))
      case Nil =>(result,dateGroup)
    }
   _iteraDriver(assignableDays.map(res=>res._1->res._2._2),dateGroup)
  }


  /***
  domingo1	   domingo2	   domingo3	   domingo4	  domingo5		       secuencias alternativas 1 (de 4 a 4 semanas)			    secuencias alternativas 2 (de 4 a 5 semanas)
  secuencia 1	         D	        D	        S	        S	       NULL		           secuencia1, secuencia 2, secuencia3			            secuencia 5, secuencia 6, secuencia 7
  secuencia 2	         D	        S	        S	        D	       NULL		           secuencia 2, secuencia 3, secuencia 4			        secuencia 6, secuencia 8, secuencia 9,secuencia 7
  secuencia 3	         S	        D	        D	        S	       NULL		           secuencia3, secuencia 2, secuencia 1, secuencia4			secuencia 7,secuencia 8,secuencia 6,secuencia 5
  secuencia 4	         S	        S	        D	        D	       NULL		           secuencia 4, secuencia 3			                        secuencia 8, secuencia 9,secuencia 7

  secuencias alternativas 3 (de 5 a 4 semanas)
  secuencia 5	         D	        D	        S	        S	        S		           secuencia 1, secuencia 2
  secuencia 6	         D	        S	        S	        S	        D		           secuencia 2, secuencia 3, secuencia 4
  secuencia 7	         S	        D	        D	        S	        S		           secuencia 1, secuencia 2, secuencia 3
  secuencia 8	         S	        S	        D	        D	        S		           secuencia 3, secuencia 4, secuencia 2, secuencia 1
  secuencia 9	         S	        S	        S	        D	        D		           secuencia 4, secuencia
   */

  private val sequenceCombinations: Map[(Int,Int,Int),List[Int]] = Map((1,4,4) -> List(1,2,3),(2,4,4) -> List(2,3,4),(3,4,4) -> List(3,2,1), (4,4,4) -> List(4,3),
    (0,4,4) -> List(1,2,3,4), (1,4,5) -> List(1,2,3),(2,4,5) -> List(2,4,5,3),(3,4,5) -> List(3,4,2,1), (4,4,5) -> List(4,5,3),
    (0,4,5) -> List(5,2,3,4,1), (1,5,4) -> List(1,2),  (2,5,4) -> List(2,3,4),  (3,5,4) -> List(1,2,3),   (4,5,4) -> List(3,4,2,1),
    (5,5,4) -> List(4,3),     (0,5,4) -> List(1,2,3,4))

  val sequences4: Map[Int,(Int,Int)] = Map(1 -> (1,2),2 -> (1,4), 3 -> (2,3), 4 -> (3,4))

  val sequences5: Map[Int,(Int,Int)] = Map(1 -> (1,2), 2 -> (1,5), 3-> (2,3), 4-> (3,4), 5-> (4,5))

  def selectSunday(value: List[Date], sequence: Int, sundays: Int): (Date,Date) = sundays match {
    case 4 => (value(sequences4(sequence)._1 -1 ),value(sequences4(sequence)._2 -1 ))
    case 5 => (value(sequences5(sequence)._1 -1 ),value(sequences5(sequence)._2 -1 ))
  }
 // A POSTO
  private def assignBalancedSundays(drivers: List[(Int, Persona)], sundays: List[Date], previousSundays: List[Date],contract: Option[List[Contratto]], previousSequence: Option[List[PreviousSequence]], assigned: Map[Int,Int]): (List[Info],Map[Int,Int],Option[List[PreviousSequence]]) = {

    @scala.annotation.tailrec
    def _assignBalancedSundays(drivers: List[(Int, Persona)], assigned: Map[Int,Int], result: List[Info] = List.empty,previousSequences: Option[List[PreviousSequence]]= None): (List[Info],Map[Int,Int],Option[List[PreviousSequence]]) = drivers match {
      case ::(driver, next) => previousSequence.toList.flatten.find(x => driver._2.matricola.contains(x.idDriver)) match {
        case Some(sequence) => sequenceCombinations.get(sequence.sequenza, previousSundays.length, sundays.length) match {
          case Some(combination) =>
            val sequenceToAssign = metodino(assigned,combination)
            val updatedAssigned  = assigned.updated(sequenceToAssign, assigned.getOrElse(sequenceToAssign,0) + 1)
            val dates = selectSunday(sundays.sortBy(_.getTime),sequenceToAssign,sundays.length)
            val fisso  = isFisso(contract,driver._1)
            val updatedPreviousSequence = updatePreviousSequence(previousSequences,driver._2.matricola,sequenceToAssign)
            val newInfo =result :+ Info(driver._2.matricola,driver._2.idTerminale,fisso,driver._1,
              List(InfoDay(dates._1,freeDay = true),InfoDay(dates._2,freeDay = true)))
            _assignBalancedSundays(next,updatedAssigned,newInfo,updatedPreviousSequence)
        }
      }
      case Nil =>(result,assigned,previousSequences)
    }
    _assignBalancedSundays(drivers,assigned,previousSequences = previousSequence)
  }

  private def updatePreviousSequence(previousSequences: Option[List[PreviousSequence]], idPerson:Option[Int],sequence: Int): Option[List[PreviousSequence]] ={
    previousSequences.map(t=>t.collect{
      case x if idPerson.contains(x.idDriver)=> x.copy(sequenza=sequence)
      case y=>y
    })
  }

  private def upsertInfoDay(infoDay: List[InfoDay], info: InfoDay): List[InfoDay] = {
    if(infoDay.exists(_.data.compareTo(info.data) == 0)){
      infoDay.map(x => if(x.data.compareTo(info.data) == 0) info else x)
    } else infoDay :+ info

  }

  private def upsertInfoDay(result: List[Info], resultNew: List[Info]): List[Info] = {
    @scala.annotation.tailrec
    def _upsertInfoDay(toIterate: List[InfoDay], newRes: List[InfoDay]): List[InfoDay] = toIterate match {
      case ::(head, next) =>
        _upsertInfoDay(next,upsertInfoDay(newRes,head))
      case Nil => newRes
    }

    val c = result.map(info => resultNew.filter(_.idDriver == info.idDriver).flatMap(_.infoDay) match {
      case x => info.copy(infoDay = _upsertInfoDay(x,info.infoDay))
      case Nil => info
    })

    c
  }

  private def upsertListInfo(result: List[Info], resultNew: List[Info]): List[Info] = {
    val newResult = resultNew.flatMap{
      case info if result.exists(_.idDriver.equals(info.idDriver)) =>result.filter(_.idDriver.equals(info.idDriver)).map(x=>{
        val infoNew =x.copy(infoDay = info.infoDay.flatMap{
          case s if x.infoDay.exists(_.data.compareTo(s.data)==0)=>
            x.infoDay.filter(_.data.compareTo(s.data)==0).map(_=>s)
          case s => List(s)
        })
        infoNew.copy(infoDay = infoNew.infoDay:::x.infoDay.filter(x=> !infoNew.infoDay.exists(_.data.compareTo(x.data)==0)))
      })
      case x => List(x)
    }
    newResult:::result.filter(xs=> !newResult.exists(_.idDriver.equals(xs.idDriver)))
  }
  //CAMBIARE NOME
  private def metodino(assignedSequences: Map[Int ,Int], possibiliSequence: List[Int]): Int = {
    @scala.annotation.tailrec
    def _metodino(assignedSequences: List[(Int,Int)]):Int = assignedSequences match {
      case ::(assigned, _) if possibiliSequence.contains(assigned._1) => assigned._1
      case ::(_, next) => _metodino(next)
    }
    _metodino(assignedSequences.toList.sortWith(_._2<_._2))
  }

 private def updateLastFree(infoForAlgorithm: InfoForAlgorithm,result:List[Info],date:Date):List[(Option[Int],Int)]={
   val sunday = getEndDayWeek(date)
   val saturday = subtract(sunday, -1)
   val update = result.filter(driver=> !FULL_AND_PART_TIME_5X2.contains(driver.tipoContratto))
     .map(x=>x.copy(infoDay=x.infoDay
       .filter(e=> (e.data.compareTo(saturday)==0 || e.data.compareTo(sunday)==0) && (e.absence || e.freeDay))))
     .filter(_.infoDay.nonEmpty)

   val sequence = infoForAlgorithm.previousSequence.toList.flatten.map(x => (x.idDriver, x.distanceFreeDay))
   sequence.map{
     case (i, _) if update.exists(_.idDriver.contains(i))=> update.foldLeft((Option(i),0)){
       case (default,actual) if actual.idDriver.equals(default._1) => (default._1,getDayNumber(actual.infoDay.last.data))
       case (default,_)=> default
     }
     case x => (Some(x._1),x._2)
   }

 }
  private def assignShifts(infoForAlgorithm: InfoForAlgorithm, dateI: Date, dateF: Date, result: List[Info]): Future[List[Info]] = Future{
    emitter.sendMessage("Assign Shift Fixed and Rotatory")
    val(fissi, rotatori) = infoForAlgorithm.persons.partition(person => infoForAlgorithm.allContract.toList.flatten.filter(_.turnoFisso)
      .flatMap(_.idContratto.toList).contains(person._1.contrattoId))

    val maronna = assignShiftForFissi(fissi,infoForAlgorithm.allRequest,dateI,dateF,result)
    val maronna2 = assignShiftForRotary(rotatori,maronna._1,dateI,dateF,maronna._2,infoForAlgorithm.allContract)
    val allSaturday= createListDayBetween(dateI,dateF).filter(isSaturday)
    val maronna2_5 =ruleThirdSaturday(allSaturday, maronna2._1,infoForAlgorithm.persons.map(_._2),maronna2._2)
    val lastFree= updateLastFree(infoForAlgorithm,maronna2_5._2,dateI)

    val maronna3=assignFreeDays(infoForAlgorithm.persons.filter(idPerson => !FULL_AND_PART_TIME_5X2.contains(idPerson._1.contrattoId)),maronna2_5._1, maronna2_5._2,createWeekLists(dateI,dateF), lastFree)

    val listWeek = createWeekLists(dateI,dateF)
    val maronna4 = assignExtraOrdinary(maronna3._2,maronna3._1,infoForAlgorithm.persons.map(_._2),listWeek)

    PrintListToExcel.printInfo(dateI,dateF,maronna4._1,maronna4._2)
    try {
    /*  RisultatoOperation.saveResultAlgorithm(maronna4._1).onComplete {
        case Failure(exception) => println(exception)
        case Success(value) =>println(value)
      }*/
    }catch {
      case e:Exception => println(e)
    }

    List()
  }

  def assignFreeDays(drivers: List[(StoricoContratto, Persona)], request: Option[List[InfoReq]], result: List[Info], weeks: List[List[Date]], lastFree: List[(Option[Int],Int)]):(Option[List[InfoReq]], List[Info]) = {
    emitter.sendMessage("Assegnando Giorni Liberi")
    @scala.annotation.tailrec
    def _assignFreeDays(request: Option[List[InfoReq]], weeks: List[List[Date]], lastFree: List[(Option[Int],Int)], resultNew: List[Info] = List.empty):(Option[List[InfoReq]], List[Info]) = weeks match {
      case ::(head,Nil) if head.length<7=>
        emitter.sendMessage("Assegnando Giorni Liberi Settimana"+ head.head)
        val secureLastFree = lastFree.partition(x=> getDayNumber(head.last)<x._2 && x._2!=0)
        val anotherDriver = secureLastFree._1.splitAt((secureLastFree._1.length/6)*head.length)
        val weekFreeDays = _assignFreeWeek(head, request,(secureLastFree._2:::anotherDriver._1).sortWith(_._2 > _._2))
        (weekFreeDays._1,upsertListInfo(resultNew,weekFreeDays._3))
      case ::(head, next) if head.length<7=>
        emitter.sendMessage("Assegnando Giorni Liberi Settimana"+ head.head)
        val secureLastFree = lastFree.partition(x=>getDayNumber(head.head)>x._2)
        val weekFreeDays = _assignFreeWeek(head, request,secureLastFree._2.sortWith(_._2 > _._2))
        _assignFreeDays(weekFreeDays._1,next, weekFreeDays._2:::secureLastFree._1, upsertListInfo(resultNew,weekFreeDays._3))
      case ::(head, next) =>
        emitter.sendMessage("Assegnando Giorni Liberi Settimana"+ head.head)
        val weekFreeDays = _assignFreeWeek(head, request,lastFree.sortWith(_._2 > _._2))
        _assignFreeDays(weekFreeDays._1,next, weekFreeDays._2, upsertListInfo(resultNew,weekFreeDays._3))
      case Nil => (request,resultNew)
    }

    @scala.annotation.tailrec
    def _assignFreeWeek(week: List[Date], request: Option[List[InfoReq]], lastFree: List[(Option[Int], Int)], resultNew: List[Info] = List.empty, lastNew: List[(Option[Int],Int)] = List.empty):(Option[List[InfoReq]],List[(Option[Int],Int)], List[Info]) = lastFree match {
      case ::(head, next) =>
        drivers.find(_._2.matricola.equals(head._1)) match {
          case Some(driver) =>if(head._2==0){
            week.foldLeft(false)((default,ress)=>{
              default || result.filter(res => driver._2.matricola.equals(res.idDriver)).exists(
                _.infoDay.count(x => x.data.compareTo(ress) == 0 && (x.freeDay || x.absence))>0)
            }) match {
              case true =>
                _assignFreeWeek(week,request,next,resultNew, lastNew:+ head)
              case false =>
                val possibleDays = result.filter(res => driver._2.matricola.equals(res.idDriver)).flatMap(
                _.infoDay.filter(x =>
                  week.exists(date => !isSunday(date) && date.compareTo(x.data) == 0)))
                possibleDays.find(x => getDayNumber(x.data) <=2 && (x.absence || x.freeDay)) match {
                  case Some(value) =>
                    _assignFreeWeek(week, request, next, resultNew, lastNew :+ (head._1, getDayNumber(value.data)))
                  case None =>
                    possibleDays.filter(res => getDayNumber(res.data) > 2).sortBy(_.data.getTime).findLast(date => date.absence || date.freeDay) match {
                      case Some(value) => _assignFreeWeek(week,request,next,resultNew, lastNew:+ (driver._2.matricola,getDayNumber(value.data))) //aggiornare lastFree se non ti trovo
                      case None =>
                        callAssign(driver,possibleDays,week,next,resultNew,lastNew)
                    }
                }

            }
          } else {

            val dayInWeek = result.filter(res => driver._2.matricola.equals(res.idDriver)).flatMap(
              _.infoDay.filter(x => week.exists(date => date.compareTo(x.data) == 0 && getDayNumber(x.data) <= head._2)))
            val possibleDays = dayInWeek.partition(x => !isSunday(x.data))
            possibleDays._2.find(date => date.absence || date.freeDay) match {
              case Some(value) =>
                val driverWeekFree = assignFreeDayInWeek(driver, possibleDays._1.filter(x => getDayNumber(x.data) < 5).map(_.data), request,result)
                _assignFreeWeek(week, driverWeekFree._1, next, resultNew :+ driverWeekFree._3, lastNew :+ (driverWeekFree._2._1, getDayNumber(value.data)))
              case None =>
                head._2 match {
                  case 6 => possibleDays._1.find(x => getDayNumber(x.data) == 1 && (x.absence || x.freeDay)) match {
                    case Some(value) =>
                      _assignFreeWeek(week, request, next, resultNew, lastNew :+ (head._1, getDayNumber(value.data)))
                    case None =>
                      assignFreeDay(driver,possibleDays._1.filter(x => getDayNumber(x.data) > 1).sortBy(_.data.getTime),week,next,resultNew,lastNew,head)
                  }
                  case _ =>
                    assignFreeDay(driver,possibleDays._1.sortBy(_.data.getTime),week,next,resultNew,lastNew,head)
                }
            }
          }
          case None =>_assignFreeWeek(week,request,next,resultNew,lastNew)
        }
      case Nil =>(request,lastNew,resultNew)
    }

    def assignFreeDay(driver:(StoricoContratto,Persona),possibleDays:List[InfoDay],week:List[Date],next:List[(Option[Int],Int)],resultNew: List[Info] = List.empty, lastNew: List[(Option[Int],Int)] = List.empty, head:(Option[Int],Int)) = {
      possibleDays.sortBy(_.data.getTime).findLast(date => date.absence || date.freeDay) match {
        case Some(value) =>
          _assignFreeWeek(week, request, next, resultNew, lastNew :+ (head._1, getDayNumber(value.data))) //aggiornare lastFree se non ti trovo
        case None =>
         callAssign(driver,possibleDays,week,next,resultNew,lastNew)

      }
    }
    def callAssign(driver:(StoricoContratto,Persona),possibleDays:List[InfoDay],week:List[Date],next:List[(Option[Int],Int)],resultNew: List[Info] = List.empty, lastNew: List[(Option[Int],Int)] = List.empty): (Option[List[InfoReq]], List[(Option[Int], Int)], List[Info]) ={
      val driverWeekFree = assignFreeDayInWeek(driver,possibleDays.map(_.data),request,result)
      _assignFreeWeek(week,driverWeekFree._1,next,resultNew :+ driverWeekFree._3, lastNew :+ driverWeekFree._2)
    }
    val boh = _assignFreeDays(request,weeks,lastFree)
    (boh._1, upsertListInfo(result, boh._2))
  }

  private def assignFreeDayInWeek(driver: (StoricoContratto, Persona), possibleFree: List[Date], request: Option[List[InfoReq]],result:List[Info]):(Option[List[InfoReq]],(Option[Int],Int),Info) = {
    val meh = request.toList.flatten.filter(x => possibleFree.exists(_.compareTo(x.data) == 0))
      .map(x => (x.assigned - x.request,x.data,x.idShift) ->
        result.find(res => driver._2.matricola.equals(res.idDriver) && res.infoDay.
          exists(date => date.data.compareTo(x.data) == 0))).maxBy(_._1)

    val infoDay = meh._2.toList.foldLeft((request,(Option(0),0),List[InfoDay]())){
      case (x,y) =>
        val updateReq = deUpdateInfoReq(x._1,meh._1._2,meh._1._3)
        y.infoDay.head.shift2 match {
          case Some(_) =>(deUpdateInfoReq(updateReq,meh._1._2,meh._1._3),(y.idDriver,getDayNumber(meh._1._2)),x._3 :+ InfoDay(meh._1._2,freeDay = true))
          case None =>(updateReq,(y.idDriver,getDayNumber(meh._1._2)), x._3 :+ InfoDay(meh._1._2,freeDay = true))
        }
    }
    (infoDay._1,infoDay._2,Info(driver._2.matricola,None,isFisso = false,0,infoDay._3))
  }



  private def assignShiftAndUpdateRequest(person: Persona, assignedForTurn: Option[List[InfoReq]], dateI: Date, dateF: Date, turnoId: Int, result: List[Info], turnoId1: Option[Int] = None):(Option[List[InfoReq]], Info) = {
    val infoDay = createListDayBetween(dateI,dateF).filter(date => !result.filter(result => person.matricola.equals(result.idDriver))
      .flatMap(_.infoDay.map(_.data)).exists(_.compareTo(date) == 0)).foldLeft((assignedForTurn,List[InfoDay]()))((x,y) => {
      val updateMap = updateInfoReq(x._1,y,turnoId)
      turnoId1 match {
        case Some(value) => (updateInfoReq(updateMap,y,value),x._2 :+ InfoDay(y,Some(turnoId),turnoId1))
        case None => (updateMap,x._2 :+ InfoDay(y,Some(turnoId),turnoId1))
      }
    })
    (infoDay._1,Info(person.matricola,None,isFisso = false,0,infoDay._2))
  }

  //CONTROLAR ALLREQUEST Y SU ACTUALIZACION
  def assignShiftForFissi(fissi: List[(StoricoContratto, Persona)], assignedForTurn: Option[List[InfoReq]],dateI: Date, dateF: Date, result: List[Info]): (Option[List[InfoReq]], List[Info]) = {

    @scala.annotation.tailrec
    def _assignShiftForFissi(fissi: List[(StoricoContratto, Persona)], assignedForTurn: Option[List[InfoReq]], resultNew: List[Info] = List.empty):(Option[List[InfoReq]], List[Info]) = fissi match {
      case ::(fisso, next) =>
        val assignmentResult = assignShiftAndUpdateRequest(fisso._2,assignedForTurn,dateI,dateF,fisso._1.turnoId.head,result,fisso._1.turnoId1)
        _assignShiftForFissi(next, assignmentResult._1, resultNew :+ assignmentResult._2)
      case Nil =>(assignedForTurn,resultNew)
    }
    val callResult = _assignShiftForFissi(fissi,assignedForTurn)
    (callResult._1, upsertListInfo(result,callResult._2))
  }

  def assignShiftForRotary(rotatori: List[(StoricoContratto, Persona)], allRequest: Option[List[InfoReq]], dateI: Date, dateF: Date, result: List[Info],contracts: Option[List[Contratto]]): (Option[List[InfoReq]],List[Info]) = {

    @scala.annotation.tailrec
    def _assignShiftForRotatory(rotatori: List[(StoricoContratto, Persona)], allRequest: Option[List[InfoReq]], weeks: List[List[Date]], resultNew: List[Info] = List.empty): (Option[List[InfoReq]],List[Info]) = weeks match {
      case ::(head, next) =>
        val weekAssignment = _assignWeekForRotatory(rotatori,allRequest,head)
        _assignShiftForRotatory(rotatori.reverse,weekAssignment._1, next, upsertListInfo(resultNew,weekAssignment._2))
      case Nil => (allRequest,resultNew)
    }

    @scala.annotation.tailrec
    def _assignWeekForRotatory(drivers: List[(StoricoContratto, Persona)], allRequest:Option[List[InfoReq]], week: List[Date], resultNew: List[Info] = List.empty):(Option[List[InfoReq]], List[Info]) = drivers match {
      case ::(head, next) =>
        val shift = shiftWithMoreRequest(week,allRequest)
        if(isPartTime(contracts,head._1.contrattoId)){
          val assignedWeek = assignShiftAndUpdateRequest(head._2, allRequest,week.head,week.last,shift,result)
          _assignWeekForRotatory(next, assignedWeek._1, week,resultNew :+assignedWeek._2)
        }else {
          shift match {
            case x if x==6 =>
              val assignedWeek = assignShiftAndUpdateRequest(head._2, allRequest,week.head,week.last,shift-1,result,Some(shift))
              _assignWeekForRotatory(next, assignedWeek._1, week,resultNew :+assignedWeek._2)
            case _ =>
              val shift2 = Some(shift+1)
              val assignedWeek = assignShiftAndUpdateRequest(head._2, allRequest,week.head,week.last,shift,result,shift2)
              _assignWeekForRotatory(next, assignedWeek._1, week,resultNew :+assignedWeek._2)
          }
        }

      case Nil => (allRequest, resultNew)
    }

    val callresult = _assignShiftForRotatory(rotatori,allRequest,createWeekLists(dateI,dateF))
    (callresult._1, upsertListInfo(result,callresult._2))
  }

  //decidere come fare stammerda
  private def shiftWithMoreRequest(week: List[Date], allRequest: Option[List[InfoReq]]): Int =
    allRequest.map(_.filter(x => week.contains(x.data)).map(res => res.idShift -> (res.request - res.assigned)).groupBy(_._1)
      .map(res => res._1 -> res._2.map(_._2)).toList.map(x => x._1 -> x._2
      .foldLeft((0.0, 1))((acc, i) => (acc._1 + (i - acc._1) / acc._2, acc._2 + 1))._1).maxBy(_._2)._1) match {
      case Some(value) => value
    }

   private def createWeekLists(dateI: Date, dateF: Date): List[List[Date]] = {
    @scala.annotation.tailrec
    def _createWeekLists(date: Date, result: List[List[Date]] = List.empty): List[List[Date]] = {
      if (nextWeek(date).compareTo(dateF) > 0)
        result:+ createListDayBetween(date, dateF)
      else
        _createWeekLists(getFirstDayWeek(nextWeek(date)), result:+ createListDayBetween(date, getEndDayWeek(date)))
    }

    _createWeekLists(dateI)
  }


  private def deUpdateInfoReq(infoReq: Option[List[InfoReq]], date: Date, shift: Int): Option[List[InfoReq]] = {
    infoReq.map(_.collect{
      case x if x.data.compareTo(date) == 0 && x.idShift == shift => x.copy(assigned = x.assigned -1)
      case x => x
    })
  }

  private def updateInfoReq(infoReq: Option[List[InfoReq]], date: Date, shift: Int): Option[List[InfoReq]] = {
    infoReq.map(_.collect{
      case x if x.data.compareTo(date) == 0 && x.idShift == shift => x.copy(assigned = x.assigned +1)
      case x => x
    })
  }



  private def assignExtraOrdinary(result:List[Info], allRequest:Option[List[InfoReq]],persons:List[Persona], listWeek:List[List[Date]]):(List[Info],Option[List[InfoReq]])={

    @scala.annotation.tailrec
    def _assignExtraOrdinary(listWeek:List[List[Date]], resultNew:List[Info]=List.empty, allRequest:Option[List[InfoReq]]):(List[Info],Option[List[InfoReq]])= listWeek match {
      case ::(week, next) =>
        val called = searchShiftWithMoreRequest(week,allRequest,persons,result.map(x=>x.copy(infoDay = x.infoDay.filter(res=>week.exists(_.compareTo(res.data)==0)))))
        _assignExtraOrdinary(next,upsertListInfo(resultNew,called._1),Some(called._2))
      case Nil =>(resultNew,allRequest)
    }
    _assignExtraOrdinary(listWeek,allRequest=allRequest)
  }

  def searchShiftWithMoreRequest(week: List[Date], allRequest:Option[List[InfoReq]],persons:List[Persona],resultWeek:List[Info]):(List[Info],List[InfoReq]) = {
    val newInfoReq: List[InfoReq] = week.flatMap(resp=> allRequest.toList.flatten.filter(x=> (x.request - x.assigned) >0 && x.data.compareTo(resp)==0))
    val mapPerson: mutable.Map[Int, Int] = persons.flatMap(x=>x.matricola.map(x=>x->0)).to(mutable.Map)
    week.headOption.foreach(data=>{
      emitter.sendMessage("Assegnando Straordinario in Settimana"+data)
    })
    @scala.annotation.tailrec
    def _searchShiftWithMoreRequest(infoReq:List[InfoReq], result:List[Info], resultInfoReq:List[InfoReq]=List.empty):(List[Info],List[InfoReq]) = infoReq match {
      case s::next =>
        val x = s::next
        val req = x.maxBy(x=>x.request - x.assigned)
        val driverExtra = result.map(x=>x.copy(infoDay= x.infoDay.filter(res=> (getDayNumber(res.data) match {
          case x if x==0 => 7==req.idDay
          case x => x==req.idDay
        })&& res.straordinario.isEmpty  && !res.absence && !res.freeDay
          && ( res.shift.contains(req.idShift+1) || (res.shift2 match {
            case Some(value) => Some(value).contains(req.idShift-1)
            case None => res.shift.contains(req.idShift-1)
          }))))).filter(x=> x.infoDay.nonEmpty && x.idDriver.exists(id=>mapPerson.filter(_._2<2).toList.map(_._1).contains(id)))

        val newInfoReq = x.filter(x=> !(x.idShift==req.idShift && x.data.compareTo(req.data)==0 && x.idDay==req.idDay))
        if(driverExtra.isEmpty){
          _searchShiftWithMoreRequest(newInfoReq,result,resultInfoReq:+req)
        }else{
          val driver = driverExtra(Random.nextInt(driverExtra.length))
          val assinged = assignExtra(driver,result,req,infoReq)
          driver.idDriver.foreach(id=>mapPerson.get(id).foreach(x=> mapPerson.update(id,x+1)))
          if(assinged._2.exists(res=>res.data.compareTo(req.data)==0 && res.idShift==req.idShift && res.assigned==res.request)){
            _searchShiftWithMoreRequest(newInfoReq,assinged._1,resultInfoReq:::assinged._2.filter(res=> res.data.compareTo(req.data)==0 && res.idShift==req.idShift))
          }else{
            _searchShiftWithMoreRequest(assinged._2,assinged._1,resultInfoReq)
          }

        }
      case Nil =>(result,resultInfoReq:::infoReq)
    }
    val resultAssign = _searchShiftWithMoreRequest(newInfoReq,resultWeek)
    (resultAssign._1,allRequest.toList.flatten(_.map{
      case x if resultAssign._2.exists(res => res.data.compareTo(x.data)==0 && res.idShift==x.idShift)=>
        x.copy(assigned = resultAssign._2.filter(res => res.data.compareTo(x.data)==0 && res.idShift==x.idShift).foldLeft(0)((default,inf)=>default+inf.assigned))
      case x => x
    }))
  }

  private def assignExtra(driver:Info,result:List[Info],infoReq: InfoReq, listInfoReq:List[InfoReq]):(List[Info],List[InfoReq]) ={
    (upsertListInfo(result,List(driver.copy(infoDay = driver.infoDay.map{
      case x if x.data.compareTo(infoReq.data)==0 =>InfoDay(x.data,x.shift,x.shift2,Some(infoReq.idShift))
      case x => x
    }))),updateInfoReq(Some(listInfoReq),infoReq.data,infoReq.idShift).toList.flatten)
  }

  private def ruleThirdSaturday(week: List[Date], allRequest:Option[List[InfoReq]],drivers:List[Persona],result:List[Info]):(Option[List[InfoReq]],List[Info])= {
    emitter.sendMessage("Assegnando Terzo Sabato")
      @scala.annotation.tailrec
      def _iterateDrivers(driver:List[Persona], allRequest:List[InfoReq], result:List[Info]):(Option[List[InfoReq]],List[Info])= driver match {
        case ::(head, next) =>
          val driverResult = result.filter(x=>head.matricola.equals(x.idDriver))
          val finalResult = _searchSaturday(week,driverResult,allRequest)
          _iterateDrivers(next,finalResult._1,upsertListInfo(result,finalResult._2))
        case Nil =>(Some(allRequest),result)
      }

    @scala.annotation.tailrec
    def _searchSaturday(saturdays:List[Date], info:List[Info], request:List[InfoReq], sundayWork:List[Date]=List.empty):(List[InfoReq],List[Info])= saturdays match {
      case ::(head, next) if info.exists(res=> res.infoDay.exists(x => x.data.compareTo(head)==0 && !x.freeDay && !x.absence)
        && res.infoDay.exists(x => x.data.compareTo(subtract(head,1))==0 && !x.freeDay && !x.absence))=>
        val finalSunday = head+:sundayWork
        if(finalSunday.length<3)
          _searchSaturday(next,info,request,finalSunday)
        else{
          val day = finalSunday(Random.nextInt(finalSunday.length))
          val otherDay = finalSunday.filter(days=>days.compareTo(day)>0)
          val newInfo =info.map(x=> x.copy(infoDay = x.infoDay.map{
            case y if y.data.compareTo(day)==0 =>
              InfoDay(day,freeDay = true)
            case y => y
          }))
          val ww = info.flatMap(_.infoDay.filter(_.data.compareTo(day)==0)).map(r=>{
            val w = deUpdateInfoReq(Some(request),day,r.shift.head)
            val e= r.shift2 match {
              case Some(value) =>  deUpdateInfoReq(w,day,value)
              case None =>w
            }
            e
          })
          _searchSaturday((next:::otherDay).sortBy(_.getTime),newInfo,ww.flatMap(_.toList).flatten)
        }
      case _::next => _searchSaturday(next,info,request)
      case Nil =>(request,info)
    }

    allRequest match {
      case Some(allRequest) =>_iterateDrivers(drivers,allRequest,result)
    }
  }

}

