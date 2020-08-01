package view.baseconfiguration.humanresource

import java.time.LocalDate

import javafx.scene.control.{Button, Label}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import utilstest.StartServer
import view.baseconfiguration.BaseTest
import view.humanresourceoperation.MalattieOperation
import view.launchview.HumanResourceLaunch

import scala.annotation.nowarn

@RunWith(classOf[JUnitParamsRunner])
class MalattieTest extends BaseTest with StartServer{
  val textOk:String = "Malattia Inserite Correttamente"
  var malattie:MalattieOperation = _
  val date:LocalDate =LocalDate.of(2020,6,12)
  val dateF:LocalDate =LocalDate.of(2020,6,15)

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[HumanResourceLaunch])
    login()
    malattie = MalattieOperation(this)
  }
  @After
  def closeStage():Unit={
    closeCurrentWindow()
  }

  private def login()={
    val user: String = "risuma"
    val password: String = "rootrootN2"

    clickOn("#usernameField")
    write(user)
    clickOn("#passwordField")
    write(password)
    clickOn("#loginButton")
    sleep(10000)
  }

  @Test
  def goodInsertAbsence(): Unit = {

    malattie.clickButtonIllness()
    ensureEventQueueComplete()
    sleep(5000)
    malattie.clickTable()
    sleep(3000)
    malattie.enterFirstDate(date)
    malattie.enterSecondDate(dateF)
    malattie.clickModalButton()
    sleep(5000)
    val msgLabel:Label = find("#messageLabel"): @nowarn
    assert(msgLabel.getText.equals(textOk))

  }
  @Test
  def notSaveAbsenceWithoutDate(): Unit = {
    malattie.clickButtonIllness()
    ensureEventQueueComplete()
    sleep(4000)
    malattie.clickTable()
    sleep(3000)
    malattie.clickModalButton()
    val modalButton:Button = find("#button"): @nowarn
    assert(modalButton.isDisable)

  }
  @Test
  def notSaveAbsenceWithoutSecondDate(): Unit = {
    malattie.clickButtonIllness()
    ensureEventQueueComplete()
    sleep(4000)
    malattie.clickTable()
    sleep(3000)
    malattie.enterFirstDate(date)
    malattie.clickModalButton()
    val modalButton:Button = find("#button"): @nowarn
    assert(modalButton.isDisable)

  }
}
