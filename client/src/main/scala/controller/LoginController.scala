package controller

import view.fxview.mainview.LoginView

/**
 * A login controller for a view of type [[view.fxview.mainview.LoginView]]
 */
trait LoginController extends AbstractController[LoginView]{
}

/**
 * Companion object of [[controller.LoginController]]
 */
object LoginController {
  private val instance = new LoginControllerImpl()

  def apply(): LoginController = instance

  private class LoginControllerImpl extends LoginController{
  }
}
