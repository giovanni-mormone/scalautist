package dbfactory.operation

import caseclass.CaseClassDB.Disponibilita
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceDisponibilita
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Giovanni Mormone
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{

}

object DisponibilitaOperation extends DisponibilitaOperation{

  override def insert(element:Disponibilita): Future[Option[Int]] = {
    for{
      disponibilita <-  InstanceDisponibilita.operation().execQueryFilter(f => f.id, x => x.giorno1 === element.giorno1 && x.giorno2 === element.giorno2)
      result <- if (disponibilita.isEmpty) for(newDisp <- super.insert(element)) yield newDisp else Future.successful(disponibilita.get.headOption)
    } yield result
  }
}
