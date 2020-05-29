package view.mainviewoperations

import view.baseconfiguration.{BaseTest, LoginViewTest}

trait LoginOperations {
  def login(user:String, pass: String):Unit
  def enterPassword(pass: String): Unit
  def enterUser(user: String): Unit
  def clickButton(): Unit
  def clickModalButton(): Unit
}

object LoginOperations{

  def apply(toTest: BaseTest): LoginOperations = new LoginOperationsImpl(toTest)

  private class LoginOperationsImpl(toTest: BaseTest) extends LoginOperations{


    override def enterPassword(pass: String): Unit = {
      toTest.clickOn("#passwordField")
        .write(pass)
    }

    override def enterUser(user: String): Unit = {
      toTest.clickOn("#usernameField")
        .write(user)
    }

    override def clickButton(): Unit = {
      toTest.clickOn("#loginButton")
    }

    override def login(user: String, pass: String): Unit = {
      enterUser(user)
      enterPassword(pass)
      clickButton()
    }

    override def clickModalButton(): Unit =
      toTest.clickOn("#confirmationButton")

  }
}
