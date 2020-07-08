package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation.InfoForAlgorithm
import caseclass.CaseClassDB.{Contratto, Persona, RichiestaTeorica, StoricoContratto, Turno}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA}
import utils.DateConverter._

import scala.collection.immutable.{AbstractMap, SeqMap, SortedMap}
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
    assignSaturdayAndSunday5x2(infoForAlgorithm.allContract, algorithmExecute, driver5x2.map(v => (v._1.contrattoId,v._2)))
      .zip(assignSunday6x1(infoForAlgorithm, algorithmExecute, driver6x1)).map{
      case (value, value1) => PrintListToExcel.printInfo(algorithmExecute.dateI,algorithmExecute.dateF,value1)
    }
    //andare avanti dopo la ZIPPPP
  }

  private def assignSaturdayAndSunday5x2(allContract: Option[List[Contratto]], algorithmExecute: AlgorithmExecute, driver: List[(Int, Persona)]): Future[List[Info]] = Future{
    @scala.annotation.tailrec
    def _assignSaturdayAndSunday(person: List[(Int, Persona)], date: Date, info: List[Info] = List.empty): List[Info] =  person match {
      case ::(head, next) => _assignSaturdayAndSunday(next, date, info :+ Info(head._2.matricola.head, head._2.idTerminale.head,
        isFisso =  isFisso(allContract,head._1), head._1, _iterateDate(date)))
      case Nil => info
    }

    @scala.annotation.tailrec
    def _iterateDate(date: Date, infoDay: List[InfoDay] = List.empty): List[InfoDay] = date match {
        //gestire le assenze bitch
      case date if date.compareTo(algorithmExecute.dateF) < 0 && getEndDayWeek(date).compareTo(algorithmExecute.dateF) < 0 =>
        _iterateDate(subtract(getEndDayWeek(date), 1),
          infoDay ::: List(InfoDay(getEndDayWeek(date), freeDay = true), InfoDay(subtract(getEndDayWeek(date), -1), freeDay = true)))
      case _ => infoDay.sortBy(_.data)
    }

    _assignSaturdayAndSunday(driver, algorithmExecute.dateI)

  }

  private val isFisso: (Option[List[Contratto]], Int) => Boolean = (allContract,idContratto) => allContract.map(_.filter(_.idContratto.contains(idContratto)).map(_.turnoFisso)).toList.flatten match {
    case List(isFisso) => isFisso
  }

  private def allSundayMonth(sunday:Date,finalDayMont:Date):List[Date]={
    @scala.annotation.tailrec
    def _allSunday(sunday:Date,allSunday:List[Date]=List.empty): List[Date] = sunday match {
      case date if date.compareTo(finalDayMont) < 0 =>_allSunday(subtract(date, 7), allSunday :+ date)
      case date if date.compareTo(finalDayMont) == 0 => (allSunday ::: createListDayBetween(subtract(date,-6),date).sortBy(_.getTime))
      case date if date.compareTo(finalDayMont)>0 => allSunday
    }
    _allSunday(sunday)
  }



  //final case class InfoDay(data:Date,shift:Option[Int]=None,shift2:Option[Int]=None,straordinario:Option[Int]=None,freeDay:Option[Int]=None,absence:Option[Int]=None)
  //  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])
  private def assignSunday6x1(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute, driver: List[(StoricoContratto, Persona)]): Future[List[Info]] = Future{

    @scala.annotation.tailrec
    def iterateMap(map: List[(Int,List[(Int,Persona)])], assigned: Map[Int,Int] = Map(1 ->0, 2 -> 0, 3 -> 0, 4 -> 0, 5-> 0),result: List[Info] = List.empty): List[Info] = map match {
      case ::(head, next) if startMonthDate(algorithmExecute.dateI) == startMonthDate(algorithmExecute.dateF) =>
        val previousSundays = allSundayMonth(getEndDayWeek(previousMonthDate(algorithmExecute.dateI)),endOfMonth(previousMonthDate(algorithmExecute.dateI)))
        val sundays = allSundayMonth(getEndDayWeek(algorithmExecute.dateI),endOfMonth(algorithmExecute.dateF))
        val assignement = assignBalancedSundays(head._2,sundays,previousSundays,infoForAlgorithm.allContract.head,infoForAlgorithm.previousSequence,assigned)
        iterateMap(next,assignement._2, result::: assignement._1)

      //case ::(head, next) if startMonthDate(algorithmExecute.dateI) != startMonthDate(algorithmExecute.dateF) =>
      case Nil => result
    }

    val (fisso,rotatorio) = driver.groupBy(_._1.contrattoId).toList.partition(t => infoForAlgorithm.allContract.toList.flatten.exists(tr => tr.idContratto.contains(t._1) && tr.turnoFisso))
    /*val(part,full) = fisso.partition(t => infoForAlgorithm.allContract.toList.flatten.exists(tr => tr.idContratto.contains(t._1) && tr.partTime))
      ::::::(part,infoForAlgorithm.shift)*/


    val t = iterateMap(rotatorio.map(x1 => x1._1 -> x1._2.map(x => (x._1.contrattoId,x._2))))//iterateMap(driver.groupBy(_._1).toList)
    t
  }

