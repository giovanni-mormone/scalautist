package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Disponibilita
import caseclass.CaseClassHttpMessage.InfoReplacement
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstanceDisponibilita, InstancePersona, InstanceRisultato}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{AssenzaTableQuery, DisponibilitaTableQuery, PersonaTableQuery, StoricoContrattoTableQuery}
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * @author Giovanni Mormone,Fabian Aspee Encina, Francesco Cassano
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{

  /**
   * returns diponibilita of employee in a week
   *
   * @param idUser
   *               employee id
   * @param week
   *             number of week in year
   * @return
   *         disponibilita
   */
  def getDisponibilita(idUser: Int, week: Int): Future[Option[Disponibilita]]

  /**
   *  Method that return all driver that have a availability for replace an another driver, return all driver
   *  that working one or 2 shift, if work 3 shift this is not considered
   * @param idRisultato id that works for search the day where exist a absence
   * @param idTemrinale represent terminal where exist a absence
   * @param idTurno represent turno where existe absence
   * @return Option of List of Int,String,String where Int-> idDriver, String->name, String->surName
   */
  def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[Option[List[InfoReplacement]]]
}

object DisponibilitaOperation extends DisponibilitaOperation{
    private case class QueryPersonStoricAvail(idPerson:Option[List[Int]],week:Int,giorno:String,idTurno:Int,idTerminale:Int,date:Date)

    private object convertToQueryPersonStoricAvail{
    private val DEFAULT_YEAR = 0
    private val DEFAULT_MONTH = 0
    private val DEFAULT_DAY = 0
    private val DEFAULT_WEEK = 0
    private val DEFAULT_NAME_DAY = ""
    implicit def datToQueryPersonStoricAvail(idPerson:(Option[List[Int]],Option[(Date, Int, String)],Int,Int)):QueryPersonStoricAvail = {
      val (date,week,day) = idPerson._2 match {
        case Some(value) => value
        case None => (Date.valueOf(LocalDate.of(DEFAULT_YEAR,DEFAULT_MONTH,DEFAULT_DAY)),DEFAULT_WEEK,DEFAULT_NAME_DAY)
      }
      QueryPersonStoricAvail(idPerson._1,week,day,idPerson._3,idPerson._4,date)
    }
  }
  import convertToQueryPersonStoricAvail._
  //identifies driver by id
  private val RUOLO_DRIVER=3
  // TODO Controllare anche la settimana :) \(-_-)/
  override def insert(element:Disponibilita): Future[Option[Int]] = {
    for{
      disponibilita <-  InstanceDisponibilita.operation().execQueryFilter(f => f.id, x => x.giorno1 === element.giorno1 && x.giorno2 === element.giorno2)
      result<-disponibilita.map(_=>super.insert(element)) match {
        case Some(value)=>value
        case None=>Future.successful(None)
      }
    } yield result
  }

  override def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[Option[List[InfoReplacement]]] = {
    callOperation(idRisultato:Int, idTemrinale:Int, idTurno:Int).collect{
      case (personWithoutShift, allWithAbsence) =>personWithoutShift.toList.flatten.filter(absence=> !allWithAbsence.toList.flatten
        .contains(absence)
       )
    }.flatMap(result=>selectAllDriverNameAndSurname(result,idRisultato))
  }

  private def callOperation(idRisultato:Int, idTemrinale:Int, idTurno:Int): Future[(Option[List[Int]], Option[List[Int]])] =
    for {
      risultato <- selectDateAndCreateDayAndWeek(idRisultato)
      resultJoinPersonaDis<- risultato.map(value => getPersonWithoutAbsence(idTemrinale,value._1)) match {
        case Some(value) => value
        case None =>Future.successful(None)
      }
      allWithAbsence<- risultato.map(value => selectAllWithAbsence(value._1)) match {
        case Some(value) => value
        case None =>Future.successful(None)
      }
      personWithoutShift<- getPersonaWithoutShiftAndAvailability(resultJoinPersonaDis,risultato,idTurno,idTemrinale)
    }yield(personWithoutShift,allWithAbsence)

  private def selectDateAndCreateDayAndWeek(idRisultato:Int): Future[Option[(Date, Int, String)]] =
    InstanceRisultato.operation()
      .execQueryFilter(risultato => risultato.data, risultato => risultato.id === idRisultato)
      .collect {
        case Some(value) => Some(value.map(date => (date, getWeekNumber(date), nameOfDay(date))) match {
          case List(a) => a })
        case None =>None
      }

  private def selectAllWithAbsence(date:Date): Future[Option[List[Int]]] =
    InstanceAssenza.operation().execQueryFilter(assenza=>assenza.personaId,
      assenza=>assenza.dataInizio<=date && assenza.dataFine>=date).map(_.map(_.distinct))

  private def getPersonWithoutAbsence(idTerminale:Int,date:Date): Future[Option[List[Int]]] = {
    val joinQuery = for {
      (persona,_) <- PersonaTableQuery.tableQuery() joinLeft AssenzaTableQuery.tableQuery()
        .filter(az=>az.dataInizio>date || az.dataFine<date || Some(az.dataInizio).isEmpty || Some(az.dataFine).isEmpty) on (_.id===_.personaId)
      if persona.ruolo===RUOLO_DRIVER && persona.terminaleId === idTerminale
    } yield persona.id
    InstancePersona.operation().execJoin(joinQuery).map(result=>result.map(_.distinct))
  }

  def getPersonaWithoutShiftAndAvailability(dat:QueryPersonStoricAvail): Future[Option[List[Int]]] ={
    val joinQuery = for {
      persona<- PersonaTableQuery.tableQuery()
      storico<-StoricoContrattoTableQuery.tableQuery()
      disp<-DisponibilitaTableQuery.tableQuery()
      if( persona.id===storico.personaId && persona.disponibilitaId===disp.id && persona.id.inSet(dat.idPerson.toList.flatten) &&
        disp.settimana===dat.week && (disp.giorno1===dat.giorno || disp.giorno2===dat.giorno) &&
        (storico.turnoId=!=dat.idTurno || storico.turnoId1=!=dat.idTurno) && persona.terminaleId===dat.idTerminale
        && (storico.dataFine>dat.date || Some(storico.dataFine).isEmpty)
        && Some(persona.disponibilitaId).isDefined)
    } yield storico.personaId
    InstancePersona.operation().execJoin(joinQuery).map(result=>result.map(_.distinct))
  }

  private def selectAllDriverNameAndSurname(idPerson:List[Int],idRisultato:Int): Future[Option[List[InfoReplacement]]] ={
    InstancePersona.operation().execQueryFilter(person=>(person.id,person.nome,person.cognome),person=>person.id.inSet(idPerson)).collect {
      case Some(value) => Some(value.map(person=>InfoReplacement(idRisultato,person._1,person._2,person._3)))
      case None =>None
    }
  }

  override def getDisponibilita(idUser: Int, week: Int = getWeekNumber(Date.valueOf(LocalDate.now()))): Future[Option[Disponibilita]] = {
    val filter = for {
      disponibilita <- PersonaTableQuery.tableQuery() join DisponibilitaTableQuery.tableQuery() on (_.disponibilitaId === _.id)
                      if disponibilita._2.settimana === week && disponibilita._1.id === idUser
    } yield (disponibilita._2.id, disponibilita._2.giorno1, disponibilita._2.giorno2, disponibilita._2.settimana)

    InstanceDisponibilita.operation().execJoin(filter).collect{
      case Some(List(disp)) => Some(Disponibilita(disp._4, disp._2, disp._3, Some(disp._1)))
      case _ => None
    }
  }
}
