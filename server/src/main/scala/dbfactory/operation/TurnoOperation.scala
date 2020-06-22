package dbfactory.operation
import caseclass.CaseClassDB.Turno
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
trait TurnoOperation extends OperationCrud[Turno]{
}
object TurnoOperation extends TurnoOperation {
  def verifyShift(element:Int): Future[Option[Int]] ={
    select(element).collect {
      case Some(_) => Option(StatusCodes.SUCCES_CODE)
      case None => Option(StatusCodes.ERROR_CODE3)
    }
  }


}