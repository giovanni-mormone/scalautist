package view.fxview.component.login
import view.fxview.util.ResourceBundleUtil._
import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, PasswordField, TextField}
import regularexpressionutilities.PasswordHelper
import view.fxview.component.login.ChangePasswordParent
import view.fxview.component.{AbstractComponent, Component}

/**
 * Component for the ChangePasswordBox contained in a [[view.fxview.component.login.ChangePasswordParent]]
 *
 *  @author Giovanni Mormone.
 */
trait ChangePasswordBox extends Component[ChangePasswordParent]{
}

/**
 * Companion object of the [[view.fxview.component.login.ChangePasswordBox]]
 *
 *  @author Giovanni Mormone.
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
    var goBackButton: Button = _
    @FXML
    var passError: Label = _
    @FXML
    var confirmError: Label = _
    @FXML
    var title: Label = _

    private val regex = PasswordHelper.passwordRegex()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      title.setText(resources.getResource("title"))
      passError.setText(resources.getResource("pass-error-message"))
      confirmError.setText(resources.getResource("confirm-error-message"))
      passwordField.setPromptText(resources.getResource("pass"))
      oldPasswordField.setPromptText(resources.getResource("old-password"))
      confirmPasswordField.setPromptText(resources.getResource("confirm-pass"))
      changePasswordButton.setText(resources.getResource("change-pass"))
      goBackButton.setText(resources.getResource("go-back"))
      confirmError setVisible false
      passError setVisible false
      changePasswordButton setDisable true

      oldPasswordField.textProperty().addListener((_,_,_)=> checkPassword(passwordField.getText))
      passwordField.textProperty().addListener((_,_,newV)=> checkPassword(newV))
      confirmPasswordField.textProperty().addListener((_,_,newV)=>  checkConfirmPassword(newV,passwordField.getText))
      goBackButton.setOnAction(_ => parent.goBack())
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
      case `passText` if regex.matches(passText) && oldPasswordField.getText.length > 0 =>
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