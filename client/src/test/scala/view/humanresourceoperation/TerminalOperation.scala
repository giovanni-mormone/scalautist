package view.humanresourceoperation

import javafx.scene.control.{Button, Label, TableView, TextField}
import javafx.scene.input.KeyCode
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.TerminalTable

trait TerminalOperation {
  def openTerminal(): Unit
  def filterByZona(zona: String): Unit
  def selectTerminal(terminal: String): Unit
  def selectZona(zona: String): Unit
  def clickTextBox(write: String): Unit
  def changeTerminal(terminal: String): Unit
  def notChange(): Unit
  def pressDelete(): Unit
  def pressUpdate(): Unit
  def pressAdd(): Unit
  def getSaveButton: Button
  def getUpdateButton: Button
  def getTable: TableView[TerminalTable]
  def getMessage: Label
}

object TerminalOperation {

  def apply(toTest: BaseTest): TerminalOperation = new TerminalOperationImpl(toTest)

  private class TerminalOperationImpl(test: BaseTest) extends TerminalOperation {

    private val filter: String = "#searchBox"
    private val table: String = "#terminalTable"
    private val terminalZone: String = "#addBox"
    private val terminalName: String = "#newName"
    private val terminalAdd: String = "#terminalButton"
    private val updateButton: String = "#update"
    private val deleteButton: String = "#delete"
    private val changeTerminal: String = "#name"
    private val changeZone: String = "#zones"

    override def openTerminal(): Unit =
      test.clickOn("#terminalManger")

    override def filterByZona(zona: String): Unit = {
      test.clickOn(filter)
      test.ensureEventQueueComplete()
      test.clickOn(zona)
      test.ensureEventQueueComplete()
    }

    override def selectTerminal(terminal: String): Unit = {
      test.doubleClickOn(terminal)
      test.ensureEventQueueComplete()
    }

    override def selectZona(zona: String): Unit = {
      test.clickOn(terminalZone)
      test.ensureEventQueueComplete()
      test.clickOn(zona)
      test.ensureEventQueueComplete()
    }

    override def clickTextBox(write: String): Unit = {
      test.clickOn(terminalName).write(write)
      test.ensureEventQueueComplete()
    }

    override def changeTerminal(terminal: String): Unit = {
      test.clickOn(changeTerminal).write(terminal)
      test.ensureEventQueueComplete()
    }

    override def notChange(): Unit = {
      test.clickOn(changeTerminal)
      val text: TextField = test.find(changeTerminal)
      while(!text.getText.equals("")) {
        test.press(KeyCode.BACK_SPACE)
        test.release(KeyCode.BACK_SPACE)
      }
    }

    override def pressDelete(): Unit = {
      test.clickOn(deleteButton)
      test.ensureEventQueueComplete()
    }

    override def pressUpdate(): Unit = {
      test.clickOn(updateButton)
      test.ensureEventQueueComplete()
    }

    override def pressAdd(): Unit = {
      test.clickOn(terminalAdd)
      test.ensureEventQueueComplete()
    }

    override def getSaveButton: Button =
      test.find(terminalAdd)

    override def getUpdateButton: Button =
      test.find(updateButton)

    override def getTable: TableView[TerminalTable] =
      test.find(table)

    override def getMessage: Label =
      test.find("#messageLabel")
  }
}