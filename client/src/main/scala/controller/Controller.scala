package controller

import java.util.concurrent.Executors

import view.BaseView

import scala.concurrent.ExecutionContext

/**
 * Basic definition of Controller functionalities.
 * @tparam A
 *           The type of view class that i control. Should be subtype of
 *           [[view.BaseView]]
 */
trait Controller[A<:BaseView] {
  /**
   *Sets the provided view as my controlled view.
   * @param view
   */
  def setView(view:A): Unit
}

/**
 * Template class of the controller.
 * @tparam A
 *           The type of view class that i control. Should be subtype of
 *           [[view.BaseView]]
 */
abstract class AbstractController[A<:BaseView] extends Controller[A]{
  /**
   * The view that i control.
   */
  protected var myView:A = _
  implicit val executionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor)

  override def setView(view: A): Unit =
    myView = view
}
