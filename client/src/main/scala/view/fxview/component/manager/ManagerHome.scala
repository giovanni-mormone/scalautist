package view.fxview.component.manager

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Parametro, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane
import view.fxview.FXHelperFactory
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent
import view.fxview.component.manager.subcomponent.{ChooseParamsBox, FillHolesBox, ManagerRichiestaBox}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/**
 * @author Fabian Aspee Encina, Giovanni Mormone, Francesco Cassano
 * trait of methods that allow user to do desired operations.
 */
trait ManagerHome extends Component[ManagerHomeParent]{

  /**
   * Method used to draw the panel that allow to choose params for run assignment algorithm
   *
   * @param terminals List of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawChooseParams(terminals: List[Terminale]): Unit

  def reDrawRichiesta(): Unit

  /**
   * method that send all shift that existing in system and allow draw this
   * @param listShift list with all shift in the system
   */
  def drawShiftRichiesta(listShift: List[Turno]): Unit

  /**
   *  method that send all terminal that existing in system and allow draw this
   * @param terminal list with all terminal existing in system
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * Method used to draw the list of turns that needs a replacement
   *
   * @param absences
   *                 The list of turns that needs a replacement
   */
  def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used to draw the list of people avalaible for the turn that needs a replacement
   * @param replacement
   *                    The people avalaible for the turn that needs a replacement
   */
  def drawManageReplacement(replacement: List[InfoReplacement]): Unit

  def loadingReplacements(): Unit

  def stopLoadingReplacements(): Unit

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
    var richiestaButton: Button = _
    @FXML
    var idLabel: Label = _

    var fillHolesView: FillHolesBox = _
    var managerRichiestaBoxView:ManagerRichiestaBox = _
    var chooseParamsBox: ChooseParamsBox = _


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
      richiestaButton.setText(resources.getResource("richiesta-button"))
      manageAbsenceButton.setOnAction(_ => parent.drawAbsencePanel())
      richiestaButton.setOnAction(_ => parent.drawRichiestaPanel())
      generateTurnsButton.setOnAction(_ => parent.drawParamsPanel())
    }

    override def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit = {
      fillHolesView = FillHolesBox()
      baseManager.setCenter(fillHolesView.setParent(parent).pane)
      fillHolesView.drawAbsenceList(absences)
    }

    override def drawManageReplacement(replacement: List[InfoReplacement]): Unit = {
      fillHolesView.endLoading()
      fillHolesView.drawSubstituteList(replacement)
    }

    override def startLoading(): Unit =
      baseManager.setCenter(FXHelperFactory.loadingBox)

    override def loadingReplacements(): Unit =
      fillHolesView.startLoading()

    override def stopLoadingReplacements(): Unit =
      fillHolesView.endLoading()

    override def drawRichiesta(terminal: List[Terminale]): Unit = {
      managerRichiestaBoxView = ManagerRichiestaBox(terminal)
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def reDrawRichiesta(): Unit = {
      managerRichiestaBoxView.reDrawRichiesta()
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def drawShiftRichiesta(listShift: List[Turno]): Unit = {
      managerRichiestaBoxView.drawShiftRequest(listShift)
    }

    override def drawChooseParams(terminals: List[Terminale]): Unit = {
      chooseParamsBox = ChooseParamsBox(terminals)
      baseManager.setCenter(chooseParamsBox.setParent(parent).pane)
    }

  }
}
