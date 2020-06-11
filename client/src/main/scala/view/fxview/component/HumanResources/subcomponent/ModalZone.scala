package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.ModalZoneParent
import view.fxview.component.{AbstractComponent, Component}

/**
 * @author Francesco Cassano
 *
 * Box that is drawn inside modal to manage information about a [[caseclass.CaseClassDB.Zona]] instance.
 * It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.ModalZoneParent]]
 */
trait ModalZone extends Component[ModalZoneParent] {

}

object ModalZone {

  def apply(zona: Zona): ModalZone = new ModalZoneFX(zona)

  private class ModalZoneFX(zona: Zona)
    extends AbstractComponent[ModalZoneParent]("humanresources/subcomponent/ModalZone") with ModalZone {

    @FXML
    var id: TextField = _
    @FXML
    var name: TextField = _
    @FXML
    var delete: Button = _
    @FXML
    var update: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      id.setText(zona.idZone.head.toString)
      id.setEditable(false)

      name.setText(zona.zones)
      name.setEditable(true)

      delete.setText(resources.getString("delete"))
      delete.setOnAction(_ => parent.deleteZona(zona))

      update.setText(resources.getString("update"))
      update.setOnAction(_ => parent.updateZona(Zona(name.getText, zona.idZone))) //todo controllare stringa
    }

  }
}
