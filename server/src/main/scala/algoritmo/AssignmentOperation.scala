package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation.InfoForAlgorithm
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA}
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
trait AssignmentOperation{
  def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]):Unit
}
object AssignmentOperation extends AssignmentOperation {
  //TODO DETTO DA GIANNI! assegnare prima i liberi ai 5x2 sabato e domenica libero
  //TODO assegnare domeniche 6x1 indipendente sia fisso o rotatorio
  //TODO se ci sono assegniamo i gruppi per ogni gruppo assegna tutti un libero controllare in che data serve di piu
  //TODO che lavorare la persona prima di assegnare il libero del gruppo
  //TODO assegnare tutti turni prima fissi dopo rotatorio, verificare InfoReq quando si sta assegnando
  //TODO verificare se regola tre sabato e attiva, se e cosi e il conducente ha 3 sabati di seguito lavorando,
  //TODO il 3 e libero, se il 3 e insieme a una domenica libera allora lo avrà il 2 e cosi via via
  //TODO assegnare i liberi ai 6x1 fissi, controllare quantità di giorni senza libero. in piu guardare se ce una settimana normale o speciale
  //TODO due liberi non possono essere insieme, due liberi devono avere una distanza minima di 2 giorni
  //TODO assegnare liberi ai 6x1 rotatorio, controllare InfoReq per vedere quanti possono essere liberi in quel turno in quella data

  //TODO per segnare le domenica, potrebbero fare una case class che rapresente le sequenze->List contenuto interno da decidere

  private val FULL_AND_PART_TIME_5X2 = List(1, 2, 3, 4)
  private val FULL_TIME_6X1 = ""
  private val PART_TIME_6X1 = ""
  private val IS_FISSO = true
  private val FREE_DAY = 10
  private val ABSENCE = 11

  final case class InfoForAlgorithm(shift: List[Turno], theoricalRequest: List[RichiestaTeorica],
                                    persons: List[(StoricoContratto, Persona)], allContract: Option[List[Contratto]] = None, absence: Option[List[(Int, Date, Date)]] = None,
                                    allRequest: Option[List[InfoReq]] = None, previousSequence: Option[List[PreviousSequence]] = None)

  final case class InfoReq(idShift: Int, request: Int, assigned: Int, idDay: Int, data: Date, idTerminal: Int)

  final case class PreviousSequence(idDriver: Int, sequenza: Int, distanceFreeDay: Int)
  
  final case class InfoDay(data: Date, shift: Option[Int] = None, shift2: Option[Int] = None, straordinario: Option[Int] = None, freeDay: Boolean = false, absence: Boolean = false)

  final case class Info(idDriver: Int, idTerminal: Int, isFisso: Boolean, tipoContratto: Int, infoDay: List[InfoDay])

  private val emitter = ConfigEmitter()

  def apply(): AssignmentOperation = {
    emitter.start()
    this
  }

  override def initOperationAssignment(algorithmExecute: AlgorithmExecute, infoForAlgorithm: Future[InfoForAlgorithm]): Unit = {
    emitter.sendMessage("Iniziando processo di assegnazione")
    infoForAlgorithm.foreach(info => {
      info.theoricalRequest.map(terminal => {
        val personaFilter = info.persons.filter(_._2.idTerminale.contains(terminal.terminaleId))
        info.copy(persons = personaFilter,
          allRequest = info.allRequest.map(_.filter(_.idTerminal == terminal.terminaleId)),
          theoricalRequest = info.theoricalRequest.filter(_.terminaleId == terminal.terminaleId),
          previousSequence = info.previousSequence.map(_.filter(value => personaFilter.map(_._2.matricola).exists(id => id.contains(value.idDriver)))))
      }).foreach(info => startAlgorithm(info, algorithmExecute))
    })
    //IN QUESTO PUNTO SEPARIAMO PER TERMINALE (?-> questo per la STUPIDAGGINE DI GIANNI)
  }

