package view.fxview.component.Login

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, PasswordField, TextField}
import javax.swing.event.ChangeListener
import passwordutilities.PasswordHelper
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.mainview.ChangePasswordParent

/**
 * Component for the ChangePasswordBox contained in a [[view.fxview.mainview.ChangePasswordParent]]
 */
trait ChangePasswordBox extends Component[ChangePasswordParent]{
}

/**
 * Companion object of the [[view.fxview.component.Login.ChangePasswordBox]]
 */
object ChangePasswordBox{

  def apply(): ChangePasswordBox = new ChangePasswordBoxImpl()

  private class ChangePasswordBoxImpl extends AbstractComponent[ChangePasswordParent]("login/ChangePasswordBox") with ChangePasswordBox{

    @FXML
    var passwordField: PasswordField = _
    @FXML
    var oldPasswordField:PasswordField = _
    @FXML
    var confirmPasswordField: PasswordField = _
    @FXML
    var changePasswordButton: Button = _
    @FXML
    var passError: Label = _
    @FXML
    var confirmError: Label = _
    @FXML
    var title: Label = _

    private val regex = PasswordHelper.passwordRegex()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      title.setText(resources.getString("title"))
      passError.setText(resources.getString("pass-error-message"))
      confirmError.setText(resources.getString("confirm-error-message"))
      passwordField.setPromptText(resources.getString("pass"))
      oldPasswordField.setPromptText(resources.getString("old-password"))
      confirmPasswordField.setPromptText(resources.getString("confirm-pass"))
      changePasswordButton.setText(resources.getString("change-pass"))
      confirmError setVisible false
      passError setVisible false
      changePasswordButton setDisable true

      passwordField.textProperty().addListener((_,_,newV)=> checkPassword(newV))
      confirmPasswordField.textProperty().addListener((_,_,newV)=>  checkConfirmPassword(newV,passwordField.getText))

      changePasswordButton.setOnAction(_ => parent.changePass(oldPasswordField getText ,confirmPasswordField getText))
    }

    private def checkPassword(text: String): Unit = regex.matches(text) match {
      case false =>
        passError setVisible true
        disableButton(true)
      case _ =>
        passError setVisible false
        checkConfirmPassword(confirmPasswordField.getText,text)
    }

    private def checkConfirmPassword(confirmText:String,passText: String): Unit = confirmText match{
      case `passText` if regex.matches(passText) =>
        confirmError setVisible false
        disableButton(false)
      case _ =>
        confirmError setVisible true
        disableButton(true)
    }

    private def disableButton(usable: Boolean): Unit =
      changePasswordButton setDisable usable

  }
}
