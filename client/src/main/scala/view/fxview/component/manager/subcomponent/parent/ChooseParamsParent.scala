package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.Parametro

/**
 * @author Francesco Cassano
 * This trait allow to send request to main view
 */
trait ChooseParamsParent {

  /**
   * Method to rn the algorithm
   * @param params
   */
  def calculateShifts(params: Parametro, save: Boolean): Unit
}