  private def startAlgorithm(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute): Unit = Future {
    val (driver5x2, driver6x1) = infoForAlgorithm.persons.partition(idPerson => FULL_AND_PART_TIME_5X2.contains(idPerson._1.contrattoId))
    infoForAlgorithm.absence match {
      case Some(value) =>
        emitter.sendMessage("Assign absence")
        assignAbsence(driver5x2.map(v => (v._1.contrattoId,v._2)),infoForAlgorithm.allContract,value)
        .zip(assignAbsence(driver6x1.map(v => (v._1.contrattoId,v._2)),infoForAlgorithm.allContract,value))
        .map{
          case (listInfo5x2, listInfo6x1) => continueAlgorithm(infoForAlgorithm,algorithmExecute,driver5x2,driver6x1,listInfo5x2,listInfo6x1)
        }
      case None => continueAlgorithm(infoForAlgorithm,algorithmExecute,driver5x2,driver6x1)
    }
    //andare avanti dopo la ZIPPPP
  }

  def continueAlgorithm(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute, driver5x2: List[(StoricoContratto, Persona)], driver6x1: List[(StoricoContratto, Persona)], listInfo5x2: List[Info] = List.empty, listInfo6x1: List[Info] = List.empty): Unit = {
    emitter.sendMessage("Assign Saturday and Sunday 5x2 and Sunday 6x1")
    assignSaturdayAndSunday5x2(infoForAlgorithm.allContract, algorithmExecute, driver5x2.map(v => (v._1.contrattoId,v._2)),listInfo5x2)
      .zip(assignSunday6x1(infoForAlgorithm, algorithmExecute, driver6x1,listInfo6x1)).map{
      case (driver5x2, driver6x1) =>
        groupForDriver(infoForAlgorithm,algorithmExecute,driver5x2:::driver6x1)
    }
  }

  private def assignAbsence(drivers: List[(Int,Persona)],contracts: Option[List[Contratto]],absence: List[(Int, Date,Date)]): Future[List[Info]] = Future{

    @scala.annotation.tailrec
    def _absignAbsence(drivers: List[(Int, Persona)], result: List[Info] = List.empty): List[Info] = drivers match {
      case ::(head, next) if absence.exists(x => head._2.matricola.contains(x._1)) => _absignAbsence(next, result :+ Info(head._2.matricola.head,
        head._2.idTerminale.head,isFisso(contracts, head._1), head._1, _iterateDate(head._2.matricola.head)))
      case ::(_,next) => _absignAbsence(next,result)
      case Nil => result
    }

    def _iterateDate(driverId: Int): List[InfoDay] = {
      absence.filter(_._1 == driverId).map(absence => createListDayBetween(absence._2, absence._3))
        .flatMap(res => res.map(date => InfoDay(date, absence =true)))
    }

    _absignAbsence(drivers)
  }

  private def assignSaturdayAndSunday5x2(allContract: Option[List[Contratto]], algorithmExecute: AlgorithmExecute, driver: List[(Int, Persona)], result: List[Info]): Future[List[Info]] = Future{
    @scala.annotation.tailrec
    def _assignSaturdayAndSunday(person: List[(Int, Persona)], date: Date, info: List[Info] = List.empty): List[Info] =  person match {
      case ::(head, next) => _assignSaturdayAndSunday(next, date, upsertListInfo(info, List(Info(head._2.matricola.head, head._2.idTerminale.head,
        isFisso =  isFisso(allContract,head._1), head._1, _iterateDate(date)))))
      case Nil => info
    }

    @scala.annotation.tailrec
    def _iterateDate(date: Date, infoDay: List[InfoDay] = List.empty): List[InfoDay] = date match {
      case date if date.compareTo(algorithmExecute.dateF) < 0 && getEndDayWeek(date).compareTo(algorithmExecute.dateF) < 0 =>
        _iterateDate(subtract(getEndDayWeek(date), 1),
          infoDay ::: List(InfoDay(getEndDayWeek(date), freeDay = true), InfoDay(subtract(getEndDayWeek(date), -1), freeDay = true)))
      case _ => infoDay.sortBy(_.data)
    }

    _assignSaturdayAndSunday(driver, algorithmExecute.dateI,result)

  }