//  def ::::::(part: List[(Int, List[(Int, Persona)])], shift: List[Turno],result: List[(Int, List[(Int, Persona)])] = List.empty):List[(Int, List[(Int, Persona)])] = shift match {
//    case ::(head, next) =>
//    case Nil =>
//  }
  
//  private def groupForDriver(infoForAlgorithm: InfoForAlgorithm, algorithmExecute: AlgorithmExecute)={
//    algorithmExecute.gruppo match {
//      case Some(value) => assignGroupDriver(infoForAlgorithm,value)
//      case None =>
//    }
//  }
//  private def assignGroupDriver(infoForAlgorithm: InfoForAlgorithm,listGroup:List[GruppoA])={
//    @scala.annotation.tailrec
//    def _assignGroupDriver(groupDriver:List[(Int,List[(Int,Persona)])], listGroup:GruppoA, result:List[Info]=List.empty):List[Info]= groupDriver match {
//      case ::(head, next) => _assignGroupDriver(next,listGroup,result:::iteraDate(head,listGroup))
//      case Nil =>result
//    }
//    @scala.annotation.tailrec
//    def _iteraGroup(listGroup:List[GruppoA], result:List[Info]=List.empty):List[Info]= listGroup match {
//      case ::(head, next) => _iteraGroup(next,result:::_assignGroupDriver(infoForAlgorithm.persons.groupBy(_._1).toList,head))
//      case Nil =>result
//    }
//    _iteraGroup(listGroup)
//  }

//  private def iteraDate(drivers:(Int,List[(Int,Persona)]),listGroup:GruppoA):List[Info]={
//    def _iteraDate(listGroup:GruppoA): Unit ={}
//    List()
//  }


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

  private def assignBalancedSundays(drivers: List[(Int, Persona)], sundays: List[Date], previousSundays: List[Date],contract: List[Contratto], previousSequence: Option[List[PreviousSequence]], assigned: Map[Int,Int]): (List[Info],Map[Int,Int]) = {

    @scala.annotation.tailrec
    def _assignBalancedSundays(drivers: List[(Int, Persona)], assigned: Map[Int,Int], result: List[Info] = List.empty): (List[Info],Map[Int,Int]) = drivers match {
      case ::(head, next) => previousSequence.toList.flatten.find(x => head._2.matricola.contains(x.idDriver)) match {
        case Some(value) => sequenceCombinations.get(value.sequenza, previousSundays.length, sundays.length) match {
          case Some(value) =>
            val sequence = metodino(assigned,value)
            val x  = assigned.updated(sequence, assigned.getOrElse(sequence,0) + 1)
            val dates = assignSunday(sundays.sortBy(_.getTime),sequence,sundays.length)
            val fisso: Boolean = isFisso(Some(contract),head._1)
            val newResult = result ::: List(Info(head._2.matricola.head,head._2.idTerminale.head,fisso,head._1,List(InfoDay(dates._1,freeDay = true),InfoDay(dates._2,freeDay = true))))
            _assignBalancedSundays(next,x,newResult)
        }
      }
      case Nil =>(result,assigned)
    }

    _assignBalancedSundays(drivers,assigned)
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

  private def assignShiftFixed() = {

  }

  private def assignShiftRotary() = {

  }

  private def rulerThirfSaturday() = {

  }

  private def assignFreeDayFixed6x1() = {

  }

  private def assignFreeDayRotary6x1() = {

  }
}