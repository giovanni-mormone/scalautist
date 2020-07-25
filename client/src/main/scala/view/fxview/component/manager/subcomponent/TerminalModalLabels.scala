package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.driver.subcomponent.parent.SalaryBoxParent
import view.fxview.component.manager.subcomponent.parent.ModalParamParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait TerminalModalLabels extends Component[ModalParamParent]


object TerminalModalLabels{

  def apply(labelName:String,value:String): TerminalModalLabels = new TerminalModalLabelsFX(labelName,value)

  private class TerminalModalLabelsFX(labelName:String,value:String) extends AbstractComponent[ModalParamParent]("manager/subcomponent/DriverResultBox")
    with TerminalModalLabels{

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
