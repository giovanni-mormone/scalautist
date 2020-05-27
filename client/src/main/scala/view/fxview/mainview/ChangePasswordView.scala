package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.ChangePasswordController
import javafx.scene.Scene
import javafx.stage.Stage
import view.GoBackView
import view.fxview.AbstractFXViewWithBack
import view.fxview.component.Login.ChangePasswordBox

/**
 * A view to manage the change of the password functionality.
 * It should be [[view.GoBackView]] to return to the previous view
 * once the password is changed.
 */
trait ChangePasswordView extends GoBackView{
}

/**
 * A ChangePasswordParent is the container of a
 */
trait ChangePasswordParent{
  /**
   * Called to submit the new password chosen by the user. It should be called by a
   * @param newPassword
   */
  def changePass(newPassword: String)
}

object ChangePasswordView{

  def apply(stage:Stage, scene:Option[Scene], userID: Int): ChangePasswordView = new ChangePasswordViewFX(stage,scene,userID)

  private class ChangePasswordViewFX(stage:Stage, scene:Option[Scene], userID: Int) extends AbstractFXViewWithBack(stage,scene) with ChangePasswordView with ChangePasswordParent{
    private var myController: ChangePasswordController = _
    private var changePasswordBox: ChangePasswordBox= _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = ChangePasswordController()
      changePasswordBox = ChangePasswordBox()
      myController.setView(this)
      changePasswordBox.setParent(this)
      pane.setCenter(changePasswordBox pane)
    }
    override def changePass(newPassword: String): Unit =
      myController.changePassword(newPassword, userID)

    /**
     * Closes the view.
     */
    override def close(): Unit =
      myStage close
  }
}