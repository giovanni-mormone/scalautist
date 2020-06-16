package view.humanresourceoperation

import javafx.scene.control.{Button, Label, TableView, TextField}
import javafx.scene.input.KeyCode
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTable

trait ZonaOperation {
  def openZona(): Unit
  def updateZona(): Unit
  def notUpdateZona(): Unit
  def deleteZona(): Unit
  def newZona(): Unit
  def searchZona(string: String): Unit
  def clickUpdate(): Unit
  def clickNotUpdate(): Unit
  def clickDelete(): Unit
  def getMessage: Label
  def getTable: TableView[PersonaTable]
  def getUpdateButton: Button
  def getSaveButton: Button
}

object ZonaOperation {

  def apply(toTest: BaseTest): ZonaOperation = new ZonaOperationImpl(toTest)

  private class ZonaOperationImpl(toTest: BaseTest) extends ZonaOperation {

    private val tableId: String = "#zonaTable"
    private val searchId: String = "#searchBox"
    private val addButtonId: String = "#zonaButton"
    private val addZoneId: String = "#newName"
    private val nameZonaId: String = "#name"
    private val updateId: String = "#update"
    private val deleteId: String = "#delete"

    override def updateZona(): Unit = {
      toTest.doubleClickOn("Cesena")
      toTest.ensureEventQueueComplete()
    }

    override def notUpdateZona(): Unit = {
      toTest.doubleClickOn("Savignano")
      toTest.ensureEventQueueComplete()
    }

    override def deleteZona(): Unit = {
      toTest.doubleClickOn("Cesenatico")
      toTest.ensureEventQueueComplete()
    }

    override def newZona(): Unit = {
      toTest.clickOn(addZoneId).write("nuovaZona")
      toTest.ensureEventQueueComplete()
      toTest.clickOn(addButtonId)
      toTest.ensureEventQueueComplete()
    }

    override def searchZona(string: String): Unit = {
      toTest.clickOn(searchId).write(string)
      toTest.ensureEventQueueComplete()
    }

    override def clickUpdate(): Unit = {
      toTest.clickOn(nameZonaId).write("Mod")
      toTest.ensureEventQueueComplete()
      toTest.clickOn(updateId)
      toTest.ensureEventQueueComplete()
    }

    override def clickNotUpdate(): Unit = {
      toTest.clickOn(nameZonaId)
      val text: TextField = toTest.find(nameZonaId)
      while(!text.getText.equals("")) {
        toTest.press(KeyCode.BACK_SPACE)
        toTest.release(KeyCode.BACK_SPACE)
      }
    }

    override def clickDelete(): Unit = {
      toTest.clickOn(deleteId)
      toTest.ensureEventQueueComplete()
    }

    override def getMessage: Label =
      toTest.find("#messageLabel")

    override def getTable: TableView[PersonaTable] =
      toTest.find(tableId)

    override def getUpdateButton: Button =
      toTest.find(updateId)

    override def getSaveButton: Button =
      toTest.find(addButtonId)

    override def openZona(): Unit =
      toTest.clickOn("#zonaManage")

  }
}