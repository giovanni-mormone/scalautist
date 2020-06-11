package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.parent.ModalZoneParent
import view.fxview.component.HumanResources.subcomponent.util.TextFieldControl
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

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.ModalZone]]
 */
object ModalZone {

  def apply(zona: Zona): ModalZone = new ModalZoneFX(zona)

  /**
   * JavaFX implementation of [[view.fxview.component.HumanResources.subcomponent.ModalZone]]
   * @param zona
   */
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

      manageZonaText()

      delete.setText(resources.getString("delete"))
      delete.setOnAction(_ => parent.deleteZona(zona))

      update.setText(resources.getString("update"))
      update.setOnAction(_ => parent.updateZona(Zona(name.getText, zona.idZone)))

      ableToChange
    }

    private def manageZonaText(): Unit = {
      name.setText(zona.zones)
      name.setEditable(true)
      name.textProperty().addListener((_, oldS, word) => {
        TextFieldControl.controlNewChar(name, ZonaChecker, word, oldS)
        ableToChange()
      })
    }

    private def ableToChange(): Unit =
      update.setDisable(name.getText().equals(""))

  }
}
