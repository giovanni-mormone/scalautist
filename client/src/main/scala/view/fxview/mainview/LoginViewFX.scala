package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import view.fxview.util.ResourceBundleUtil._
import controller.LoginController
import javafx.application.Platform
import javafx.stage.Stage
import view.BaseView
import view.fxview.component.login.LoginParent
import view.fxview.{AbstractFXDialogView, FXHelperFactory}
import view.fxview.component.login.LoginBox
import view.mainview.LoginView

/**
 * @author Giovanni Mormone.
 *
 * Companion object of [[view.fxview.mainview.LoginViewFX]]
 *
 */
object LoginViewFX{

  private class LoginViewFX(stage:Stage) extends AbstractFXDialogView(stage) with LoginView with LoginParent {
    private val PREDEF_WIDTH_SIZE: Double = 800
    private val PREDEF_HEIGHT_SIZE: Double = 700
    private var myController:LoginController = _
    private var loginBox:LoginBox = _

    override def close(): Unit =
      myStage.close()


    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
      myController = LoginController()
      loginBox = LoginBox()
      myController.setView(this)
      loginBox.setParent(this)
      pane.getChildren.add(loginBox pane)
      myStage.setResizable(false)
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
        showMessage(generalResources.getResource("first-login"))
        stopLoading()
        loginBox.resetViewFields()
        ChangePasswordViewFX(myStage,Some(myStage.getScene))
      })
    }

    private def setStageDimensions():Unit = {
      stage.setMinWidth(PREDEF_WIDTH_SIZE)
      stage.setMinHeight(PREDEF_HEIGHT_SIZE)
    }

    override def driverAccess(): Unit =
      Platform.runLater(() =>{
        stopLoading()
        myStage.setResizable(true)
        setStageDimensions()
        DriverViewFX(myStage)
      })

    override def humanResourcesAccess(userName: String, userId:String): Unit =
      Platform.runLater(() =>{
        stopLoading()
        myStage.setResizable(true)
        setStageDimensions()
        HumanResourceViewFX(myStage,userName,userId)
      })

    private def stopLoading():Unit = {
      loginBox.enable()
      pane.getChildren.remove(FXHelperFactory.loadingBox)
    }

    override def managerAccess(userName: String, userId:String): Unit = {
      Platform.runLater(() =>{
        stopLoading()
        myStage.setResizable(true)
        setStageDimensions()
        ManagerViewFX(myStage,userName,userId)
      })
    }

    override def result(message: String): Unit = Platform.runLater(()=>{
      this.showMessageFromKey(message)
      stopLoading()
    })
  }

  def apply(stage:Stage):LoginView = new LoginViewFX(stage)
}
