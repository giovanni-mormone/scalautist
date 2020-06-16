package view.baseconfiguration.person

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import view.baseconfiguration.BaseTest
import view.launchview.LoginLaunch
import view.mainviewoperations.LoginOperations

@RunWith(classOf[JUnitParamsRunner])
class LoginViewTest extends BaseTest {

  private val NEW_USER:String = "root"
  private val NEW_USER_PASS:String = "root"

  private var loginView:LoginOperations = _

  @Before
  def beforeEachLoginTest(): Unit = {
    setUp(classOf[LoginLaunch])
    loginView = LoginOperations(this)
  }

  @Test
  def newUserLogin(): Unit ={
    loginView.login(NEW_USER,NEW_USER_PASS)
    Thread.sleep(25000)
    loginView.clickModalButton()
    Thread.sleep(1000)
    val titleRes: Label = find("#title")
    assert(titleRes.getText.equals("Cambia Password"))
  }


  @Test
  def newUserLoginModal(): Unit = {
    loginView.login(NEW_USER, NEW_USER_PASS)
    Thread.sleep(12000)
    val msgLabel: Label = find("#messageLabel")
    assert(msgLabel.getText.equals("È il tuo primo login! Cambia password per poter accedere al programma!"))
    loginView.clickModalButton()
  }

  @Test
  def displayLoading(): Unit = {
    loginView.login(NEW_USER, NEW_USER_PASS)
    ensureEventQueueComplete()
    val loadBox: VBox = find("#loadingBox")
    assert(loadBox.isVisible)
  }
  @Test
  def noUserName(): Unit = {
    loginView.enterPassword(NEW_USER_PASS)
    loginView.clickButton()
    credentialError()
  }

  @Test
  def noPassword(): Unit = {
    loginView.enterUser(NEW_USER)
    loginView.clickButton()
    credentialError()
  }

  @Test
  def emptyPass(): Unit = {
    loginView.login(NEW_USER," ")
    credentialError()
  }

  @Test
  def emptyUserName(): Unit = {
    loginView.login(" ",NEW_USER_PASS)
    credentialError()
  }

  private def credentialError(): Unit = {
    ensureEventQueueComplete()
    val errorLabel:Label = find("#error")
    assert(errorLabel.isVisible)
  }
}
