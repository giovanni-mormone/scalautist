package view.fxview.component.manager.subcomponent.parent

import java.time.LocalDate

import caseclass.CaseClassDB.Regola
import caseclass.CaseClassHttpMessage.AlgorithmExecute

trait GroupParamsParent {

  /**
   *
   */
  def resetGroupsParams(): Unit

  /**
   *
   * @param initDate
   * @param endDate
   * @param dateNo
   * @param rules
   */
  def openModal(initDate: LocalDate, endDate: LocalDate, dateNo: List[LocalDate], rules: List[Regola]): Unit

  /**
   *
   * @param info
   * @param name
   */
  def showParams(info: AlgorithmExecute, name: Option[String]): Unit
}
