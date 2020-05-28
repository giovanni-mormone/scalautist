package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.LoginController
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.stage.{Modality, Stage}
import view.BaseView
import view.fxview.{AbstractFXView, FXHelperFactory}
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
  def firstUserAccess(userID:Int): Unit
}

/**
 * A LoginParent is the container of a [[view.fxview.component.Login.LoginBox]] [[view.fxview.component.Component]]
 */
trait LoginParent{
  /**
   * Method called to submit user credentials. It should be called by a [[view.fxview.component.Login.LoginBox]]
   * @param username
   *                 The username of the user.
   * @param password
   *                 The password of the user.
   */
  def login(username: String, password: String): Unit
}

/**
 * Companion object of [[view.fxview.mainview.LoginView]]
 */
object LoginView{

  private class LoginViewFX(stage:Stage) extends AbstractFXView(stage) with LoginView with LoginParent {
    private var myController:LoginController = _
    private var loginBox:LoginBox = _
    private var firstLoginMessage:String = _

    override def close(): Unit =
      myStage close


    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
      firstLoginMessage = resources.getString("first-login")
      myController = LoginController()
      loginBox = LoginBox()
      myController.setView(this)
      loginBox.setParent(this)
      pane.getChildren.add(loginBox pane)
    }

    override def login(username: String, password: String): Unit = {
      loginBox.disable()
      pane.getChildren.add(FXHelperFactory.loadingBox)
      myController.login(username, password)
    }

    override def badLogin(): Unit = {
      Platform.runLater(()=>{
        loginBox.enable()
        pane.getChildren.remove(FXHelperFactory.loadingBox)
        loginBox.showErrorMessage()
      })
    }

    override def firstUserAccess(userID: Int): Unit = {
      FXHelperFactory.modalWithMessage(myStage, firstLoginMessage).show()
      loginBox.resetViewFields()
      ChangePasswordView(myStage,Some(myStage.getScene),userID)
    }
  }

  def apply(stage:Stage):LoginView = new LoginViewFX(stage)
}
