package view.fxview.component.manager.subcomponent.util

import java.util.Observable

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

class ShiftTable(shiftVal: String, mondayVal: String, tuesdayVal: String, wednesdayVal: String,
                 thursdayVal: String, fridayVal: String, saturdayVal: String, combos: List[String])
  extends TableArgument {

  val elements = {
    val el = FXCollections.observableArrayList[String]()
    combos.foreach(rule => el.add(rule))
    el
  }
  var shift = new SimpleStringProperty(shiftVal)
  var monday = new SimpleStringProperty(mondayVal)
  var tuesday = new SimpleStringProperty(tuesdayVal)
  var wednesday = new SimpleStringProperty(wednesdayVal)
  var thursday = new SimpleStringProperty(thursdayVal)
  var friday = new SimpleStringProperty(fridayVal)
  var saturday = new SimpleStringProperty(saturdayVal)
  var comboBox = new ComboBox[String](elements)

  def getShift: String = shift.get
  def getMonday: String = monday.get
  def getTuesday: String = tuesday.get
  def getWednesday: String = wednesday.get
  def getThursday: String = thursday.get
  def getFriday: String = friday.get
  def getSaturday: String = saturday.get
  def getCombo: ComboBox[String] = comboBox

  def setShift(value: String): Unit = shift.set(value)
  def setMonday (value: String): Unit = monday.set(value)
  def setTuesday(value: String): Unit = tuesday.set(value)
  def setWednesday(value: String): Unit = wednesday.set(value)
  def setThursday(value: String): Unit = thursday .set(value)
  def setFriday(value: String): Unit = friday.set(value)
  def setSaturday(value: String): Unit = saturday.set(value)
  def setCombo(value: ComboBox[String]): Unit = comboBox = value

  def getSelected: Option[String] = Option(comboBox.getSelectionModel.getSelectedItem)
}

object ShiftTable{

  val editableShiftTable: List[(String, (ShiftTable, String) => ShiftTable)] = List(
    ("monday", (st, value) => {
      st.setMonday(value); st
    }),
    ("tuesday", (st, value) => {
      st.setTuesday(value); st
    }),
    ("wednesday", (st, value) => {
      st.setWednesday(value); st
    }),
    ("thursday", (st, value) => {
      st.setThursday(value); st
    }),
    ("friday", (st, value) => {
      st.setFriday(value); st
    }),
    ("saturday", (st, value) => {
      st.setSaturday(value); st
    })
  )
}