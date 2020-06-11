package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView, TextField}
import regularexpressionutilities.NameChecker
import view.fxview.component.HumanResources.subcomponent.parent.ZonaParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, TextFieldControl, ZonaTable}
import view.fxview.component.{AbstractComponent, Component}

/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.ZonaParent]]
 */
trait ZonaBox extends Component[ZonaParent] {

}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.ZonaBox]]
 *
 */
object ZonaBox {

  def apply(zones: List[Zona]): ZonaBox = new ZonaBoxFX(zones)

  /**
   * javaFX private implementation of [[view.fxview.component.HumanResources.subcomponent.ZonaBox]]
   *
   * @param zones
   *              List of [[caseclass.CaseClassDB.Zona]] to manage
   */
  private class ZonaBoxFX(zones: List[Zona])
    extends AbstractComponent[ZonaParent]("humanresources/subcomponent/ZonaBox") with ZonaBox {

    @FXML
    var zonaTable: TableView[ZonaTable] = _
    @FXML
    var zonaButton: Button = _
    @FXML
    var newName: TextField = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      initializeButton(resources)
      initializeSearch(resources)
      initializeTextField(resources)
      initializeTable()
    }

    private def initializeTable(): Unit = {
      val columnFields = List("id", "name")
      CreateTable.createColumns[ZonaTable](zonaTable, columnFields)
      CreateTable.fillTable[ZonaTable](zonaTable, zones)
      CreateTable.clickListener[ZonaTable](
        zonaTable,
        item => parent.openZonaModal(Zona(item.name.get, Some(item.id.get().toInt)))
      )
    }

    private def initializeTextField(resources: ResourceBundle): Unit = {
      newName.setPromptText(resources.getString("nametxt"))
      newName.textProperty().addListener((_, old, word) => {
        TextFieldControl.controlNewChar(newName, NameChecker, word, old)
        ableToSave()
      })
    }

    private def initializeButton(resources: ResourceBundle): Unit = {
      zonaButton.setText(resources.getString("add"))
      zonaButton.setDisable(true)
      zonaButton.setOnAction(_ => parent.newZona(Zona(newName.getText)))
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getString("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[ZonaTable](
          zonaTable,
          zones.filter(zone => zone.zones.contains(word) || zone.idZone.head.toString.contains(word)))
      })
    }

    ////////////////////////////////////////////////////////////////////////////////  Controll

    private def ableToSave(): Unit =
      zonaButton.setDisable(newName.getText().equals(""))

  }
}

