package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Terminale, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField}
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.util.TextFieldControl
import view.fxview.component.manager.subcomponent.parent.ModalTerminalParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
/**
 * @author Francesco Cassano
 *
 * Box that is drawn inside modal to manage information about a [[caseclass.CaseClassDB.Terminale]] instance.
 * It extends [[view.fxview.component.Component]]
 * of [[ModalTerminalParent]]
 */
trait ModalTerminal extends Component[ModalTerminalParent] {

}

/**
 * Companion object of [[ModalTerminal]]
 */
object ModalTerminal {

  def apply(zones: List[Zona], terminal: Terminale): ModalTerminal = new ModalTerminalFX(zones, terminal)

  /**
   * JavaFX implementation of [[ModalTerminal]]
   *
   * @param zonesList
   *                  list of reference [[caseclass.CaseClassDB.Zona]] of the terminals
   * @param terminal
   *                 list of [[caseclass.CaseClassDB.Terminale]] to manage
   */
  private class ModalTerminalFX(zonesList: List[Zona], terminal: Terminale)
    extends AbstractComponent[ModalTerminalParent]("manager/subcomponent/ModalTerminal")
      with ModalTerminal {

    @FXML
    var zones: ComboBox[String] = _
    @FXML
    var id: TextField = _
    @FXML
    var namet: TextField = _
    @FXML
    var delete: Button = _
    @FXML
    var update: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      initializeComboBox()
      initializeFixedField()

      manageTerminalText()

      delete.setText(resources.getResource("delete"))
      delete.setOnAction(_ => parent.deleteTerminal(terminal))

      updateButton(resources)

      ableToChange()
    }

    private def initializeComboBox(): Unit = {
      zonesList.foreach(zone => zones.getItems.add(zone.zones))
      zones.getSelectionModel.select(zonesList.filter(zone => terminal.idZona == zone.idZone.head).head.zones)
    }

    private def initializeFixedField(): Unit = {
      id.setDisable(true)
      id.setText(terminal.idTerminale.head.toString)
    }

    private def updateButton(resourceBundle: ResourceBundle): Unit = {
      update.setText(resourceBundle.getResource("update"))
      update.setOnAction(_ => parent.updateTerminal(
        Terminale(namet.getText(),
          zonesList.filter(zone => zones.getSelectionModel.getSelectedItem.equals(zone.zones)).head.idZone.head,
          Some(id.getText.toInt)))
      )
    }

    private def manageTerminalText(): Unit = {
      namet.setText(terminal.nomeTerminale)
      namet.textProperty().addListener((_, oldS, word) => {
        TextFieldControl.controlNewChar(namet, ZonaChecker, word, oldS)
        ableToChange()
      })
    }

    private def ableToChange(): Unit =
      update.setDisable(namet.getText().equals(""))

  }
}


