package view.baseconfiguration.humanresource

import javafx.application.Platform
import javafx.scene.control.TextField
import junitparams.JUnitParamsRunner
import org.junit.runner.RunWith
import org.junit.{After, Before, Test}
import org.scalacheck.Prop.True
import view.baseconfiguration.BaseTest
import view.humanresourceoperation.FireOperation
import view.launchview.HumanResourceLaunch

@RunWith(classOf[JUnitParamsRunner])
class LicenziaTest extends BaseTest {

  var licenzia: FireOperation = _

  val nameToSearch: String = "f"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[HumanResourceLaunch])
    licenzia = FireOperation(this)
    licenzia.openFireBox()
    ensureEventQueueComplete()
    sleep(7000)
  }

  @After
  def closeStage(): Unit = {
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def checkOne(): Unit = {
    licenzia.fireOne()
    ensureEventQueueComplete()
    sleep(2000)
    assert(true)
  }

}
