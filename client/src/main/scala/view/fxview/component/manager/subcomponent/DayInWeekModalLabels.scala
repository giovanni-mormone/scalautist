package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ModalParamParent

trait DayInWeekModalLabels extends Component[ModalParamParent]


object DayInWeekModalLabels{

  def apply(day:String,shift:String,variation:String,rule:String): DayInWeekModalLabels =
    new DayInWeekModalLabelsFX(day,shift,variation,rule)

  private class DayInWeekModalLabelsFX(day:String,shift:String,variation:String,rule:String)
    extends AbstractComponent[ModalParamParent]("manager/subcomponent/ThreeFieldBox")
    with DayInWeekModalLabels{

    @FXML
    var field1: Label = _
    @FXML
    var field2: Label = _
    @FXML
    var field3: Label = _
    @FXML
    var field4: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      field1.setText(day)
      field2.setText(shift)
      field3.setText(variation)
      field4.setText(rule)
    }
  }
}

