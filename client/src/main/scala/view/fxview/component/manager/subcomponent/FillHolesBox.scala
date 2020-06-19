package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Persona
import caseclass.CaseClassHttpMessage.InfoAbsence
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableView, TextField}
import javafx.scene.layout.VBox
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.manager.subcomponent.parent.TerminalAndTurnsParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._


trait FillHolesBox extends Component[TerminalAndTurnsParent]{

  def drawAbsenceList(terminalsAndTurn: List[InfoAbsence]): Unit

  def drawSubstituteList(substitutes: List[InfoReplacement]): Unit

}

object FillHolesBox{

  def apply(): FillHolesBox = new FillHolesFX()

  private class FillHolesFX extends AbstractComponent[TerminalAndTurnsParent]("manager/subcomponent/FillHolesBox")
    with FillHolesBox{
    @FXML
    var terminalsAndTurnsBox: VBox= _
    @FXML
    var substitutesBox: VBox = _
    @FXML
    var terminalAndTurnsBoxColumn1: Label = _
    @FXML
    var terminalAndTurnsBoxColumn2: Label = _
    @FXML
    var substitutesBoxColumn1: Label = _
    @FXML
    var substitutesBoxColumn2: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      terminalAndTurnsBoxColumn1.setText(resources.getResource("terminal-column"))
      terminalAndTurnsBoxColumn2.setText(resources.getResource("turn-column"))
      substitutesBoxColumn1.setText(resources.getResource("name-column"))
      substitutesBoxColumn2.setText(resources.getResource("surname-column"))

    }

    override def drawAbsenceList(terminalsAndTurn: List[InfoAbsence]): Unit = {
     terminalsAndTurn.foreach(absence => {
       terminalsAndTurnsBox.getChildren.add(TerminalAndTurnsBox(absence).setParent(parent).pane)
     })
    }

    override def drawSubstituteList(substitutes: List[Any]): Unit = {

    }
  }
}
