package controller

import view.fxview.mainview.LoginView

/**
 * A login controller for a view of type [[view.fxview.mainview.LoginView]]
 */
trait LoginController extends AbstractController[LoginView]{
  def login(username: String, password: String)
}

/**
 * Companion object of [[controller.LoginController]]
 */
object LoginController {
  private val instance = new LoginControllerImpl()

  def apply(): LoginController = instance

  private class LoginControllerImpl extends LoginController{
    override def login(username: String, password: String): Unit = (username,password) match{
      case (s1,s2) if s1.trim.length == 0 || s2.trim.length == 0 => myView.badLogin
      case _ => println(username,password)
    }
  }
}
