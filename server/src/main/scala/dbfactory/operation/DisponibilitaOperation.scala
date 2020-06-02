package dbfactory.operation

import caseclass.CaseClassDB.Disponibilita
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceDisponibilita
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * @author Giovanni Mormone
 *
 * Allows to perform operations on the table Disponibilita in the DB
 */
trait DisponibilitaOperation extends OperationCrud[Disponibilita]{

}

object DisponibilitaOperation extends DisponibilitaOperation{

  override def insert(element: Disponibilita): Future[Int] = {
    val promiseInsertDisp = Promise[Int]
    InstanceDisponibilita.operation()
      .execQueryFilter(f => f.id, x => x.giorno1 === element.giorno1 && x.giorno2 === element.giorno2)
      .onComplete {
        case Success(value) if value.nonEmpty => promiseInsertDisp.success(value.head)
        case Success(_) => super.insert(element).onComplete{
          case Success(value) => promiseInsertDisp.success(value)
          case Failure(exception) => promiseInsertDisp.failure(exception)
        }
        case Failure(exception) => promiseInsertDisp.failure(exception)
      }
    promiseInsertDisp.future
  }
}
