package controller

import caseclass.CaseClassDB.Persona
import caseclass.CaseClassHttpMessage.Response
import model.entity.PersonaModel
import view.fxview.mainview.LoginView

import scala.util.{Failure, Success}

/**
 * @author Giovanni Mormone.
 *
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
  def login(username: String, password: String): Unit
}

/**
 * @author Giovanni Mormone.
 *
 * Companion object of [[controller.LoginController]]
 *
 */
object LoginController {
  private val instance = new LoginControllerImpl()
  private val myModel = PersonaModel()

  def apply(): LoginController = instance

  private class LoginControllerImpl extends LoginController {

    override def login(username: String, password: String): Unit = (username, password) match {
      case (s1, s2) if s1.trim.length == 0 || s2.trim.length == 0 => myView.badLogin()
      case _ =>
        myModel.login(username, password).onComplete {
          case Success(Response(_, persona)) => checkLoginResult(persona)
          case Failure(_) => myView.result("GeneralError-ConnectionFail")
        }
    }

    private val checkLoginResult: Option[Persona] => Unit = {
      case Some(user) if user.isNew =>
        storeLoginData(user.matricola, Some(user.userName))
        myView.firstUserAccess()
      case Some(user) if user.ruolo == 1 =>
        storeLoginData(user.matricola, Some(user.userName))
        user.matricola.foreach(x=> myView.managerAccess(user.userName,x.toString))
      case Some(user) if user.ruolo == 2 =>
        storeLoginData(user.matricola, Some(user.userName))
        user.matricola.foreach(x=> myView.humanResourcesAccess(user.userName,x.toString))
      case Some(user) if user.ruolo == 3 =>
        storeLoginData(user.matricola, Some(user.userName))
        myView.driverAccess()
      case _ => myView.badLogin()
    }
  }

  private def storeLoginData(userId: Option[Int], userName: Option[String]): Unit = {
    Utils.username = userName
    Utils.userId = userId
  }
}
