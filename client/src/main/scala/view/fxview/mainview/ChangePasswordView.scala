package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.ChangePasswordController
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import view.GoBackView
import view.fxview.{AbstractFXViewWithBack, FXHelperFactory}
import view.fxview.component.Login.ChangePasswordBox

/**
 * A view to manage the change of the password functionality.
 * It should be [[view.GoBackView]] to return to the previous view
 * once the password is changed.
 */
trait ChangePasswordView extends GoBackView{

  def errorChange()

  def okChange()
}

/**
 * A ChangePasswordParent is the container of a
 */
trait ChangePasswordParent{
  /**
   * Called to submit the new password chosen by the user. It should be called by a
   * @param oldPassword
   *                    The oldPassword submitted by the user.
   * @param newPassword
   *                    The newPassword submitted by the user.
   */
  def changePass(oldPassword:String, newPassword: String)
}

object ChangePasswordView{

  def apply(stage:Stage, scene:Option[Scene]): ChangePasswordView = new ChangePasswordViewFX(stage,scene)

  private class ChangePasswordViewFX(stage:Stage, scene:Option[Scene]) extends AbstractFXViewWithBack(stage,scene) with ChangePasswordView with ChangePasswordParent{
    private var myController: ChangePasswordController = _
    private var changePasswordBox: ChangePasswordBox= _
    private var errorMsg: String = _
    private var goodChangeMsg: String = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      errorMsg = resources.getString("error-change-pass")
      goodChangeMsg = resources.getString("good-change-pass")
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

    /**
     * Closes the view.
     */
    override def close(): Unit =
      myStage close

    override def errorChange(): Unit ={
      Platform.runLater(() =>{
        stopLoading()
        FXHelperFactory.modalWithMessage(myStage,errorMsg)
      })
    }

    override def okChange(): Unit = {
      Platform.runLater(() => {
        stopLoading()
        FXHelperFactory.modalWithMessage(myStage,goodChangeMsg)
        back()
      })
    }

    private def stopLoading(): Unit = {
      pane.getChildren.remove(FXHelperFactory.loadingBox)
      changePasswordBox.enable()
    }
  }
}