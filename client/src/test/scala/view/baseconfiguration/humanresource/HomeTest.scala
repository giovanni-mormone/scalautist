package view.baseconfiguration.humanresource

import javafx.application.Platform
import javafx.scene.layout.{BorderPane, VBox}
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import view.baseconfiguration.BaseTest
import view.launchview.HumanResourceLaunch
import view.mainviewoperations.HumanResourceOperations

@RunWith(classOf[JUnitParamsRunner])
class HomeTest extends BaseTest {

  var home: HumanResourceOperations = _

  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[HumanResourceLaunch])
    home = HumanResourceOperations(this)
  }

  @After
  def closeStage():Unit={
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def goodOpenRecruit(): Unit = {
    home.openRescruit()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenFire(): Unit = {
    home.openFire()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenSick(): Unit = {
    home.openSick()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenHoliday(): Unit = {
    home.openHoliday()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenZone(): Unit = {
    home.openZona()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenTerminal(): Unit = {
    home.openTerminal()
    ensureEventQueueComplete()
    sleep(7000)
    val homePanel: BorderPane = find("#baseHR")
    assert(homePanel.getCenter.isVisible)
  }

  @Test
  def goodOpenChangePassword(): Unit = {
    home.openChangePassword()
    ensureEventQueueComplete()
    sleep(2000)
    val changePassword: VBox = find("#changepasswordbox")
    assert(changePassword.isVisible)
  }
}
