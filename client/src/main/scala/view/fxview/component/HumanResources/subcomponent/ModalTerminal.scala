package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Terminale, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.HumanResources.subcomponent.parent.{ModalTerminalParent, ModalZoneParent}

/**
 * @author Francesco Cassano
 *
 * Box that is drawn inside modal to manage information about a [[caseclass.CaseClassDB.Terminale]] instance.
 * It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.ModalTerminalParent]]
 */
trait ModalTerminal extends Component[ModalTerminalParent] {

}

object ModalTerminal {

  def apply(zones: List[Zona], terminal: Terminale): ModalTerminal = new ModalTerminalFX(zones, terminal)

  private class ModalTerminalFX(zonesList: List[Zona], terminal: Terminale)
    extends AbstractComponent[ModalTerminalParent]("humanresources/subcomponent/ModalTerminal")
      with ModalTerminal {

    @FXML
    var zones: ComboBox[String] = _
    @FXML
    var id: TextField = _
    @FXML
    var name: TextField = _
    @FXML
    var delete: Button = _
    @FXML
    var update: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      initializeComboBox()
      initializeFixedField()

      name.setText(terminal.nomeTerminale)

      delete.setText(resources.getString("delete"))
      delete.setOnAction(_ => parent.deleteTerminal(terminal))

      updateButton(resources)
    }

    def initializeComboBox(): Unit = {
      zonesList.foreach(zone => zones.getItems.add(zone.zones))
      zones.getSelectionModel.select(zonesList.filter(zone => terminal.idZona == zone.idZone.head).head.zones)
    }

    def initializeFixedField(): Unit = {
      id.setDisable(true)
      id.setText(terminal.idTerminale.head.toString)
    }

    def updateButton(resourceBundle: ResourceBundle): Unit = {
      update.setText(resourceBundle.getString("update"))
      update.setOnAction(_ => parent.updateTerminal(
        Terminale(name.getText(),
          zonesList.filter(zone => zones.getSelectionModel.getSelectedItem.equals(zone.zones)).head.idZone.head,
          Some(id.getText.toInt)))
      )
    }
  }
}


