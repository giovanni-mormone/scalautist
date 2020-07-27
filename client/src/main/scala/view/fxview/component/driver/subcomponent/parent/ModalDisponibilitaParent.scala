package view.fxview.component.driver.subcomponent.parent

import view.fxview.component.modal.ModalParent

trait ModalDisponibilitaParent extends ModalParent{

  /**
   * Method allows to save the driver availability to extra work shift in a week
   * @param day1 first day of availability
   * @param day2 second day of availability
   */
  def selectedDays(day1: String, day2: String): Unit

}
