package view.fxview.component.manager

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoAbsence
import view.fxview.util.ResourceBundleUtil._
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, Pane}
import view.fxview.FXHelperFactory
import view.fxview.component.manager.subcomponent.FillHolesBox
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent
import view.fxview.component.{AbstractComponent, Component}

trait ManagerHome extends Component[ManagerHomeParent]{

  def drawManageAbsence(absences: List[InfoAbsence]): Unit
}

object ManagerHome{

  def apply(): ManagerHome = new ManagerHomeFX()

  private class ManagerHomeFX extends AbstractComponent[ManagerHomeParent]("manager/BaseManager")
    with ManagerHome{
    @FXML
    var nameLabel: Label = _
    @FXML
    var baseManager: BorderPane = _
    @FXML
    var notificationButton: Button = _
    @FXML
    var generateTurnsButton: Button = _
    @FXML
    var manageAbsenceButton: Button = _
    @FXML
    var redoTurnsButton: Button = _
    @FXML
    var printResultButton: Button = _
    @FXML
    var manageZoneButton: Button = _
    @FXML
    var manageTerminalButton: Button = _
    @FXML
    var idLabel: Label = _


    var fillHolesView: FillHolesBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText(resources.getResource("username-label"))
      idLabel.setText(resources.getResource("id-label"))
      notificationButton.setText(resources.getResource("notification-button"))
      generateTurnsButton.setText(resources.getResource("generate-turns-button"))
      manageAbsenceButton.setText(resources.getResource("manage-absence-button"))
      redoTurnsButton.setText(resources.getResource("redo-turns-button"))
      printResultButton.setText(resources.getResource("print-result-button"))
      manageZoneButton.setText(resources.getResource("manage-zone-button"))
      manageTerminalButton.setText(resources.getResource("manage-terminal-button"))

      manageAbsenceButton.setOnAction(_ => parent.drawAbsencePanel())
    }

    override def drawManageAbsence(absences: List[InfoAbsence]): Unit = {
      val a = FillHolesBox()
      a.setParent(parent)
      baseManager.setCenter(a.pane)
      a.drawAbsenceList(absences)
    }

  }

}
