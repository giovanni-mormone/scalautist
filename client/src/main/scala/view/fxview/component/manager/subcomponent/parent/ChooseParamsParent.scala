package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.{Parametro, Zona}

/**
 * @author Francesco Cassano
 * This trait allow to send request to main view
 */
trait ChooseParamsParent {

  /**
   * Method to run the algorithm
   *
   * @param params
   *               That are the params the algorithm needs to run
   * @param save
   *             Boolean to save params and reuse that in future
   */
  def calculateShifts(params: Parametro, save: Boolean): Unit

  /**
   * Method allows to draw the modal for choosing params
   */
  def modalOldParam(): Unit

}
