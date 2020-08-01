package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassHttpMessage.Ferie

/**
 * @author Fabian Aspee Encina
 *
 * It is the interface of the methods used by illness views to make requests to main view
 */
trait IllBoxParent{

  /**
   * Request to open Modal to add an employee's period of illness
   *
   * @param item
   *             instance of [[caseclass.CaseClassHttpMessage.Ferie]] to add to the database
   * @param isMalattia
   *                   boolean to explicit which kind of absence it is
   */
  def openModal(item:Ferie,isMalattia:Boolean=true):Unit
}