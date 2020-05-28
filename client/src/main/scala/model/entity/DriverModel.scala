package model.entity

import caseclass.CaseClassDB.{Stipendio, Turno}
import model.Model

import scala.concurrent.Future


/**
 * DriverModel extends [[model.Model]].
 * Interface for driver's operation on data
 */
trait DriverModel extends Model {
  /**
   * Return a set of one driver's turn
   * @param id
   * User id
   * @param startData
   * The start date of the workshift period to be shown
   * @param endData
   * The end date of the workshift period to be shown
   * @return
   * Future of list of Turno
   */
  def getWorkshift(id: Int, startData: String, endData: String): Future[List[Turno]]

  /**
   * Return a set of driver's Stipendio
   * @param id
   * User id
   * @return
   * Future of List of Stipendio
   */
  def getSalaries(id: Int): Future[List[Stipendio]]


}
