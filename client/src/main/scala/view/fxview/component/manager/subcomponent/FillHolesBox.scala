package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import view.fxview.component.manager.subcomponent.parent.FillHolesParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._


trait FillHolesBox extends Component[FillHolesParent]{
  /**
   * Method used to draw the List of turns that needs a replacement
   * @param terminalsAndTurn
   *                         The turns and terminals that needs a replacement
   */
  def drawAbsenceList(terminalsAndTurn: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used to draw the List of avaliable people for the selected turn to replace
   * @param substitutes
   *                     The avaliable people for the selected turn to replace
   */
  def drawSubstituteList(substitutes: List[InfoReplacement]): Unit
}

object FillHolesBox{

  def apply(): FillHolesBox = new FillHolesFX()

  private class FillHolesFX extends AbstractComponent[FillHolesParent]("manager/subcomponent/FillHolesBox")
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

    override def drawAbsenceList(terminalsAndTurn: List[InfoAbsenceOnDay]): Unit =
     terminalsAndTurn.foreach(absence => terminalsAndTurnsBox.getChildren.add(TerminalAndTurnsBox(absence).setParent(parent).pane))

    override def drawSubstituteList(substitutes: List[InfoReplacement]): Unit = {
      substitutesBox.getChildren.remove(1,substitutesBox.getChildren.size())
      substitutes.foreach(replacement => substitutesBox.getChildren.add(ReplacementsBox(replacement).setParent(parent).pane))
    }
  }
}
