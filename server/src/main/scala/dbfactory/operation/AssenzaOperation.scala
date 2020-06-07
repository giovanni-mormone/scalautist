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
   * Returns a list of [[caseclass.CaseClassHttpMessage.Ferie]] for the year provided as input for all the conducenti
   * in the DB.
   * @param data
   *             The year of the remainig feries to get
   */
  def getAllFerie(data: Int): Future[Option[List[Ferie]]]
}

object AssenzaOperation extends AssenzaOperation{

  private case class JoinResult(dataInizio:Date,dataFine:Date,idPersona:Int,nomePersona:String,cognomePersona:String)

  private object JoinResult{

    implicit def tuple5ToJoinResult(tuple:(Date,Date,Int,String,String)):JoinResult =
      JoinResult.apply _ tupled tuple
  }
  //caricare da qualche parte?
  private val GIORNI_FERIE_ANNUI: Int = 35
  //anche questo si può caricare da qualche parte
  private val CODICE_CONDUCENTE: Int = 3
  //idem per i prossimi codici
  /**
   * 0
   */
  private val CODE_WRONG_ASSENZA_DAYS:Int = 0
  /**
   * -1
   */
  private val CODE_NOT_SAME_YEAR:Int = -1
  /**
   * -2
   */
  private val CODE_START_AFTER_END:Int = -2
  /**
   * -2
   */
  private val CODE_TOO_MUCH_DAYS: Int = -3

  //input un anno, output un java.sql.Date -> colpa del DB
  private val dateFromYear: Int => Date = year => {
    val calendar = Calendar.getInstance()
    calendar.set(year,0,1)
    new Date(calendar.getTimeInMillis)
  }
  private val computeDaysBetweenDates: (Date,Date) => Int = (dateStart,dateStop) =>
    ChronoUnit.DAYS.between(dateStart.toLocalDate,dateStop.toLocalDate).toInt

  private val notSameYear: (Date,Date) => Boolean = (start,end) =>
    start.toLocalDate.getYear != end.toLocalDate.getYear

  override def getAllFerie(data: Int):Future[Option[List[Ferie]]] = {
    val nextYear = dateFromYear(data+1)
    val currentYear = dateFromYear(data)
    constructFerie(currentYear,nextYear)
  }

  /**
   *
   * @param element case class that represent instance of the table in database
   * @return
   *         Future of Int that represent status of operation ->
   *            -[[CODE_WRONG_ASSENZA_DAYS]] (0) if the days between the given day are > of [[GIORNI_FERIE_ANNUI]](35 per ora)
   *            -[[CODE_NOT_SAME_YEAR]](-1) if the dates given in input are not of the same year.
   *            -[[CODE_START_AFTER_END]](-2) if the start date is after the end date.
   *            -[[CODE_TOO_MUCH_DAYS]](-3) if the days of the assenza to insert are greater than the remaninig day of assenza for the persona.
   */
  override def insert(element: Assenza): Future[Option[Int]] = element match {
    case Assenza(_,start,end,false,_) if computeDaysBetweenDates(start,end) > GIORNI_FERIE_ANNUI => Future.successful(Some(CODE_WRONG_ASSENZA_DAYS))
    case Assenza(_,start,end,false,_) if notSameYear(start,end) => Future.successful(Some(CODE_NOT_SAME_YEAR))
    case Assenza(_,start,end,false,_) if start.compareTo(end) >= 0 => Future.successful(Some(CODE_START_AFTER_END))
    case Assenza(id,start,end,false,_) => tryInsert(Assenza(id,start,end,malattia = false))
    case _ => super.insert(element)
  }

  private def tryInsert(assenza:Assenza): Future[Option[Int]] = {
    val d:(Int,Assenza) => Int = (x,assenz) => x + computeDaysBetweenDates(assenz.dataInizio,assenz.dataFine)
    InstanceAssenza.operation()
      .selectFilter(filter => filter.personaId === assenza.personaId)
      .map(_.map(_.foldLeft(0)(d))).map(days => days.exists(_ + computeDaysBetweenDates(assenza.dataInizio,assenza.dataFine) > GIORNI_FERIE_ANNUI))
      .flatMap(outOfDays =>if(outOfDays) Future.successful(Option(CODE_TOO_MUCH_DAYS)) else super.insert(assenza))
  }

  /**
   * Constructs the list of ferie in one year. First construct the list of ferie for all the conducenti as if they have
   * full days of ferie remaining; after that select all the assenze in the given year and reduces the output to the Ferie
   * for each conducente; lastly substitutes each Ferie in the starting List of ferie with the computed one, if the conducente
   * did days of assenza, else it remains the basic Ferie.
   */
  private def constructFerie(currentYear: Date, nextYear: Date): Future[Option[List[Ferie]]] ={
    val filterJoin = for{
      (persona,assenza) <- PersonaTableQuery.tableQuery() join AssenzaTableQuery.tableQuery() on (_.id === _.personaId)
      if assenza.dataFine < nextYear && assenza.dataInizio >= currentYear && !assenza.malattia
    }yield (assenza.dataInizio,assenza.dataFine,persona.id,persona.nome,persona.cognome)
    val ferieReduction: (Ferie,Ferie) => Ferie = (x,y) => Ferie(x.idPersona,x.nomeCognome,x.giorniVacanza + y.giorniVacanza)
    val toFerie: JoinResult => Ferie = join =>
      Ferie(join.idPersona,join.nomePersona.concat(join.cognomePersona).concat(join.idPersona.toString),computeDaysBetweenDates(join.dataInizio,join.dataFine))
    val setFerieDays: (List[Ferie],Ferie) => Int = (value, startingFerie)=> GIORNI_FERIE_ANNUI - value.find(_.idPersona == startingFerie.idPersona).getOrElse(Ferie(0,"",0)).giorniVacanza

    startingPersoneFerie()
      .flatMap(ferie => {
        InstanceAssenza.operation().execJoin(filterJoin)
          .map(_.map(ass => ass.map(x => toFerie(x)).groupBy(_.idPersona).values.map(_.reduce(ferieReduction)).toList))
          .collect{
            case None => ferie
            case Some(value) => ferie.map(_.map(startingFerie => Ferie(startingFerie.idPersona,startingFerie.nomeCognome, setFerieDays(value,startingFerie))))
          }
      })
  }

  /**
   * It constructs the list of ferie with default remaining days for all the conducenti.
   * None is returned if there is no conducente in the db.
   */
  private def startingPersoneFerie(): Future[Option[List[Ferie]]] = {
    //prende id-nome-cognome e li concatena
    val tupToNameSurname: ((Int,String,String)) => String = x => x._2.concat(x._3).concat(x._1.toString)

    InstancePersona.operation().execQueryFilter(field => (field.id,field.nome,field.cognome),_.ruolo === CODICE_CONDUCENTE)
      .map(_.map(_.map(x => Ferie(x._1,tupToNameSurname(x),GIORNI_FERIE_ANNUI))))
  }
}
