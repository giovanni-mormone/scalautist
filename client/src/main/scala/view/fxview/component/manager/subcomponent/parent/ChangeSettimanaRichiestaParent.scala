package view.fxview.component.manager.subcomponent.parent

import view.fxview.component.manager.subcomponent.util.ParamsForAlgorithm

/**
 * @author Francesco Cassano
 * This trait allow to send request to main view from [[view.fxview.component.manager.subcomponent.ChangeSettimanaRichiesta]]
 */
trait ChangeSettimanaRichiestaParent {

  /**
   * notify to draw next panel
   * @param param instance of [[ParamsForAlgorithm]] that contains parameters chosen until this view
   */
  def groupParam(param: ParamsForAlgorithm): Unit

  /**
   * notify to delete all params and restart the process of choose
   */
  def resetWeekParams(): Unit
}
