package view.fxview.component.driver.subcomponent
import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.driver.subcomponent.parent.SalaryBoxParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait LabelAndValueBox extends Component[SalaryBoxParent]


object LabelAndValueBox{

  def apply(labelName:String,value:String): LabelAndValueBox = new LabelAndValueBoxFX(labelName,value)

  private class LabelAndValueBoxFX(labelName:String,value:String) extends AbstractComponent[SalaryBoxParent]("manager/subcomponent/DriverResultBox")
    with LabelAndValueBox{

    @FXML
    var driverId: Label = _
    @FXML
    var driverTerminal: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      driverId.setText(resources.getResource(labelName))
      driverTerminal.setText(value)

    }
  }
}