package view.fxview.component.driver.subcomponent.parent

import view.fxview.component.modal.ModalParent

trait ModalDisponibilitaParent extends ModalParent{

  def selectedDays(day1: String, day2: String): Unit

}
