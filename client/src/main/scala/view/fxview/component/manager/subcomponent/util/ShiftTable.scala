package view.fxview.component.manager.subcomponent.util

import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

class ShiftTable(shiftVal: String, mondayVal: String, tuesdayVal: String, wednesdayVal: String,
                 thursdayVal: String, fridayVal: String, saturdayVal: String) extends TableArgument {

  var shift = new SimpleStringProperty(shiftVal)
  var monday = new SimpleStringProperty(mondayVal)
  var tuesday = new SimpleStringProperty(tuesdayVal)
  var wednesday = new SimpleStringProperty(wednesdayVal)
  var thursday = new SimpleStringProperty(thursdayVal)
  var friday = new SimpleStringProperty(fridayVal)
  var saturday = new SimpleStringProperty(saturdayVal)

  def getShift: String = shift.get
  def getMonday: String = monday.get
  def getTuesday: String = tuesday.get
  def getWednesday: String = wednesday.get
  def getThursday: String = thursday.get
  def getFriday: String = friday.get
  def getSaturday: String = saturday.get

  def setShift(value: String): Unit = shift.set(value)
  def setMonday (value: String): Unit = monday.set(value)
  def setTuesday(value: String): Unit = tuesday.set(value)
  def setWednesday(value: String): Unit = wednesday.set(value)
  def setThursday(value: String): Unit = thursday .set(value)
  def setFriday(value: String): Unit = friday.set(value)
  def setSaturday(value: String): Unit = saturday.set(value)

}

object ShiftTable{

  val editableShiftTable: List[(String, (ShiftTable, String) => Unit)] = List(
    ("monday", (st, value) => st.setMonday(value)),
    ("tuesday", (st, value) => st.setTuesday(value)),
    ("wednesday", (st, value) => st.setWednesday(value)),
    ("thursday", (st, value) => st.setThursday(value)),
    ("friday", (st, value) => st.setFriday(value)),
    ("saturday", (st, value) => st.setSaturday(value))
  )
}