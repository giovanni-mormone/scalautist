package dbfactory.operation
import java.sql.Date
import java.time.temporal.ChronoUnit
import java.util.Calendar

import akka.http.scaladsl.server.util.Tuple
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePersona}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{AssenzaTableQuery, PersonaTableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** @author Giovanni Mormone
 *  Trait which allows to perform operations on the assenze table.
 */
trait AssenzaOperation extends OperationCrud[Assenza]{
  /**
   * Returns a list of [[caseclass.CaseClassHttpMessage.Ferie]] for the year provided as input.
   * @param data
   *             The year of the remainig feries to get
   */
  def getAllFerie(data: Int): Future[Option[List[Ferie]]]
}

object AssenzaOperation extends AssenzaOperation{

  private case class JoinResult(dataInizio:Date,dataFine:Date,idPersona:Int,nomePersona:String,cognomePersona:String)

  private object JoinResult{
    implicit def toJoin(tup:(Date,Date,Int,String,String)):JoinResult =
      JoinResult.apply _ tupled tup

    implicit def t(list:Option[List[(Date,Date,Int,String,String)]]): Option[List[JoinResult]] =
      list.map(x => x.map(x => JoinResult.apply _ tupled x))

    implicit def a(a:List[(Date,Date,Int,String,String)]): Option[List[JoinResult]]=
      t(Option(a))
  }

  private val GIORNI_FERIE_ANNUI: Int = 35
  private val dateFromYear: Int => Date = year => {
    val calendar = Calendar.getInstance()
    calendar.set(year,0,1)
    new Date(calendar.getTimeInMillis)
  }

  override def getAllFerie(data: Int):Future[Option[List[Ferie]]] = {
    val nextYear = dateFromYear(data+1)
    val currentYear = dateFromYear(data)

    startingPersoneFerie().flatMap(ferie => constructFerie(ferie,currentYear,nextYear))

  }

  private def constructFerie(ferie: Option[List[Ferie]],currentYear: Date, nextYear: Date): Future[Option[List[Ferie]]] ={
    val filterJoin = for{
      (persona,assenza) <- PersonaTableQuery.tableQuery() join AssenzaTableQuery.tableQuery() on (_.id === _.personaId)
      if assenza.dataFine < nextYear && assenza.dataInizio >= currentYear && !assenza.malattia
    }yield (assenza.dataInizio,assenza.dataFine,persona.id,persona.nome,persona.cognome)

    val ferieReduction: (Ferie,Ferie) => Ferie = (x,y) => Ferie(x.idPersona,x.nomeCognome,x.giorniVacanza+y.giorniVacanza)

    val computeDays: (Date,Date) => Int = (dateStart,dateStop) =>
      GIORNI_FERIE_ANNUI - ChronoUnit.DAYS.between(dateStart.toLocalDate,dateStop.toLocalDate).toInt

    val toFerie: JoinResult=> Ferie = join =>
      Ferie(join.idPersona,join.nomePersona.concat(join.cognomePersona).concat(join.idPersona.toString),computeDays(join.dataInizio,join.dataFine))

    InstanceAssenza.operation().execJoin(filterJoin).map(_.map(ass => ass.map(x => toFerie(x)).groupBy(_.idPersona).values.map(_.reduce(ferieReduction)).toList)).collect{
      case None => ferie
      case Some(value) => ferie.map(_.map(startingFerie => Ferie(startingFerie.idPersona,startingFerie.nomeCognome, value.find(_.idPersona == startingFerie.idPersona).getOrElse(startingFerie).giorniVacanza)))
    }
  }

  private def startingPersoneFerie(): Future[Option[List[Ferie]]] = {
    InstancePersona.operation().execQueryFilter(field => (field.id,field.nome,field.cognome),_.ruolo === 3)
      .map(_.map(_.map(x => Ferie(x._1,x._2.concat(x._3).concat(x._1.toString),GIORNI_FERIE_ANNUI))))
  }
}
