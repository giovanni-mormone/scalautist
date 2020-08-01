package view.baseconfiguration.manager

import javafx.application.Platform
import javafx.scene.control.{Button, Label, TableView}
import org.junit.{After, Before, Test}
import utilstest.StartServer3
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.TerminalTable
import view.humanresourceoperation.TerminalOperation
import view.launchview.ManagerLaunch
import view.mainviewoperations.ManagerOperations

class TerminalTest extends BaseTest with StartServer3{

  var terminal: TerminalOperation = _
  var managerOperations: ManagerOperations = _

  val resultOk: String = "Operazione riuscita con successo"

  val newTerminalName: String = "nuovoTerminale"
  val terminalName: String = "Roma"
  val zona: String = "SantGiovanni"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[ManagerLaunch])
    managerOperations = ManagerOperations(this)
    terminal = TerminalOperation(this)
    terminal.openTerminal()
    ensureEventQueueComplete()
    sleep(7000)
  }

  @After
  def closeStage(): Unit = {
    Platform.runLater(()=>myStage.close())
  }

  @Test
  def newTerminal(): Unit = {
    terminal.clickTextBox(newTerminalName)
    sleep(100)
    terminal.selectZona(zona)
    sleep(200)
    terminal.pressAdd()
    sleep(1000)
    val text: Label = terminal.getMessage
    assert(text.getText.contains("success"))
  }

  @Test
  def filterByZone(): Unit = {
    terminal.filterByZona(zona)
    sleep(200)
    val table: TableView[TerminalTable] = terminal.getTable
    assert(table.getItems.size == 1)
  }

  @Test
  def changeTerminal(): Unit = {
    terminal.selectTerminal(terminalName)
    sleep(3000)
    terminal.changeTerminal("Cesena")
    terminal.pressUpdate()
    sleep(3000)
    val text: Label = terminal.getMessage
    assert(text.getText.contains("success"))
  }

  @Test
  def deleteTerminal(): Unit = {
    terminal.selectTerminal("Roma")
    sleep(1000)
    terminal.pressDelete()
    sleep(3000)
    val text: Label = terminal.getMessage
    assert(text.getText.contains("success"))
  }

  @Test
  def notSaveTerminal(): Unit = {
    val saveB: Button = terminal.getSaveButton
    assert(saveB.isDisable)
  }

  @Test
  def notChange(): Unit = {
    terminal.selectTerminal("Florida")
    ensureEventQueueComplete()
    sleep(5000)
    terminal.notChange()
    ensureEventQueueComplete()
    sleep(1000)
    val saveB: Button = terminal.getUpdateButton
    assert(saveB.isDisable)
  }

}
