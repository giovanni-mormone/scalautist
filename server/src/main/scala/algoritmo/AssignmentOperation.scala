package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation.InfoForAlgorithm
import caseclass.CaseClassDB.{Contratto, Persona, RichiestaTeorica, Turno}
import caseclass.CaseClassHttpMessage.AlgorithmExecute
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

  private val FULL_AND_PART_TIME_5X2= List(1,2,3,4)
  private val FULL_TIME_6X1= ""
  private val PART_TIME_6X1= ""
  private val IS_FISSO=true
  private val FREE_DAY = 10
  private val ABSENCE=11

  final case class InfoForAlgorithm(shift: List[Turno],theoricalRequest:List[RichiestaTeorica],
                                    persons:List[(Int,Persona)],allContract:Option[List[Contratto]]=None,absence:Option[List[(Int,Date,Date)]]=None,
                                    allRequest:Option[List[InfoReq]]=None,previousSequence: Option[List[PreviousSequence]]=None)

  final case class InfoReq(idShift:Int,request:Int,assigned:Int,idDay:Int,data:Date,idTerminal:Int)
  final case class PreviousSequence(idDriver:Int,sequenza:Int,distanceFreeDay:Int)
  final case class InfoDay(data:Date,shift:Option[Int]=None,shift2:Option[Int]=None,straordinario:Option[Int]=None,freeDay:Boolean=false,absence:Boolean=false)
  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])

  private val emitter=ConfigEmitter()
  def apply():AssignmentOperation ={
    emitter.start()
    this
  }
  override def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]): Unit = {
    emitter.sendMessage("Iniziando processo di assegnazione")
    infoForAlgorithm.foreach(info=>{
       info.theoricalRequest.map(terminal=>{
         val personaFilter = info.persons.filter(_._2.idTerminale.contains(terminal.terminaleId))
        info.copy(persons = personaFilter,
          allRequest = info.allRequest.map(_.filter(_.idTerminal==terminal.terminaleId)),
          theoricalRequest = info.theoricalRequest.filter(_.terminaleId==terminal.terminaleId),
          previousSequence = info.previousSequence.map(_.filter(value=>personaFilter.map(_._2.matricola).exists(id=>id.contains(value.idDriver)))))
      }).foreach(info=>startAlgorithm(info,algorithmExecute))

    })
   //IN QUESTO PUNTO SEPARIAMO PER TERMINALE (?-> questo per la STUPIDAGGINE DI GIANNI)
  }
  private def startAlgorithm(infoForAlgorithm: InfoForAlgorithm,algorithmExecute: AlgorithmExecute): Unit =Future{
    val (driver5x2,driver6x1)=infoForAlgorithm.persons.partition(idPerson=>FULL_AND_PART_TIME_5X2.contains(idPerson._1))
    assignSaturdayAndSunday5x2(infoForAlgorithm.allContract,algorithmExecute,driver5x2)
      .zip(assignSunday6x1(infoForAlgorithm,algorithmExecute,driver6x1))
  }

  private def assignSaturdayAndSunday5x2(allContract: Option[List[Contratto]],algorithmExecute: AlgorithmExecute,driver: List[(Int, Persona)]):List[Info]={
    @scala.annotation.tailrec
    def _assignSaturdayAndSunday(person: List[(Int, Persona)], date: Date, info: List[Info]=List.empty):List[Info]= person match {
      case ::(head, next) =>val isFisso=allContract.map(_.filter(_.idContratto.contains(head._1)).map(_.turnoFisso)).toList.flatten match {
        case List(isFisso) => isFisso}
        _assignSaturdayAndSunday(next,date,
        info:+Info(head._2.matricola.head,head._2.idTerminale.head,isFisso =isFisso ,head._1, _iterateDate(date)))
      case Nil =>info
    }
    @scala.annotation.tailrec
    def _iterateDate(date: Date, infoDay: List[InfoDay]=List.empty):List[InfoDay]=date match {
      case date if date.compareTo(algorithmExecute.dateF)<0 && getEndDayWeek(date).compareTo(algorithmExecute.dateF)<0=>
        _iterateDate(subtract(getEndDayWeek(date),1),
        infoDay:::List(InfoDay(getEndDayWeek(date),freeDay = true),InfoDay(subtract(getEndDayWeek(date),-1),freeDay = true)))
      case _ => infoDay.sortBy(_.data)
    }
   _assignSaturdayAndSunday(driver,algorithmExecute.dateI)

  }

  //final case class InfoDay(data:Date,shift:Option[Int]=None,shift2:Option[Int]=None,straordinario:Option[Int]=None,freeDay:Option[Int]=None,absence:Option[Int]=None)
  //  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])
  private def assignSunday6x1(infoForAlgorithm: InfoForAlgorithm,algorithmExecute: AlgorithmExecute,driver: List[(Int, Persona)]):List[Info]={

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

}
object  e extends App{


  private def startAlgorithm(infoForAlgorithm: Int): Unit ={
    println(infoForAlgorithm)
  }
  (0 to 10).foreach(startAlgorithm)
}