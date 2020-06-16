package view.humanresourceoperation

import javafx.scene.control.{Label, TableColumn, TableView}
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTableWithSelection

trait FireOperation {
  def openFireBox(): Unit
  def fireOne(): Unit
  def fireMore(): Unit
  def searchSomeone(string: String): Unit
  def pressFireButton(): Unit
  def getTable: TableView[PersonaTableWithSelection]
  def getLabel: Label
}

object FireOperation {

  def apply(toTest: BaseTest): FireOperation = new FireOperationImpl(toTest)

  private class FireOperationImpl(toTest: BaseTest) extends FireOperation {

    private val nameId: String = "#firesButton"
    private val tableNameId: String = "#employeeTable"
    private val searchBoxId: String = "#searchBox"
    private val buttonId: String = "#fireButton"

    override def openFireBox(): Unit =
      toTest.clickOn(nameId)

    override def fireOne(): Unit =
      toTest.find(tableNameId).asInstanceOf[TableView[PersonaTableWithSelection]]
                              .getItems.get(0)
                              .selected.setSelected(true)

    override def fireMore(): Unit = {
      toTest.find(tableNameId).asInstanceOf[TableView[PersonaTableWithSelection]]
        .getItems.get(0)
        .selected.setSelected(true)
      toTest.find(tableNameId).asInstanceOf[TableView[PersonaTableWithSelection]]
        .getItems.get(1)
        .selected.setSelected(true)
    }

    override def getLabel: Label =
      toTest.find("#messageLabel")

    override def searchSomeone(string: String): Unit = {
      toTest.clickOn(searchBoxId).write(string)
    }

    override def pressFireButton(): Unit =
      toTest.clickOn(buttonId)

    override def getTable: TableView[PersonaTableWithSelection] =
      toTest.find(tableNameId)
  }
}
