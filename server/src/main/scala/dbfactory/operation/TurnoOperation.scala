package dbfactory.operation
import caseclass.CaseClassDB.Turno
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes

import scala.concurrent.Future
trait TurnoOperation extends OperationCrud[Turno]{
  /**
   * method that allow verify if turno exist in database
   * @param element id of the shift that we want verify
   * @return Future of Option of Int with status operation
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if shift exist in database
   *         [[messagecodes.StatusCodes.ERROR_CODE3]] if shift not exist in database
   */
  def verifyShift(element:Int): Future[Option[Int]]
}
object TurnoOperation extends TurnoOperation {
  override def verifyShift(element:Int): Future[Option[Int]] ={
    select(element).collect {
      case Some(_) => Option(StatusCodes.SUCCES_CODE)
      case None => Option(StatusCodes.ERROR_CODE3)
    }
  }


}