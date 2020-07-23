package view.fxview.component.manager.subcomponent.parent

import java.time.LocalDate

trait GroupParamsParent {

  /**
   *
   */
  def resetGroupsParams(): Unit

  /**
   *
   */
  def openModal(initDate: LocalDate, endDate: LocalDate): Unit
}
