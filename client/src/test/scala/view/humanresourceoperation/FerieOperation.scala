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
      val initDate:DatePicker = toTest.find("#initDate")
      toTest.sleep(1000)
      toTest.clickOn(initDate)
      initDate.setValue(day)
    }

    override def enterSecondDate(day: LocalDate): Unit = {
      val finishDate:DatePicker = toTest.find("#finishDate")
      toTest.sleep(1000)
      toTest.clickOn(finishDate)
      finishDate.setValue(day)
    }

    override def clickButtonHoliday(): Unit = {
      toTest.clickOn("#holidays")
      toTest.sleep(1000)
    }
    override def clickButtonCloseError(): Unit = {
      toTest.clickOn("#confirmationButton")
      toTest.sleep(1000)
    }
    override def clickModalButton(): Unit = {
      toTest.clickOn("#button")
      toTest.sleep(1000)
    }

    override def clickTable(): Unit = {
      toTest.doubleClickOn("2")
      toTest.sleep(1000)
    }
  }
}
