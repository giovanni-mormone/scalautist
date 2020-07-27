package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent

trait TerminalLabelBox extends Component[ShowParamAlgorithmBoxParent]

object TerminalLabelBox {

  def apply(terminals: String): TerminalLabelBox = new TerminalLabelBoxFX(terminals)

  private class TerminalLabelBoxFX(terminals: String) extends AbstractComponent[ShowParamAlgorithmBoxParent]("manager/subcomponent/OneLabel")
    with TerminalLabelBox {

    @FXML
    var labelOne: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      labelOne.setText(terminals)
    }
    
  }

}
