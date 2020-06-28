package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.{AbstractComponent, Component}


trait ShiftAndQuantity extends Component[RichiestaForDayBox]{

}
object ShiftAndQuantity{

  def apply(shift: String,quantity:String): ShiftAndQuantity = new ShiftAndQuantityFX(shift,quantity)

  private class ShiftAndQuantityFX(shift: String,quantity:String) extends AbstractComponent[RichiestaForDayBox]("manager/subcomponent/TwoFieldBox")
    with ShiftAndQuantity{

    @FXML
    var field1: Label = _
    @FXML
    var field2: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      field1.setText(shift)
      field2.setText(quantity)
    }
  }
}