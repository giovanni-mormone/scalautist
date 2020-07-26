package controller

import utils.Execution
import view.BaseView

import scala.concurrent.ExecutionContextExecutor

abstract class AbstractController[A<:BaseView] extends Controller[A]{

  /**
   * The view that i control.
   */
  protected var myView:A = _
  implicit protected val execution: ExecutionContextExecutor = Execution.executionContext
  override def setView(view: A): Unit =
    myView = view
}
