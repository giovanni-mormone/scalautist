package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, Label}
import view.fxview.component.{AbstractComponent, Component}

trait CheckBoxSelector extends Component[ModalDisponibilita]{

}

object CheckBoxSelector{

  def apply(value:String): CheckBoxSelector = new CheckBoxSelectorFX(value)

  private class CheckBoxSelectorFX(value: String) extends AbstractComponent[ModalDisponibilita]("driver/subcomponent/CheckboxSelector")
    with CheckBoxSelector {

    @FXML
    var field: Label = _
    @FXML
    var checkbox: CheckBox = _
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      field.setText(value)
      checkbox.setOnAction(_ => parent.daySelected(field.getText))
    }

    override def disable(): Unit = {
      if (!checkbox.isSelected) super.disable()
    }
  }
}
