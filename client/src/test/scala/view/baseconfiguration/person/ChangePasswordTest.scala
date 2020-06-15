package view.baseconfiguration.person

import controller.LoginController
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.VBox
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import view.baseconfiguration.BaseTest
import view.launchview.ChangePasswordLaunch
import view.mainviewoperations.ChangePasswordOperations

@RunWith(classOf[JUnitParamsRunner])
class ChangePasswordTest extends BaseTest {
  private val VALID_PASS = "mammaMia1"
  private val OLD_PASS = "root"
  private val INVALID_PASS = "mammamama1"

  private var changeView: ChangePasswordOperations = _

  @Before
  def beforeEachTest(): Unit = {
    LoginController().login("root","root")
    setUp(classOf[ChangePasswordLaunch])
    changeView = ChangePasswordOperations(this)
  }

  @Test
  def goodChange(): Unit = {
    changeView.changePassword(OLD_PASS,VALID_PASS,VALID_PASS)
    ensureEventQueueComplete()
    Thread.sleep(10000)
    val msgLabel:Label = find("#messageLabel")
    assert(msgLabel.getText.equals("Password Cambiata con successo!"))
    changeView.clickModalButton()
  }

  @Test
  def displayLoading(): Unit = {
    changeView.changePassword(OLD_PASS, VALID_PASS, VALID_PASS)
    ensureEventQueueComplete()
    val loadBox: VBox = find("#loadingBox")
    assert(loadBox.isVisible)
  }

  @Test
  def noOldPasswordMessage():Unit = {
    changeView.enterNewPassword(VALID_PASS)
    changeView.enterConfirmPassword(VALID_PASS)
    confirmError()
  }

  @Test
  def noOldPasswordButton():Unit = {
    changeView.enterNewPassword(VALID_PASS)
    changeView.enterConfirmPassword(VALID_PASS)
    changeButtonDisabled()

  }

  @Test
  def noNewPasswordButton():Unit = {
    changeView.enterOldPassword(OLD_PASS)
    changeView.enterConfirmPassword(VALID_PASS)
    changeButtonDisabled()
  }

  @Test
  def noNewPasswordMessage():Unit = {
    changeView.enterOldPassword(OLD_PASS)
    changeView.enterConfirmPassword(VALID_PASS)
    passError()
  }

  @Test
  def noConfirmPasswordButton():Unit = {
    changeView.enterOldPassword(OLD_PASS)
    changeView.enterNewPassword(VALID_PASS)
    changeButtonDisabled()
  }

  @Test
  def noConfirmPasswordMessage():Unit = {
    changeView.enterOldPassword(OLD_PASS)
    changeView.enterNewPassword(VALID_PASS)
    confirmError()
  }

  @Test
  def passwordsNotEquals(): Unit = {
    changeView.enterOldPassword(OLD_PASS)
    changeView.enterNewPassword(VALID_PASS)
    changeView.enterConfirmPassword(INVALID_PASS)
    confirmError()
  }

  private def confirmError(): Unit = {
    val error: Label = find("#confirmError")
    assert(error.isVisible)
  }

  private def passError(): Unit = {
    val error: Label = find("#passError")
    assert(error.isVisible)
  }

  private def changeButtonDisabled(): Unit = {
    val changeButton: Button = find("#changePasswordButton")
    assert(changeButton.isDisable)
  }



  //solo se muore server
  /*@Test
  def badChange(): Unit = {
    changeView.changePassword(OLD_PASS,VALID_PASS,VALID_PASS)
    ensureEventQueueComplete()
    val loadBox: VBox = find("#loadingBox")
    assert(loadBox.isVisible)
    Thread.sleep(10000)
    val msgLabel:Label = find("#messageLabel")
    assert(msgLabel.getText.equals("Password Cambiata con successo!"))
    changeView.clickModalButton()
  }*/
}
