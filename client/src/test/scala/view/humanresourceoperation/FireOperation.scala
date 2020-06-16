package view.humanresourceoperation

import javafx.scene.control.Label
import view.baseconfiguration.BaseTest

trait FireOperation {
  def openFireBox(): Unit
  def fireOne(): Unit
  def fireMore(): Unit
  def searchSomeone(): Unit
  def pressFireButton(): Unit
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

    override def fireOne(): Unit = {
      toTest.clickOn("Fabian")
    }

    override def fireMore(): Unit = ???

    override def getLabel: Label = ???

    override def searchSomeone(): Unit = ???

    override def pressFireButton(): Unit = ???
  }
}
