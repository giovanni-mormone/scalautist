package view.baseconfiguration

import java.time.LocalDate

import javafx.application.Platform
import javafx.scene.control.{Button, Label}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import view.driverviewoperations.FerieOperation
import view.launchview.HumanResourceLaunch

@RunWith(classOf[JUnitParamsRunner])
class FerieTest extends BaseTest {
  val textOk:String = "Ferie Assegnate Correttamente"
  var ferie:FerieOperation = _
  val date:LocalDate =LocalDate.of(2020,6,12)
  val dateF:LocalDate =LocalDate.of(2020,6,15)

  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[HumanResourceLaunch])
    ferie = FerieOperation(this)
  }
  @After
  def closeStage():Unit={
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def goodInsertFerie(): Unit = {
    ferie.clickButtonHoliday()
    ensureEventQueueComplete()
    sleep(5000)
    ferie.clickTable()
    sleep(3000)
    ferie.enterFirstDate(date)
    ferie.enterSecondDate(dateF)
    ferie.clickModalButton()
    sleep(5000)
    val msgLabel:Label = find("#messageLabel")
    assert(msgLabel.getText.equals(textOk))

  }
  @Test
  def notSaveAbsenceWithoutDate(): Unit = {
    ferie.clickButtonHoliday()
    ensureEventQueueComplete()
    sleep(4000)
    ferie.clickTable()
    sleep(3000)
    ferie.clickModalButton()
    val modalButton:Button = find("#button")
    assert(modalButton.isDisable)

  }
  @Test
  def notSaveAbsenceWithoutSecondDate(): Unit = {
    ferie.clickButtonHoliday()
    ensureEventQueueComplete()
    sleep(4000)
    ferie.clickTable()
    sleep(3000)
    ferie.enterFirstDate(date)
    ferie.clickModalButton()
    val modalButton:Button = find("#button")
    assert(modalButton.isDisable)

  }

}
