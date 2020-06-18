package view.humanresourceoperation

import java.time.LocalDate

import javafx.scene.control.DatePicker
import view.baseconfiguration.BaseTest

trait MalattieOperation{
  def enterFirstDate(day: LocalDate): Unit
  def enterSecondDate(day: LocalDate): Unit
  def clickButtonIllness(): Unit
  def clickModalButton(): Unit
  def clickTable():Unit
  def clickButtonCloseError(): Unit
}
object MalattieOperation {
  def apply(toTest: BaseTest): MalattieOperation = new MalattieOperationImpl(toTest)

  private class MalattieOperationImpl(toTest: BaseTest) extends MalattieOperation{

    override def enterFirstDate(day: LocalDate): Unit =  {
      val initDate:DatePicker = toTest.find("#initDate")
      toTest.clickOn(initDate)
      initDate.setValue(day)

    }
    override def enterSecondDate(day: LocalDate): Unit = {
      val finishDate:DatePicker = toTest.find("#finishDate")
      toTest.clickOn(finishDate)
      finishDate.setValue(day)
    }

    override def clickModalButton(): Unit = {
      toTest.clickOn("#button")
    }

    override def clickTable(): Unit =  {
      toTest.doubleClickOn("2")
    }

    override def clickButtonIllness(): Unit = {
      toTest.clickOn("#illness")
    }

    override def clickButtonCloseError(): Unit = {
      toTest.clickOn("#confirmationButton")
    }
  }
}
