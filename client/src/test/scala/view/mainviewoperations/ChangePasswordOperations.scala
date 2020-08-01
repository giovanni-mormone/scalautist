package view.mainviewoperations

import view.baseconfiguration.BaseTest

import scala.annotation.nowarn

trait ChangePasswordOperations {
  def changePassword(oldPassword: String, newPassword: String, confirmPassword: String): Unit
  def enterOldPassword(password: String): Unit
  def enterNewPassword(password:String):Unit
  def enterConfirmPassword(password:String):Unit
  def clickButton(): Unit
  def clickModalButton():Unit
}

object ChangePasswordOperations{
  def apply(toTest: BaseTest): ChangePasswordOperations = new ChangePasswordOperationsImpl(toTest)

  private class ChangePasswordOperationsImpl(toTest: BaseTest) extends ChangePasswordOperations{

    override def changePassword(oldPassword: String, newPassword: String, confirmPassword: String): Unit ={
      enterOldPassword(oldPassword)
      enterNewPassword(newPassword)
      enterConfirmPassword(confirmPassword)
      clickButton()
    }

    override def enterOldPassword(password: String): Unit = {
      toTest.clickOn("#oldPasswordField")
        .write(password)
    }

    override def enterNewPassword(password: String): Unit = {
      toTest.clickOn("#passwordField")
        .write(password)
    }

    override def enterConfirmPassword(password:String): Unit = {
      toTest.clickOn("#confirmPasswordField")
        .write(password)
    }

    override def clickButton(): Unit =
      toTest.clickOn("#changePasswordButton")

    override def clickModalButton(): Unit =
      toTest.clickOn("#confirmationButton")
  }
}
