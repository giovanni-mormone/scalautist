package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassHttpMessage.Ferie

//metodi view -> controller
trait IllBoxParent{
  def openModal(item:Ferie,isMalattia:Boolean=true):Unit
}