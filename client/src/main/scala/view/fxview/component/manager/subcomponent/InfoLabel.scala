package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.manager.subcomponent.parent.ModalRunParent
import view.fxview.component.{AbstractComponent, Component}

trait InfoLabel extends Component[ModalRunParent]

object InfoLabel {

  def apply(date:String): InfoLabel = new InfoLabelFX(date)

  private class InfoLabelFX(date: String) extends AbstractComponent[ModalRunParent]("manager/subcomponent/OneLabel")
    with InfoLabel {

    @FXML
    var labelOne: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      labelOne.setText(date)
    }
  }

}
