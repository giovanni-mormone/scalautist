package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Disponibilita
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceDisponibilita, InstancePersona, InstanceRisultato}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{DisponibilitaTableQuery, PersonaTableQuery}
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * @author Giovanni Mormone
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{
  def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int):Unit


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
}

object DisponibilitaOperation extends DisponibilitaOperation{

  override def insert(element:Disponibilita): Future[Option[Int]] = {
    for{
      disponibilita <-  InstanceDisponibilita.operation().execQueryFilter(f => f.id, x => x.giorno1 === element.giorno1 && x.giorno2 === element.giorno2)
      result <- if (disponibilita.isEmpty) for(newDisp <- super.insert(element)) yield newDisp else Future.successful(disponibilita.get.headOption)
    } yield result
  }

  override def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int): Unit = {
    for {
      risultato <- InstanceRisultato.operation()
        .execQueryFilter(risultato => risultato.data, risultato => risultato.id === idRisultato)
      week<- risultato.toList.flatten.foldRight(Future.successful((-1,"")))((date,_)=>Future.successful(getWeekNumber(date),nameOfDay(date)))
      join<-join(week,idTemrinale)
      personForTerminal<-InstancePersona.operation()
        .execQueryFilter(persona=>persona.disponibilitaId, persona=>persona.terminaleId === idTemrinale)
      persons<- InstanceRisultato.operation()
        .execQueryFilter(person=>person.personeId,person=>person.data.inSet(risultato.toList.flatten))
          .collect { case Some(value) => Some(value.map(id=>id->value.count(_ == id)).filter{_==2})
                    case None => None}

    }yield None

  }
  def join(value:(Int,String),idTerminale:Int)= {
    val joinQuery = for {
      persona <- PersonaTableQuery.tableQuery()
      dispo <- DisponibilitaTableQuery.tableQuery()
      if (persona.terminaleId === idTerminale && dispo.id === persona.disponibilitaId && dispo.settimana===value._1
        && (dispo.giorno1===value._2 || dispo.giorno2===value._2))
    } yield (persona.nome, persona.cognome)
    InstancePersona.operation().execJoin(joinQuery)}

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
