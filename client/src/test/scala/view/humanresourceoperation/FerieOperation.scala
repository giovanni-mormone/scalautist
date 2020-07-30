package view.humanresourceoperation

import java.time.LocalDate

import javafx.scene.control.DatePicker
import view.baseconfiguration.BaseTest

import scala.annotation.nowarn

trait FerieOperation{
  def enterFirstDate(day: LocalDate): Unit
  def enterSecondDate(day: LocalDate): Unit
  def clickButtonHoliday(): Unit
  def clickModalButton(): Unit
  def clickTable():Unit
  def clickButtonCloseError(): Unit
}
object FerieOperation {
  def apply(toTest: BaseTest): FerieOperation = new FerieOperationImpl(toTest)

  private class FerieOperationImpl(toTest: BaseTest) extends FerieOperation{

    override def enterFirstDate(day: LocalDate): Unit = {
      val initDate:DatePicker = toTest.find("#initDate"): @nowarn
      toTest.clickOn(initDate)
      initDate.setValue(day)
    }

    override def enterSecondDate(day: LocalDate): Unit = {
      val finishDate:DatePicker = toTest.find("#finishDate"): @nowarn
      toTest.clickOn(finishDate)
      finishDate.setValue(day)
    }

    override def clickButtonHoliday(): Unit = {
      toTest.clickOn("#holidays")
    }
    override def clickButtonCloseError(): Unit = {
      toTest.clickOn("#confirmationButton")
    }
    override def clickModalButton(): Unit = {
      toTest.clickOn("#button")
    }

    override def clickTable(): Unit = {
      toTest.doubleClickOn("2")
    }
  }
}
