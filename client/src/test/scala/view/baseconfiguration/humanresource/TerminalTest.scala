package view.baseconfiguration.humanresource

import javafx.application.Platform
import javafx.scene.control.{Button, Label, TableView}
import org.junit.{After, Before, Test}
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.TerminalTable
import view.humanresourceoperation.TerminalOperation
import view.launchview.HumanResourceLaunch

class TerminalTest extends BaseTest {

  var terminal: TerminalOperation = _

  val resultOk: String = "Operazione riuscita con successo"

  val newTerminalName: String = "nuovoTerminale"
  val terminalName: String = "Roma"
  val zona: String = "SantGiovanni"

  @Before
  def beforeEachFerieTest(): Unit = {
    setUp(classOf[HumanResourceLaunch])
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
    assert(text.getText.equals(resultOk))
  }

  @Test
  def filterByZone(): Unit = {
    terminal.filterByZona(zona)
    sleep(200)
    val table: TableView[TerminalTable] = terminal.getTable
    assert(table.getItems.size == 3)
  }

  @Test
  def changeTerminal(): Unit = {
    terminal.selectTerminal(terminalName)
    sleep(1000)
    terminal.changeTerminal("Mod")
    terminal.pressUpdate()
    sleep(1000)
    val text: Label = terminal.getMessage
    assert(text.getText.equals("Success"))
  }

  @Test
  def deleteTerminal(): Unit = {
    terminal.selectTerminal(newTerminalName)
    sleep(1000)
    terminal.pressDelete()
    sleep(1000)
    val text: Label = terminal.getMessage
    assert(text.getText.equals("Success"))
  }

  @Test
  def notSaveTerminal(): Unit = {
    val saveB: Button = terminal.getSaveButton
    assert(saveB.isDisable)
  }

  @Test
  def notChange(): Unit = {
    terminal.selectTerminal("Florida")
    sleep(1000)
    terminal.notChange()
    val saveB: Button = terminal.getUpdateButton
    assert(saveB.isDisable)
  }

}
