package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Zona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView, TextField}
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.parent.ZonaParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, ZonaTable}
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

  private class ZonaBoxFX(zones: List[Zona]) extends AbstractComponent[ZonaParent]("humanresources/subcomponent/ZonaBox")
    with ZonaBox {

    @FXML
    var zonaTable: TableView[ZonaTable] = _
    @FXML
    var zonaButton: Button = _
    @FXML
    var newNome: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      zonaButton.setText(resources.getString("add"))
      zonaButton.setDisable(true)
      zonaButton.setOnAction(_ => parent.newZona(Zona(newNome.getText)))

      newNome.setPromptText(resources.getString("nametxt"))
      newNome.textProperty().addListener((_, old, word) => {
        if(!ZonaChecker.checkRegex.matches("" + word.last))
          newNome.setText(old)
        ableToSave
      })

      val columnFields = List("id", "name")
      CreateTable.createColumns[ZonaTable](zonaTable, columnFields)
      CreateTable.fillTable[ZonaTable](zonaTable, zones)
    }

    private def ableToSave: Unit =
      zonaButton.setDisable(newNome.getText().equals(""))
  }
}

