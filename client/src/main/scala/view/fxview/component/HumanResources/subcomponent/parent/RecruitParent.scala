package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Zona
import caseclass.CaseClassHttpMessage.Assumi

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by recruit views to make requests to controller
 */
trait RecruitParent {

  /**
   * If recruit button is clicked the controller is asked to save the instance of persona
   *
   * @param persona
   *                instance of assumi. It's the employee to save
   */
  def recruitClicked(persona: Assumi): Unit

  /**
   * If the Zona was choosen the controller is asked the list of terminale
   *
   * @param zona
   *             instance of terminale's Zona to return
   */
  def loadRecruitTerminals(zona: Zona): Unit

}
