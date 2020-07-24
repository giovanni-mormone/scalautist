package view.fxview.component.manager.subcomponent.parent

import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm

trait ChangeSettimanaRichiestaParent {

  /**
   *
   * @param param
   */
  def groupParam(param: ParamsForAlgoritm): Unit

  /**
   *
   */
  def resetWeekParams(): Unit
}
