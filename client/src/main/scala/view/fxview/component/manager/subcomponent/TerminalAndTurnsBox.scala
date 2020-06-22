package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoAbsenceOnDay
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.FillHolesParent

trait TerminalAndTurnsBox extends Component[FillHolesParent]

object TerminalAndTurnsBox{

  def apply(dataToShow: InfoAbsenceOnDay): TerminalAndTurnsBox = new TerminalAndTurnsBoxFX(dataToShow)

  private class TerminalAndTurnsBoxFX(absence: InfoAbsenceOnDay) extends AbstractComponent[FillHolesParent]("manager/subcomponent/TwoFieldBox")
    with TerminalAndTurnsBox{

    @FXML
    var field1: Label = _
    @FXML
    var field2: Label = _
    @FXML
    var baseBox: HBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      field1.setText(absence.nomeTerminale)
      field2.setText(absence.nomeTurno)
      baseBox.setOnMouseClicked(e => if(e.getClickCount == 2) parent.absenceSelected(absence.idTurno, absence.idRisultato,absence.idTerminale))
    }
  }
}