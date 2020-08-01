package controller

<<<<<<< HEAD
trait Controller {

=======
<<<<<<< HEAD

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
=======
trait Controller {

>>>>>>> develop
}
>>>>>>> 1ba3c3074a7ddf4f6e65bc964b56d7e086f29836
