package view.baseconfiguration

import javafx.application.Application
import javafx.scene.Node
import javafx.scene.input.{KeyCode, MouseButton}
import javafx.stage.Stage
import org.junit.{After, BeforeClass}
import org.testfx.api.FxToolkit.registerPrimaryStage
import org.testfx.framework.junit.ApplicationTest
import org.testfx.util.WaitForAsyncUtils

import scala.annotation.nowarn

class BaseTest extends ApplicationTest{

  protected var myStage:Stage = _
  def setUp(t:Class[_<:Application]):Unit = {
    ApplicationTest.launch(t)
  }

  override def start(stage: Stage): Unit = {
    myStage = stage
    stage.show()
  }

  @After
  def afterEachTest(): Unit = {
    release(KeyCode.ENTER)
    release(MouseButton.PRIMARY)
  }

  def ensureEventQueueComplete():Unit = WaitForAsyncUtils.waitForFxEvents(2)

  def find[T<:Node](query:String): T =
    lookup(query).queryAll().iterator().next().asInstanceOf[T]: @nowarn//metodo find non capito bene da scala
}
