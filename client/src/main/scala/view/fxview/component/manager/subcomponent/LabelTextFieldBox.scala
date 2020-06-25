package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import view.fxview.component.{AbstractComponent, Component}

trait LabelTextFieldBox extends Component[RichiestaForDayBox]{
  def getQuantity:(Int,Int)
}
object LabelTextFieldBox{

  def apply(shift: Turno): LabelTextFieldBox = new LabelTextFieldBoxFX(shift)

  private class LabelTextFieldBoxFX(shift: Turno) extends AbstractComponent[RichiestaForDayBox]("manager/subcomponent/LabelTextFieldBox")
    with LabelTextFieldBox{

    @FXML
    var shiftLabel: Label = _
    @FXML
    var quantity: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      shiftLabel.setText(shift.nomeTurno)
    }
    override def getQuantity: (Int, Int) = (shift.id.head,quantity.getText.toInt)
  }
}