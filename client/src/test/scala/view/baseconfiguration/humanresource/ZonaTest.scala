package view.baseconfiguration.humanresource

import javafx.application.Platform
import javafx.scene.control.{Button, Label, TableView}
import org.junit.{After, Before, Test}
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTable
import view.humanresourceoperation.ZonaOperation
import view.launchview.HumanResourceLaunch

class ZonaTest extends BaseTest {

  var zona: ZonaOperation = _

  val nameToSearch: String = "ces"
  val resultOk: String = "Success"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[HumanResourceLaunch])
    zona = ZonaOperation(this)
    zona.openZona()
    ensureEventQueueComplete()
    sleep(7000)
  }

  @After
  def closeStage(): Unit = {
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def newZone(): Unit = {
    zona.newZona()
    ensureEventQueueComplete()
    sleep(2000)
    val text: Label = zona.getMessage
    assert(text.getText.equals(resultOk))
  }

  @Test
  def searchZone(): Unit = {
    zona.searchZona(nameToSearch)
    ensureEventQueueComplete()
    sleep(2000)
    val table: TableView[PersonaTable] = zona.getTable
    assert(table.getItems.size == 2)
  }

  @Test
  def updateZona(): Unit = {
    zona.updateZona()
    ensureEventQueueComplete()
    sleep(500)
    zona.clickUpdate()
    sleep(1500)
    val text: Label = zona.getMessage
    assert(text.getText.equals(resultOk))
  }

  @Test
  def deleteZona(): Unit = {
    zona.updateZona()
    ensureEventQueueComplete()
    sleep(2000)
    zona.clickUpdate()
    sleep(1500)
    val text: Label = zona.getMessage
    assert(text.getText.equals(resultOk))
  }

  @Test
  def notUpdateZona(): Unit = {
    zona.notUpdateZona()
    ensureEventQueueComplete()
    zona.clickNotUpdate()
    ensureEventQueueComplete()
    sleep(1500)
    val updateB: Button = zona.getUpdateButton
    assert(updateB.isDisable)
  }

  @Test
  def notSaveZone(): Unit = {
    val saveB: Button = zona.getSaveButton
    assert(saveB.isDisable)
  }
}
