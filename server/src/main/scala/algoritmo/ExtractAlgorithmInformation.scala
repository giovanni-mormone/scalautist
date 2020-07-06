package algoritmo

import java.sql.Date

import algoritmo.Algoritmo.{InfoForAlgorithm, InfoReq}
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.AlgorithmExecute
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceRichiesta}
import dbfactory.setting.Table.{GiornoTableQuery, RichiestaTableQuery}
import emitter.ConfigEmitter
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter.{computeDaysBetweenDates, getDayNumber, subtract}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ExtractAlgorithmInformation{
  def getAllData(algorithmExecute:AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm]
}
object ExtractAlgorithmInformation extends ExtractAlgorithmInformation {
  //TODO tornare tutti conducenti per terminale
  //TODO tornare tutti turni esistenti
  //TODO tornare quantità di giorni trascorsi dal ultimo libero di ogni conducente col suo turno
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

  override def getAllData(algorithmExecute: AlgorithmExecute,infoForAlgorithm: InfoForAlgorithm):Future[InfoForAlgorithm] =
    for{
      absence<-InstanceAssenza.operation().execQueryFilter(persona=>(persona.id,persona.dataInizio,persona.dataFine),
        filter=>filter.dataInizio>=algorithmExecute.dateI && filter.dataFine<=algorithmExecute.dateF)
      allRequest<-getTheoricalRequest(algorithmExecute,infoForAlgorithm)
    }yield infoForAlgorithm.copy(absence = absence,allRequest = allRequest)

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
      case Nil =>emitter.sendMessage(s"costruendo infoReq for day $infoReq")
        Option(infoReq)
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
}
