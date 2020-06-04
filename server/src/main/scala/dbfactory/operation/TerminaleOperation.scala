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
   * Returns all the terminale within one given zone, returning None if no terminale is associated with the zone
   * @param zonaId
   *               The zone of the id
   * @return
   *         A [[scala.concurrent.Future]] [[scala.Option]] list of the terminali in the zone; The value of the Option
   *         is None if cannot find terminali associated to the zona.
   */
  def getTermininaliInZona(zonaId: Int): Future[Option[List[Terminale]]]
}


object TerminaleOperation extends TerminaleOperation {

  override def getTermininaliInZona(zonaID:Int): Future[Option[List[Terminale]]] = {
   InstanceTerminale.operation().selectFilter(x => x.zonaId === zonaID).collect(collectCheck(_))
  }
}