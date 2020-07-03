package view.manageroperations

import java.time.LocalDate

import javafx.scene.control.cell.CheckBoxListCell
import javafx.scene.control.{DatePicker, Label}
import javafx.scene.layout.HBox
import view.baseconfiguration.BaseTest

trait ManagerTeoricRequestOperation {
  def clickCancel():Unit

  def clickOk():Unit

  def setAllDay():Unit

  def setAllShiftWithQuantity():Unit

  def search():Label
  def writeInt():Unit

  def writeInFirstShift():Unit

  def setTerminal():Unit

  def setTimeCorrect():Unit

  def setTime():Unit
  def clickNext():Unit
  def clickBack():Unit
  def sleep(time:Int):Unit
  def getDateI:String
  def getDateF: String
  def getQuantity:String
}
object ManagerTeoricRequestOperation {
  def apply(toTest: BaseTest): ManagerTeoricRequestOperation = new ManagerTeoricRequestOperationImpl(toTest)

  private class ManagerTeoricRequestOperationImpl(toTest: BaseTest) extends ManagerTeoricRequestOperation {
    val FIRST_SON = 0
    val SECOND_SON = 1
    val THIRD_SON= 2
    val LUNEDI=1
    val DOMENICA=6
    val FIRST_SHIFT=1
    val END_SHIFT=6
    val DATEI: LocalDate =LocalDate.of(2020,6,7)
    val DATEF: LocalDate =LocalDate.of(2020,6,15)
    val DATEI_CORRECT:LocalDate =  LocalDate.of(2020,6,1)
    val DATEF_CORRECT: LocalDate =LocalDate.of(2020,6,30)
    val QUANTITY="123"
    override def clickNext(): Unit = toTest.clickOn("#next")

    override def clickBack(): Unit = toTest.clickOn("#back")

    override def setTime(): Unit = {
     val (date,date1)= searchDatepicker()
      date.setValue(DATEI)
      date1.setValue(DATEF)
    }
    def searchDatepicker():(DatePicker,DatePicker)=
      (toTest.find("#datepickerInit"),toTest.find("#datepickerFinish"))

    override def setTimeCorrect(): Unit = {
      val (date,date1)= searchDatepicker()
      date.setValue(DATEI_CORRECT)
      date1.setValue(DATEF_CORRECT)
    }

    override def setTerminal(): Unit = {
      toTest.clickOn("#terminal")
      val terminal:CheckBoxListCell[String] = toTest.find("Florida")
      toTest.clickOn(terminal.getChildrenUnmodifiable.get(FIRST_SON))
      toTest.clickOn("#title")
    }

    override def sleep(time:Int): Unit =
      toTest.sleep(time)

    override def writeInFirstShift(): Unit = {
      val idHbox="#1"
      searchTextField(idHbox)
      toTest.write("s")
    }
    def searchTextField(idHbox:String): Unit ={
      val textField:HBox = toTest.find(idHbox)
      toTest.clickOn(textField.getChildrenUnmodifiable.get(SECOND_SON))
    }
    override def writeInt(): Unit = {
      val idHbox="#1"
      searchTextField(idHbox)
      toTest.write(QUANTITY)
    }
    override def search():Label = {
      val idHbox="#2"
      val textField:HBox = toTest.find(idHbox)
      textField.getChildrenUnmodifiable.get(THIRD_SON).asInstanceOf[Label]
    }

    override def setAllShiftWithQuantity(): Unit = {
      (FIRST_SHIFT to END_SHIFT).foreach(id=>{
        searchTextField("#"+id)
        toTest.write(QUANTITY)
      })
    }

    override def setAllDay(): Unit = {
      (LUNEDI to DOMENICA).foreach(_=>{
        setAllShiftWithQuantity()
        clickNext()
      })
    }

    override def getQuantity:String=QUANTITY
    override def getDateI: String =DATEI_CORRECT.toString
    override def getDateF: String =DATEF_CORRECT.toString

    override def clickCancel(): Unit = toTest.clickOn("Cancel")

    override def clickOk(): Unit = toTest.clickOn("OK")

  }
}