package controller

import model.entity.PersonaModel
import view.fxview.mainview.LoginView

import scala.util.{Failure, Success}

/**
 * A login controller for a view of type [[view.fxview.mainview.LoginView]]
 */
trait LoginController extends AbstractController[LoginView]{
  /**
   * Tries to login a user in the system. Gets the params submitted by a [[view.fxview.mainview.LoginView]]
   * and tells to the view to change accordingly.
   * @param username
   *                 The username of the user to login.
   * @param password
   *                 The password of the user to login.
   */
  def login(username: String, password: String)
}

/**
 * Companion object of [[controller.LoginController]]
 */
object LoginController {
  private val instance = new LoginControllerImpl()
  private val myModel = PersonaModel()

  def apply(): LoginController = instance

  private class LoginControllerImpl extends LoginController{

    override def login(username: String, password: String): Unit = (username,password) match{
      case (s1,s2) if s1.trim.length == 0 || s2.trim.length == 0 => myView.badLogin
      case _ => {
        myModel.login(username,password).onComplete{
          case Success(t) => t match {
            case Some(user) =>
              Utils.username = user.userName
              Utils.userId = user.matricola.head
            case None => myView.badLogin
          }
          case Failure(exception) => println(exception)
        }
      }
    }
  }
}
