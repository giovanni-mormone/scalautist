package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.ChangePasswordController
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import view.GoBackView
import view.fxview.component.login.ChangePasswordParent
import view.fxview.{AbstractFXViewWithBack, FXHelperFactory}
import view.fxview.component.login.ChangePasswordBox

/**
 * @author Giovanni Mormone.
 *
 * A view to manage the change of the password functionality.
 * It should be [[view.GoBackView]] to return to the previous view
 * once the password is changed.
 */
trait ChangePasswordView extends GoBackView{

  def errorChange()

  def okChange()
}

/**
 * @author Giovanni Mormone.
 *
 * Companion object of [[view.fxview.mainview.ChangePasswordView]]
 *
 */
object ChangePasswordView{

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

    override def close(): Unit =
      myStage close

    override def errorChange(): Unit ={
      Platform.runLater(() =>{
        stopLoading()
        showMessage(generalResources.getString("error-change-pass"))
      })
    }

    override def okChange(): Unit = {
      Platform.runLater(() => {
        stopLoading()
        showMessage(generalResources.getString("good-change-pass"))
        back()
      })
    }

    private def stopLoading(): Unit = {
      pane.getChildren.remove(FXHelperFactory.loadingBox)
      changePasswordBox.enable()
    }
  }
}