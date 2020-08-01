package dbfactory.operation

import caseclass.CaseClassDB.Terminale
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceTerminale
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes.{ERROR_CODE2, SUCCES_CODE}
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.Future

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
  def verifyTerminal(idTerminal: Int): Future[Option[Int]] = {
    select(idTerminal).collect {
      case Some(_) => Option(SUCCES_CODE)
      case None => Option(ERROR_CODE2)
    }
  }


  override def getTermininaliInZona(zonaID:Int): Future[Option[List[Terminale]]] = {
   InstanceTerminale.operation().selectFilter(x => x.zonaId === zonaID)
  }
}