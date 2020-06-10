package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassHttpMessage.Ferie

trait HolidayBoxParent{
  def openModal(item:Ferie,isMalattia:Boolean=true):Unit
}
