package view.fxview.component.HumanResources.subcomponent

import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.CheckBox

class PersonaTable(idp: String, namep: String, surnamep: String){

  var id = new SimpleStringProperty(idp)
  var name = new SimpleStringProperty(namep)
  var surname = new SimpleStringProperty(surnamep)
  //var contract = new SimpleStringProperty(contractp)
  var selected: CheckBox = new CheckBox()

  def getId: String = id.get
  def getName: String = name.get
  def getSurname: String = surname.get
  //def getContract: String = contract.get
  def getSelected: CheckBox = selected

  def setId(v: String) = id.set(v)
  def setName(v: String) = name.set(v)
  def setSurname(v: String) = surname.set(v)
  //def setContract(v: String) = contract.set(v)
  def setSelected(v: CheckBox) = selected = v
  def isSelected: Boolean = selected.isSelected
}
