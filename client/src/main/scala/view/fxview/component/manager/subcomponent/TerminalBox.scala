package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Terminale, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TableView, TextField}
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, TerminalTable, TextFieldControl}
import view.fxview.component.manager.subcomponent.parent.TerminalParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[TerminalParent]]
 */
trait TerminalBox extends Component[TerminalParent] {

}

/**
 * @author Francesco Cassano
 *
 *  Companion object of [[TerminalBox]]
 *
 */
object TerminalBox {

  def apply(zoneList: List[Zona], terminalList: List[Terminale]): TerminalBox = new TerminalBoxFX(zoneList, terminalList)

  /**
   * javaFX private implementation of [[TerminalBox]]
   *
   * @param zoneList
   *                 List of reference [[caseclass.CaseClassDB.Zona]]  of the terminals
   * @param terminalList
   *                     List of [[caseclass.CaseClassDB.Terminale]] to manage
   */
  private class TerminalBoxFX(zoneList: List[Zona], terminalList: List[Terminale])
    extends AbstractComponent[TerminalParent]("manager/subcomponent/TerminalBox") with TerminalBox {

    @FXML
    var terminalTable: TableView[TerminalTable] = _
    @FXML
    var terminalButton: Button = _
    @FXML
    var newName: TextField = _
    @FXML
    var searchBox: ComboBox[String] = _
    @FXML
    var addBox: ComboBox[String] = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      initilizePrompt(resources)

      setComboBox(searchBox, zoneList)
      searchBox.setOnAction(_ => {
        CreateTable.fillTable[TerminalTable](
          terminalTable,
          terminalList.filter(terminal => terminal.idZona == getZona(searchBox, zoneList))
        )
      })

      setComboBox(addBox, zoneList)
      addBox.setOnAction(_ => ableToSave())

      initializeTable()

      initializeTextField()

      initializeButton()
    }

    private def setComboBox(component: ComboBox[String], list: List[Zona]): Unit =
      list.foreach(zone => component.getItems.add(zone.zones))

    private def initializeTextField(): Unit =
      newName.textProperty().addListener((_, old, word) => {
        TextFieldControl.controlNewChar(newName, ZonaChecker, word, old)
        ableToSave()
      })

    private def initializeButton(): Unit = {
      terminalButton.setOnAction(_ => parent.newTerminale(Terminale(newName.getText, getZona(addBox, zoneList))))
      terminalButton.setDisable(true)
    }

    private def initializeTable(): Unit = {
      val fieldsList: List[String] = List("id", "name")
      CreateTable.createColumns[TerminalTable](terminalTable, fieldsList,250)
      CreateTable.fillTable[TerminalTable](terminalTable, terminalList)
      CreateTable.clickListener[TerminalTable](terminalTable,
        terminal  => parent.openTerminalModal(terminal.id.get().toInt)
      )
    }

    private def initilizePrompt(resources: ResourceBundle): Unit = {
      terminalButton.setText(resources.getResource("add"))
      newName.setPromptText(resources.getResource("nametxt"))
      searchBox.setPromptText(resources.getResource("chooseZone"))
      addBox.setPromptText(resources.getResource("chooseZone"))
    }

    /////////////////////////////////////////////////////////////////////////////////////////   Get

    private def getZona(component: ComboBox[String], list: List[Zona]) =
      list.filter(zone => zone.zones.equals(component.getSelectionModel.getSelectedItem)).head.idZone.get

    /////////////////////////////////////////////////////////////////////////////////////////   Controller

    private def ableToSave(): Unit =
      terminalButton.setDisable(newName.getText().equals("") || addBox.getSelectionModel.isEmpty)
  }
}
