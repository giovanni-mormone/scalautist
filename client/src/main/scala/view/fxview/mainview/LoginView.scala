package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.LoginController
import javafx.application.Platform
import javafx.stage.Stage
import view.BaseView
import view.fxview.{AbstractFXDialogView, FXHelperFactory}
import view.fxview.component.login.LoginBox

/**
 * @author Giovanni Mormone.
 *
 * A view to manage login functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait LoginView extends BaseView{
  /**
   * Method that shows a message of error in case of a bad login(e.g. wrong username or password)
   */
  def badLogin():Unit

  /**
   * Method called to notify a login by a user not validated. it opens a
   * [[view.fxview.mainview.ChangePasswordView]].
   *
   */
  def firstUserAccess(): Unit
}

/**
 * @author Giovanni Mormone.
 *
 *         A LoginParent is the container of a [[view.fxview.component.login.LoginBox]] [[view.fxview.component.Component]]
 *
 */
trait LoginParent{
  /**
   * Method called to submit user credentials. It should be called by a [[view.fxview.component.login.LoginBox]]
 *
   * @param username
   *                 The username of the user.
   * @param password
   *                 The password of the user.
   */
  def login(username: String, password: String): Unit
}

/**
 * @author Giovanni Mormone.
 *
 * Companion object of [[view.fxview.mainview.LoginView]]
 *
 */
object LoginView{

  private class LoginViewFX(stage:Stage) extends AbstractFXDialogView(stage) with LoginView with LoginParent {
    private var myController:LoginController = _
    private var loginBox:LoginBox = _

    override def close(): Unit =
      myStage close


    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
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
        stopLoading()
        loginBox.showErrorMessage()
      })
    }

    override def firstUserAccess(): Unit = {
      Platform.runLater(() => {
        showMessage(generalResources.getString("first-login"))
        stopLoading()
        loginBox.resetViewFields()
        ChangePasswordView(myStage,Some(myStage.getScene))
      })
    }

    private def stopLoading():Unit = {
      loginBox.enable()
      pane.getChildren.remove(FXHelperFactory.loadingBox)
    }
  }

  def apply(stage:Stage):LoginView = new LoginViewFX(stage)
}
