package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent
import view.fxview.component.{AbstractComponent, Component}

trait InfoGroup extends Component[ShowParamAlgorithmBoxParent]


object InfoGroup{

  def apply(labelName:String,value:String): InfoGroup = new InfoGroupLabelsFX(labelName,value)

  private class InfoGroupLabelsFX(labelName:String,value:String) extends AbstractComponent[ShowParamAlgorithmBoxParent]("manager/subcomponent/DriverResultBox")
    with InfoGroup{

    @FXML
    var driverId: Label = _
    @FXML
    var driverTerminal: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      driverId.setText(labelName)
      driverTerminal.setText(value)

    }
  }
}