  private val isFisso: (Option[List[Contratto]], Int) => Boolean = (allContract,idContratto) => allContract.map(_.filter(_.idContratto.contains(idContratto)).map(_.turnoFisso)).toList.flatten match {
    case List(isFisso) => isFisso
  }

  private val isPartTime: (Option[List[Contratto]], Int) => Boolean = (allContract,idContratto) => allContract.map(_.filter(_.idContratto.contains(idContratto)).map(_.partTime)).toList.flatten match {
    case List(isPartTime) => isPartTime
  }

  private def allSundayMonth(sunday:Date,finalDayMont:Date):List[Date]={
    @scala.annotation.tailrec
    def _allSunday(sunday:Date,allSunday:List[Date]=List.empty): List[Date] = sunday match {
      case date if date.compareTo(finalDayMont) < 0 =>_allSunday(subtract(date, 7), allSunday :+ date)
      case date if date.compareTo(finalDayMont) == 0 => allSunday :+ date
      case date if date.compareTo(finalDayMont) > 0 => allSunday
    }
    _allSunday(sunday)
  }

  private def assignSunday6x1(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute, driver: List[(StoricoContratto, Persona)], result: List[Info]): Future[List[Info]] = Future{

    @scala.annotation.tailrec
    def iterateMap(map: List[(Int,List[(Int,Persona)])],date: Date,previousSequence: Option[List[PreviousSequence]], assigned: Map[Int,Int] = Map(1 ->0, 2 -> 0, 3 -> 0, 4 -> 0, 5-> 0),result: List[Info] = List.empty): (List[Info], Option[List[PreviousSequence]]) = map match {
      case ::(head, next) =>
        val previousSundays = allSundayMonth(getEndDayWeek(previousMonthDate(date)),endOfMonth(previousMonthDate(date)))
        val sundays = allSundayMonth(getEndDayWeek(date),endOfMonth(date))
        val assignement = assignBalancedSundays(head._2,sundays,previousSundays,infoForAlgorithm.allContract.head,previousSequence,assigned)
        iterateMap(next,date,assignement._3,assignement._2, upsertListInfo(result, assignement._1))
      case Nil => (result, previousSequence)
    }

    @scala.annotation.tailrec
    def iterateDate(dataI: Date, map: List[(Int,List[(Int,Persona)])], previousSequence: Option[List[PreviousSequence]], result: List[Info] = List.empty):List[Info] = dataI match{
      case x if x.compareTo(startMonthDate(algorithmExecute.dateF)) == 0 => iterateMap(map,x, previousSequence = previousSequence,result = result)._1
      case date =>
        val resultIterateMap = iterateMap(map,date, previousSequence, result = result)
        iterateDate(nextMonthDate(date),map, resultIterateMap._2, resultIterateMap._1)
    }
    iterateDate(algorithmExecute.dateI,partitionAndUnion(infoForAlgorithm,driver),infoForAlgorithm.previousSequence, result)
   }

  private def partitionAndUnion(infoForAlgorithm: InfoForAlgorithm,driver: List[(StoricoContratto, Persona)])={
    val (fisso,rotatorio) = driver.groupBy(_._1.contrattoId).toList.partition(t => infoForAlgorithm.allContract.toList.flatten.exists(tr => tr.idContratto.contains(t._1) && tr.turnoFisso))
    val(part,full) = fisso.partition(t => infoForAlgorithm.allContract.toList.flatten.exists(tr => tr.idContratto.contains(t._1) && tr.partTime))
    rotatorio.map(x1 => x1._1 -> x1._2.map(x => (x._1.contrattoId,x._2))):::divideYConquista(part):::divideYConquista(full)
  }

  private def divideYConquista(part: List[(Int, List[(StoricoContratto, Persona)])]):List[(Int, List[(Int, Persona)])] = {
    part.flatMap(_._2.groupBy(_._1.turnoId).toList).map(x=>x._1.head->x._2.map(x=>(x._1.contrattoId,x._2)))
  }


