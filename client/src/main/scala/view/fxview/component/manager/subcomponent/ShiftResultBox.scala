package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoDates
import javafx.fxml.FXML
import javafx.scene.control.Label
import view.fxview.component.manager.subcomponent.parent.SelectResultParent
import view.fxview.component.{AbstractComponent, Component}

trait ShiftResultBox extends Component[SelectResultParent]{
}

object ShiftResultBox {

  def apply(result: InfoDates): ShiftResultBox = new ShiftResultBoxFX(result)

  private class ShiftResultBoxFX(result: InfoDates) extends AbstractComponent[SelectResultParent]("manager/subcomponent/ShiftResultBox")
    with ShiftResultBox {
    @FXML
    var firstShift: Label = _
    @FXML
    var secondShift: Label = _
    @FXML
    var extraOrdinary: Label = _
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      firstShift.setText(result.turno)
      result.turno2 match {
        case Some(value) => secondShift.setText(value)
        case None => secondShift.setText("")
      }
      result.straordinario match {
        case Some(value) => extraOrdinary.setText(value)
        case None => extraOrdinary.setText("")
      }
    }

  }

}
