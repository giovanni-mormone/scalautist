package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.LoginController
import javafx.stage.Stage
import view.BaseView
import view.fxview.AbstractFXView
import view.fxview.component.Login.LoginBox

/**
 * A view to manage login functionalities.
 * It extends [[view.BaseView]]
 */
trait LoginView extends BaseView{
  /**
   * Method that shows a message of error in case of a bad login(e.g. wrong username or password)
   */
  def badLogin():Unit
}

/**
 * A LoginParent is the container of a [[view.fxview.component.Login.LoginBox]] [[view.fxview.component.Component]]
 */
trait LoginParent{
  def login(username: String, password: String): Unit
}

/**
 * Companion object of [[view.fxview.mainview.LoginView]]
 */
object LoginView{

  private class LoginViewFX(stage:Stage) extends AbstractFXView(stage) with LoginView with LoginParent {
    private var myController:LoginController = _
    private var loginBox:LoginBox = _

    override def show(): Unit =
      myStage show

    override def hide(): Unit =
      myStage hide

    override def close(): Unit =
      myStage close

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
      myController = LoginController()
      loginBox = LoginBox()
      myController.setView(this)
      loginBox.setParent(this)
      pane.setCenter(loginBox.pane())
    }

    override def login(username: String, password: String): Unit =
      myController.login(username, password)

    override def badLogin(): Unit =
      loginBox.showErrorMessage()
  }

  def apply(stage:Stage):LoginView = new LoginViewFX(stage)
}
