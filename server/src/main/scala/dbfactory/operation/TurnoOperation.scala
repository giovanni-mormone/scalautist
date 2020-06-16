package dbfactory.operation

import java.sql.Date

import caseclass.CaseClassDB.Turno
import caseclass.CaseClassHttpMessage.InfoHome
import dbfactory.implicitOperation.OperationCrud

import scala.concurrent.Future
trait TurnoOperation extends OperationCrud[Turno]{

  /**
   * @TODO
   * @param idUser
   * @param date
   * @return
   */
  def getTurniInDate(idUser: Int, date: Date): Future[Option[InfoHome]]

  /**
   * @TODO
   * @param idUser
   * @param date
   * @return
   */
  def getTurniSettimanali(idUser: Int, date: Date): Future[Option[List[InfoHome]]]

}
object TurnoOperation extends TurnoOperation {

  override def getTurniInDate(idUser: Int, date: Date): Future[Option[InfoHome]] = ???

  override def getTurniSettimanali(idUser: Int, date: Date): Future[Option[List[InfoHome]]] = ???
}