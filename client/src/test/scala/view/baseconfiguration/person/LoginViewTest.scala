package view.baseconfiguration.person

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import utilstest.StartServer
import view.baseconfiguration.BaseTest
import view.launchview.LoginLaunch
import view.mainviewoperations.LoginOperations

import scala.annotation.nowarn

@RunWith(classOf[JUnitParamsRunner])
class LoginViewTest extends BaseTest with StartServer{

  private val NEW_USER:String = "root"
  private val NEW_USER_PASS:String = "root"

  private var loginView:LoginOperations = _
  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }
  @Before
  def beforeEachLoginTest(): Unit = {
    setUp(classOf[LoginLaunch])
    loginView = LoginOperations(this)
  }

  @Test
  def newUserLogin(): Unit ={
    loginView.login(NEW_USER,NEW_USER_PASS)
    Thread.sleep(4000)
    loginView.clickModalButton()
    Thread.sleep(1000)
    val titleRes: Label = find("#title"): @nowarn
    Thread.sleep(2000)
    assert(titleRes.getText.equals("Cambia Password"))
  }


  @Test
  def newUserLoginModal(): Unit = {
    loginView.login(NEW_USER, NEW_USER_PASS)
    Thread.sleep(4000)
    val msgLabel: Label = find("#messageLabel"): @nowarn
    Thread.sleep(2000)
    assert(msgLabel.getText.equals("È il tuo primo login! Cambia password per poter accedere al programma!"))
    loginView.clickModalButton()
  }

  @Test
  def displayLoading(): Unit = {
    loginView.login(NEW_USER, NEW_USER_PASS)
    ensureEventQueueComplete()
    val loadBox: VBox = find("#loadingBox"): @nowarn
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
    val errorLabel:Label = find("#error"): @nowarn
    assert(errorLabel.isVisible)
  }
}
