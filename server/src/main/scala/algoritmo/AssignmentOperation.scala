package algoritmo

import java.sql.Date

import _root_.emitter.ConfigEmitter
import algoritmo.AssignmentOperation.InfoForAlgorithm
import caseclass.CaseClassDB.{Contratto, Persona, RichiestaTeorica, Turno}
import caseclass.CaseClassHttpMessage.AlgorithmExecute

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

  private val FULL_TIME_5X2= 1
  private val PART_TIME_5X2= 2
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
  final case class InfoDay(data:Date,shift:Option[Int]=None,shift2:Option[Int]=None,straordinario:Option[Int]=None,freeDay:Option[Int]=None,absence:Option[Int]=None)
  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])

  private val emitter=ConfigEmitter()
  def apply():AssignmentOperation ={
    emitter.start()
    this
  }
  override def initOperationAssignment(algorithmExecute: AlgorithmExecute,infoForAlgorithm: Future[InfoForAlgorithm]): Unit = {
    emitter.sendMessage("Iniziando processo di assegnazione")
    infoForAlgorithm.foreach(e=> emitter.sendMessage(e.persons.toString()))

  }
  private def assignSaturdayAndSunday5x2(infoForAlgorithm: Future[InfoForAlgorithm])={
    infoForAlgorithm.collect(driver=>driver.persons.filter(idPerson=>idPerson._1==FULL_TIME_5X2 && idPerson._1==PART_TIME_5X2))
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

}
