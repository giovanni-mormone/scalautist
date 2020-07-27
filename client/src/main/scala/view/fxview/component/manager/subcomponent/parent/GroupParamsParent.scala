package view.fxview.component.manager.subcomponent.parent

import java.time.LocalDate

import caseclass.CaseClassDB.Regola
import caseclass.CaseClassHttpMessage.AlgorithmExecute

/**
 * @author Francesco Cassano
 * This trait allow to send request to main view
 */
trait GroupParamsParent {

  /**
   * notifies to delete all params and restart the process of choose
   */
  def resetGroupsParams(): Unit

  /**
   * Opens the modal for choosing group of special days
   * @param initDate date of start
   * @param endDate date of end
   * @param dateNo date to not show
   * @param rules rule to show
   */
  def openModal(initDate: LocalDate, endDate: LocalDate, dateNo: List[LocalDate], rules: List[Regola]): Unit

  /**
   * Notifies to open next view
   * @param info instance of [[AlgorithmExecute]] that contains info chosen
   * @param name optional name if user want to save
   */
  def showParams(info: AlgorithmExecute, name: Option[String]): Unit
}
