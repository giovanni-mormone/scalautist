package view.fxview.component.manager.subcomponent.util

import javafx.scene.control.ComboBox
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

abstract class ComboTableField(comboVal: ComboBox[String]) extends TableArgument {

  var combo: ComboBox[String] = combo

  def getCombo: ComboBox[String] = combo

  def setCombo(value: ComboBox[String]): Unit = combo = value

  def getSelected: Option[String] = Option(combo.getSelectionModel.getSelectedItem)
}
