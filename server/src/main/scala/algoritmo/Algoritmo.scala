package algoritmo

import java.sql.Date

import caseclass.CaseClassHttpMessage.AlgorithmExecute
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona}
import dbfactory.operation.TurnoOperation

import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLServerProfile.api._
trait Algoritmo {
  def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute)
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


  final case class InfoReq(idShift:Int,request:Int,assigned:Int,idDay:Int,data:Date)

  final case class InfoDay(data:Date,shift:Int,shift2:Option[Int])
  final case class Info(idDriver:Int,idTerminal:Int,isFisso:Boolean,tipoContratto:Int,infoDay: List[InfoDay])

  override def shiftAndFreeDayCalculus(algorithmExecute: AlgorithmExecute): Unit = {
    getAllData(algorithmExecute)

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
