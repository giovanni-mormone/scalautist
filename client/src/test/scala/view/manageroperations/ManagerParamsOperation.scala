package view.manageroperations

import java.time.LocalDate

import javafx.scene.control.{Button, DatePicker, TableView}
import view.baseconfiguration.BaseTest
import view.fxview.component.HumanResources.subcomponent.util.PersonaTableWithSelection
import view.fxview.component.manager.subcomponent.util.ParamsTable

trait ManagerParamsOperation {

  def clickNext(): Unit
  def clickReset(): Unit
  def clickOnComponent(name: String): Unit
  def sleep(time: Int): Unit
  def setTime(datePicker: String, day: Int)
  def isButtonEnable(component: String): Boolean
  def chooseOldParam(): Unit
  def isThere(component: String): Boolean
}

object ManagerParamsOperation {

  def apply(toTest: BaseTest): ManagerParamsOperation = new ManagerParamsOperationImpl(toTest)

  private class ManagerParamsOperationImpl(toTest: BaseTest) extends ManagerParamsOperation{

    val FIRST_OF_MONTH: LocalDate = LocalDate.of(2020,8,1)
    val LAST_OF_MONTH: LocalDate = LocalDate.of(2020,8,31)
    val GROUP_DAY_1: LocalDate = LocalDate.of(2020,8,21)
    val GROUP_DAY_2: LocalDate = LocalDate.of(2020,8,20)
    val TODAY: LocalDate = LocalDate.now()
    val NEXT: String = "#run"

    override def clickNext(): Unit =
      toTest.clickOn(NEXT)

    override def clickReset(): Unit =
      toTest.clickOn("#reset")

    override def clickOnComponent(name: String): Unit =
      toTest.clickOn(name)

    override def sleep(time: Int): Unit =
      toTest.sleep(time)

    override def setTime(datePicker: String, day: Int): Unit = {
      val datePic: DatePicker = toTest.find(datePicker)
      day match {
        case 1 => datePic.setValue(FIRST_OF_MONTH)
        case 2 => datePic.setValue(LAST_OF_MONTH)
        case 3 => datePic.setValue(GROUP_DAY_1)
        case 4 => datePic.setValue(GROUP_DAY_2)
        case _ => datePic.setValue(TODAY)
      }
    }

    override def isButtonEnable(component: String): Boolean =
      !toTest.find[Button](component).isDisable

    override def chooseOldParam(): Unit =
      toTest.find[TableView[ParamsTable]]("#params")
        .getSelectionModel.select(10) //10

    override def isThere(component: String): Boolean =
      Option(toTest.find(component)).isDefined
  }
}
