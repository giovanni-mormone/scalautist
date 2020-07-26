package controller


import utils.Execution
import view.BaseView

import scala.concurrent.ExecutionContextExecutor

/**
 * @author Giovanni Mormone.
 *
 * Basic definition of Controller functionalities.
 * @tparam A
 *           The type of view class that i control. Should be subtype of
 *           [[view.BaseView]]
 *
 */
trait Controller[A<:BaseView] {
  /**
   *Sets the provided view as my controlled view.
   * @param view
   */
  def setView(view:A): Unit
}

/**
 * @author Giovanni Mormone.
 *
 * Template class of the controller.
 * @tparam A
 *           The type of view class that i control. Should be subtype of
 *           [[view.BaseView]]
 *
 */
