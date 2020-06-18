package dbfactory.operation

import caseclass.CaseClassDB.Disponibilita
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceDisponibilita, InstancePersona, InstanceRisultato}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.PersonaTableQuery
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * @author Giovanni Mormone
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{
  def allDriverWithAvailabilityForADate(idRisultato:Int, idTemrinale:Int, idTurno:Int):Unit
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
      personForTerminal<-InstancePersona.operation()
        .execQueryFilter(persona=>persona.disponibilitaId, persona=>persona.terminaleId === idTemrinale)
      persons<- InstanceRisultato.operation()
        .execQueryFilter(person=>person.personeId,person=>person.data.inSet(risultato.toList.flatten))
          .collect { case Some(value) => Some(value.map(id=>id->value.count(_ == id)).filter{_==2})
                    case None => None}

    }yield(None)
    PersonaTableQuery.tableQuery().filter(_.terminaleId===idTemrinale)
  }


}
