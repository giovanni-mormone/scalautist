package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.manager.subcomponent.parent.ModalGruopParent
import view.fxview.component.{AbstractComponent, Component}

trait LabelDateBox  extends Component[ModalGruopParent]

object LabelDateBox {

  def apply(date:String): LabelDateBox = new LabelDateBoxBoxFX(date)

  private class LabelDateBoxBoxFX(date: String) extends AbstractComponent[ModalGruopParent]("manager/subcomponent/OneLabel")
    with LabelDateBox {

    @FXML
    var labelOne: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      labelOne.setText(date)
    }
  }

}
