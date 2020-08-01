package view.baseconfiguration.manager

import javafx.application.Platform
import javafx.scene.control.{Button, Label, TableView}
import org.junit.{After, Before, Test}
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTable
import view.humanresourceoperation.ZonaOperation
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations

class ZonaTest extends BaseTest with StartServer3{

  var zona: ZonaOperation = _
  var managerOperations: ManagerOperations = _

  val nameToSearch: String = "ces"
  val resultOk: String = "success"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[ManagerLaunch])
    managerOperations = ManagerOperations(this)
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
    assert(text.getText.contains(resultOk))
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
    sleep(1500)
    zona.clickUpdate()
    sleep(1500)
    val text: Label = zona.getMessage
    assert(text.getText.contains(resultOk))
  }

  @Test
  def deleteZona(): Unit = {
    zona.updateZona()
    ensureEventQueueComplete()
    sleep(2000)
    zona.clickUpdate()
    sleep(1500)
    val text: Label = zona.getMessage
    assert(text.getText.contains(resultOk))
  }

  @Test
  def notUpdateZona(): Unit = {
    zona.notUpdateZona()
    ensureEventQueueComplete()
    sleep(2000)
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
