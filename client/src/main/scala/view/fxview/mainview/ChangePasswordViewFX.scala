package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.ChangePasswordController
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import view.fxview.component.login.ChangePasswordParent
import view.fxview.{AbstractFXViewWithBack, FXHelperFactory}
import view.fxview.component.login.ChangePasswordBox
import view.fxview.util.ResourceBundleUtil._
import view.mainview.ChangePasswordView

/**
 * @author Giovanni Mormone.
 *
 * FX implementation of [[view.mainview.ChangePasswordView]]
 *
 */
object ChangePasswordViewFX{

  def apply(stage:Stage, scene:Option[Scene]): ChangePasswordView = new ChangePasswordViewFX(stage,scene)

  private class ChangePasswordViewFX(stage:Stage, scene:Option[Scene]) extends AbstractFXViewWithBack(stage,scene) with ChangePasswordView with ChangePasswordParent{
    private var myController: ChangePasswordController = _
    private var changePasswordBox: ChangePasswordBox= _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = ChangePasswordController()
      changePasswordBox = ChangePasswordBox()
      myController.setView(this)
      changePasswordBox.setParent(this)
      pane.getChildren.add(changePasswordBox pane)
    }
    override def changePass(oldPassword:String ,newPassword: String): Unit = {
      pane.getChildren.add(FXHelperFactory.loadingBox)
      changePasswordBox.disable()
      myController.changePassword(oldPassword,newPassword)
    }

    override def errorChange(): Unit ={
      Platform.runLater(() =>{
        stopLoading()
        showMessage(generalResources.getResource("error-change-pass"))
      })
    }

    override def okChange(): Unit = {
      Platform.runLater(() => {
        stopLoading()
        showMessage(generalResources.getResource("good-change-pass"))
        back()
      })
    }

    private def stopLoading(): Unit = {
      pane.getChildren.remove(FXHelperFactory.loadingBox)
      changePasswordBox.enable()
    }

    override def goBack(): Unit =
      Platform.runLater(() => {
        stopLoading()
        back()
      })
  }
}