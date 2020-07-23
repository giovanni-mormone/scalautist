package view.fxview.component.manager.subcomponent.parent

import java.time.LocalDate

import caseclass.CaseClassDB.Regola

trait GroupParamsParent {

  /**
   *
   */
  def resetGroupsParams(): Unit

  /**
   *
   */
  def openModal(initDate: LocalDate, endDate: LocalDate, rules: List[Regola]): Unit
}
