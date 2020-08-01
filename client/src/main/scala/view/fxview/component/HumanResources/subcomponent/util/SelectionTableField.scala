package view.fxview.component.HumanResources.subcomponent.util

import javafx.scene.control.CheckBox

/**
 * @author Francesco Cassano
 *
 * Define method to execute when table needs selection field
 */
trait SelectionTableField extends TableArgument {

  var selected: CheckBox = new CheckBox()

  /**
   * basic get
   *
   * @return
   *         CheckBox instance
   */
  def getSelected: CheckBox = selected

  /**
   * basic set
   *
   * @param v
   *          CheckBox instance
   */
  def setSelected(v: CheckBox): Unit = selected = v

  /**
   * return true if checkBox is selected else false
   *
   * @return
   *         Boolean of selected
   */
  def isSelected: Boolean = selected.isSelected
}