  private def groupForDriver(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute,assigned:List[Info]): Unit ={
    emitter.sendMessage("Assign Group for Driver")
    algorithmExecute.gruppo match {
      case Some(group) => assignShifts(infoForAlgorithm,algorithmExecute.dateI,algorithmExecute.dateF,assignGroupDriver(infoForAlgorithm,group,assigned))
      case None =>assignShifts(infoForAlgorithm,algorithmExecute.dateI,algorithmExecute.dateF,assigned)
    }
  }

  private def assignGroupDriver(infoForAlgorithm: InfoForAlgorithm,listGroup:List[GruppoA],assigned:List[Info]): List[Info] ={
    @scala.annotation.tailrec
    def _assignGroupDriver(groupDriver:List[(Int,List[(Int,Persona)])], listGroup:GruppoA,  map:Map[Date,Int], result:List[Info]=List.empty):List[Info]= groupDriver match {
      case ::(head, next) =>
        val response= iteraDate(head,listGroup,assigned,map)
        _assignGroupDriver(next,listGroup,response._2,result:::response._1)
      case Nil =>result
    }
    @scala.annotation.tailrec
    def _iteraGroup(listGroup:List[GruppoA], result:List[Info]=List.empty):List[Info]= listGroup match {
      case ::(head, next) =>
         _iteraGroup(next,result:::_assignGroupDriver(partitionAndUnion(infoForAlgorithm,infoForAlgorithm.persons).sortWith(_._1>_._1),head,listGroup.flatMap(_.date).map(date=>date->0).toMap))
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
        case 1=>conditionAssignGroup(groupDate,date)
        case 2=>conditionAssignGroup2(groupDate,date)
        case 3=>conditionAssignGroup3(groupDate,date)
      }))


  private def filterDayAssign(listGroups:GruppoA,assigned:List[Info]):(Int,List[Date])={
    @scala.annotation.tailrec
    def _filterDayAssign(listGroup:List[Date], result:List[Date]=List.empty):(Int,List[Date])= listGroup match {
      case ::(head, next) if filterAndVerify(listGroups.regola,assigned,head) => _filterDayAssign(next,result:+head)
      case ::(_, next)=> _filterDayAssign(next,result)
      case Nil if result.isEmpty =>(listGroups.date.length,listGroups.date)
      case Nil => (result.length,result)
    }
    _filterDayAssign(listGroups.date)
  }

  private def iteraDate(drivers:(Int,List[(Int,Persona)]),listGroup:GruppoA,assigned:List[Info],map:Map[Date,Int]):(List[Info],Map[Date,Int])={

    val probability:List[(Persona,(Int,List[Date]))]=drivers._2.map(res=>res._2->filterDayAssign(listGroup,assigned.filter(driver=>res._2.matricola.contains(driver.idDriver)))).sortWith(_._2._1<_._2._1)
    @scala.annotation.tailrec
    def _iteraDriver(probabilit:List[(Persona,List[Date])],map:Map[Date,Int], result:List[Info]=List.empty):(List[Info],Map[Date,Int]) = probabilit match {
      case ::(head, next) => val date = map.toList.sortWith(_._2 < _._2).find(date => head._2.exists(_.compareTo(date._1) == 0)) match {
        case Some(value) => value._1
        }
        val x =map.updated(date, map.getOrElse(date,0) + 1)
        _iteraDriver(next,x,result :+ Info(head._1.matricola.head, 0, isFisso = false, 0, List(InfoDay(date, freeDay = true))))
      case Nil =>(result,map)
    }
   _iteraDriver(probability.map(res=>res._1->res._2._2).sortBy(r=>r._1.matricola),map)
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
    (0,4,5) -> List(5,2,3,4),(1,5,4) -> List(1,2),(2,5,4) -> List(2,3,4),(3,5,4) -> List(1,2,3), (4,5,4) -> List(3,4,2,1),
    (5,5,4) -> List(4,3),(0,5,4) -> List(1,2,3,4))

  private val sequences4: Map[Int,(Int,Int)] = Map(1 -> (1,2),2 -> (1,4), 3 -> (2,3), 4 -> (3,4))

  private val sequences5: Map[Int,(Int,Int)] = Map(1 -> (1,2), 2 -> (1,5), 3-> (2,3), 4-> (3,4), 5-> (4,5))

  def assignSunday(value: List[Date], sequence: Int, sundays: Int): (Date,Date) = sundays match {
    case 4 => (value(sequences4(sequence)._1 -1 ),value(sequences4(sequence)._2 -1 ))
    case 5 => (value(sequences5(sequence)._1 -1 ),value(sequences5(sequence)._2 -1 ))
  }

  private def assignBalancedSundays(drivers: List[(Int, Persona)], sundays: List[Date], previousSundays: List[Date],contract: List[Contratto], previousSequence: Option[List[PreviousSequence]], assigned: Map[Int,Int]): (List[Info],Map[Int,Int],Option[List[PreviousSequence]]) = {

    @scala.annotation.tailrec
    def _assignBalancedSundays(drivers: List[(Int, Persona)], assigned: Map[Int,Int], result: List[Info] = List.empty,previousSequences: Option[List[PreviousSequence]]= None): (List[Info],Map[Int,Int],Option[List[PreviousSequence]]) = drivers match {
      case ::(head, next) => previousSequence.toList.flatten.find(x => head._2.matricola.contains(x.idDriver)) match {
        case Some(value) => sequenceCombinations.get(value.sequenza, previousSundays.length, sundays.length) match {
          case Some(value2) =>
            val sequence = metodino(assigned,value2)
            val x  = assigned.updated(sequence, assigned.getOrElse(sequence,0) + 1)
            val dates = assignSunday(sundays.sortBy(_.getTime),sequence,sundays.length)
            val fisso: Boolean = isFisso(Some(contract),head._1)
            val valore = updatePreviousSequence(previousSequences,head._2.matricola.head,sequence)
            _assignBalancedSundays(next,x,result ::: List(Info(head._2.matricola.head,head._2.idTerminale.head,fisso,head._1,List(InfoDay(dates._1,freeDay = true),InfoDay(dates._2,freeDay = true))))
              ,valore)
        }
      }
      case Nil =>(result,assigned,previousSequences)
    }
    _assignBalancedSundays(drivers,assigned,previousSequences = previousSequence)
  }
  private def updatePreviousSequence(previousSequences: Option[List[PreviousSequence]], idPerson:Int,sequence: Int): Option[List[PreviousSequence]] ={
    previousSequences.map(t=>t.collect{
      case x if x.idDriver==idPerson=> x.copy(sequenza=sequence)
      case y=>y
    })
  }

  private def upsertListInfo(result: List[Info], resultNew: List[Info]): List[Info] = {
    @scala.annotation.tailrec
    def _upsertListInfo(result: List[Info], resultNew: List[Info]): List[Info] = resultNew match {
      case ::(head, next) => _upsertListInfo(result.find(_.idDriver == head.idDriver) match {
          case Some(value) => result.updated(result.indexOf(value),value.copy(infoDay = value.infoDay ::: head.infoDay))
          case None =>  head :: result
        },next)
      case Nil => result
    }
    _upsertListInfo(result,resultNew)
  }

  private def metodino(assigned: Map[Int ,Int], possibili: List[Int]): Int = {
    @scala.annotation.tailrec
    def _metodino(assigned: List[(Int,Int)]):Int = assigned match {
      case ::(head, _) if possibili.contains(head._1) => head._1
      case ::(_, next) => _metodino(next)
//      case _ => possibili.head
    }

    _metodino(assigned.toList.sortWith(_._2<_._2))
  }

  private def assignShifts(infoForAlgorithm: InfoForAlgorithm, dateI: Date, dateF: Date, result: List[Info]): Future[List[Info]] = Future{
    emitter.sendMessage("Assign Shift Fixed and Rotatory")
    val(fissi, rotatori) = infoForAlgorithm.persons.partition(person => infoForAlgorithm.allContract.toList.flatten.filter(_.turnoFisso).flatMap(_.idContratto.toList).contains(person._1.contrattoId))
    val maronna = assignShiftForFissi(fissi,infoForAlgorithm.allRequest,dateI,dateF,result)
    val maronna2 = assignShiftForRotary(rotatori,maronna._1,dateI,dateF,maronna._2,infoForAlgorithm.allContract)
    PrintListToExcel.printInfo(dateI,dateF,maronna2._2,maronna2._1)
    List()
  }
  //CONTROLAR ALLREQUEST Y SU ACTUALIZACION
  def assignShiftForFissi(fissi: List[(StoricoContratto, Persona)], assignedForTurn: Option[List[InfoReq]],dateI: Date, dateF: Date, result: List[Info]): (Option[List[InfoReq]], List[Info]) = {

    @scala.annotation.tailrec
    def _assignShiftForFissi(fissi: List[(StoricoContratto, Persona)], assignedForTurn: Option[List[InfoReq]], resultNew: List[Info] = List.empty):(Option[List[InfoReq]], List[Info]) = fissi match {
      case ::(head, next) =>
        val assignmentResult = assignShiftAndUpdateRequest(head._2,assignedForTurn,dateI,dateF,head._1.turnoId.head,result,head._1.turnoId1)
        _assignShiftForFissi(next, assignmentResult._1, resultNew :+ assignmentResult._2)
      case Nil =>(assignedForTurn,resultNew)
    }

    val callresult = _assignShiftForFissi(fissi,assignedForTurn)
    (callresult._1, upsertListInfo(result,callresult._2))
  }

  private def assignShiftAndUpdateRequest(person: Persona,assignedForTurn: Option[List[InfoReq]], dateI: Date, dateF: Date, turnoId: Int, result: List[Info],turnoId1: Option[Int] = None):(Option[List[InfoReq]], Info) = {
    val infoDay = createListDayBetween(dateI,dateF).filter(date => !result.filter(result => person.matricola.contains(result.idDriver))
      .flatMap(_.infoDay.map(_.data)).exists(_.compareTo(date) == 0)).foldLeft((assignedForTurn,List[InfoDay]()))((x,y) => {
      val updateMap = updateInfoReq(x._1,y,turnoId)
      turnoId1 match {
        case Some(value) => (updateInfoReq(updateMap,y,value),x._2 :+ InfoDay(y,Some(turnoId),turnoId1))
        case None => (updateMap,x._2 :+ InfoDay(y,Some(turnoId),turnoId1))
      }
    })
    (infoDay._1,Info(person.matricola.head,0,isFisso = false,0,infoDay._2))
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
          val shift2 = Some(shift % 6 + 1)
          val assignedWeek = assignShiftAndUpdateRequest(head._2, allRequest,week.head,week.last,shift,result,shift2)
          _assignWeekForRotatory(next, assignedWeek._1, week,resultNew :+assignedWeek._2)
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
      .foldLeft((0.0, 1))((acc, i) => ((acc._1 + (i - acc._1) / acc._2), acc._2 + 1))._1).maxBy(_._2)._1) match {
      case Some(value) => value
    }

   private def createWeekLists(dateI: Date, dateF: Date): List[List[Date]] = {

    @scala.annotation.tailrec
    def _createWeekLists(date: Date, result: List[List[Date]] = List.empty): List[List[Date]] = {
      if (nextWeek(date).compareTo(dateF) > 0)
        result:+ createListDayBetween(date, dateF)
      else
        _createWeekLists(nextWeek(date), result:+ createListDayBetween(date, getEndDayWeek(date)))
    }

    _createWeekLists(dateI)
  }
  private def updateInfoReq(infoReq: Option[List[InfoReq]], date: Date, shift: Int): Option[List[InfoReq]] = {
    infoReq.map(_.collect{
      case x if x.data.compareTo(date) == 0 && x.idShift == shift => x.copy(assigned = x.assigned +1)
      case x => x
    })
  }

  private def rulerThirfSaturday() = {

  }

  private def assignFreeDayFixed6x1() = {

  }

  private def assignFreeDayRotary6x1() = {

  }
}