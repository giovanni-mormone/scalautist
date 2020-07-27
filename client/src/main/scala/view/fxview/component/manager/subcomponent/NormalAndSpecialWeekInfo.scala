package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent
import view.fxview.component.{AbstractComponent, Component}

trait NormalAndSpecialWeekInfo extends Component[ShowParamAlgorithmBoxParent]


object NormalAndSpecialWeekInfo{

  def apply(day:String,shift:String,variation:String,rule:String): NormalAndSpecialWeekInfo = new NormalAndSpecialWeekInfoLabelsFX(day,shift,variation,rule)

  private class NormalAndSpecialWeekInfoLabelsFX(day:String,shift:String,variation:String,rule:String) extends AbstractComponent[ShowParamAlgorithmBoxParent]("manager/subcomponent/ThreeFieldBox")
    with NormalAndSpecialWeekInfo{

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
