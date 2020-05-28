package model.entity

import caseclass.CaseClassDB.Turno
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
   * Future of Turno seq
   */
  def getTurn(id: Int, startData: String, endData: String): Future[Seq[Turno]]

  def getSalaries(id: Int): Future[]
}
