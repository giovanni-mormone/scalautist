package dbfactory.operation

import caseclass.CaseClassDB.Terminale
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceTerminale
import dbfactory.implicitOperation.OperationCrud
import dbfactory.table.TerminaleTable.TerminaleTableRep
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * @author Giovanni Mormone
 *
 * Abstraction of the operations tha one can do on the Terminale Table in DB.
 *
 */
trait TerminaleOperation extends OperationCrud[Terminale]{

  /**
   * Returns all the terminale within one given zone.
   * @param zonaId
   *               The zone of the id
   * @return
   *         A list of the terminali in the zone.
   */
  def getTermininaliInZona(zonaId: Int): Future[Option[List[Terminale]]]
}


object TerminaleOperation extends TerminaleOperation {

  override def getTermininaliInZona(zonaId: Int): Future[Option[List[Terminale]]] = {

    val promiseTerminaliInZona = Promise[Option[List[Terminale]]]
    execFilter(promiseTerminaliInZona, x => x.zonaId === zonaId)
    promiseTerminaliInZona.future
  }


  private def execFilter(promise: Promise[Option[List[Terminale]]],f:TerminaleTableRep=>Rep[Boolean]): Future[Unit] = Future {
    InstanceTerminale.operation().selectFilter(f) onComplete {
      case Success(value) if value.nonEmpty=>promise.success(value)
      case Success(_) =>promise.success(None)
      case Failure(_)=>promise.success(None)
    }
  }
}