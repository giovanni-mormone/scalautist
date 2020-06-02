package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField}
import view.fxview.component.HumanResources.HRViewParent
import view.fxview.component.{AbstractComponent, Component}

import scala.language.postfixOps

trait RecruitBox extends Component[HRViewParent]{
  def setTerminals(l:List[Terminale])
}

object RecruitBox{

  def apply(contractList: List[Contratto], shiftList: List[Turno], zoneList: List[Zona]): RecruitBox =
    new RecruitBoxImpl(contractList, shiftList, zoneList)

  private class RecruitBoxImpl(contractList: List[Contratto], shiftList: List[Turno], zoneList: List[Zona])
    extends AbstractComponent[HRViewParent]("humanresources/subcomponent/RecruitBox") with RecruitBox {

    @FXML
    var name: TextField = _
    @FXML
    var surname: TextField = _
    @FXML
    var contractTypes: ComboBox[String] = _
    @FXML
    var shift1: ComboBox[String] = _
    @FXML
    var shift2: ComboBox[String] = _
    @FXML
    var day1: ComboBox[String] = _
    @FXML
    var day2: ComboBox[String] = _
    @FXML
    var zones: ComboBox[String] = _
    @FXML
    var terminals: ComboBox[String] = _
    @FXML
    var role: ComboBox[String] = _
    @FXML
    var tel: TextField = _
    @FXML
    var save: Button = _

    private var terminalList = List[Terminale]()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      fillComboBox
      setActions
      setPromptText(resources)
    }

    override def setTerminals(l: List[Terminale]): Unit = {
      terminalList = l
      terminals.getSelectionModel.clearSelection
      terminals.getItems.clear()
      l.flatMap(t=>List(t.nomeTerminale)).foreach(t => terminals.getItems.add(t))
      terminals.setDisable(false)
    }

    private def fillComboBox: Unit = {
      //default values
      contractList.foreach(contract => contractTypes.getItems.add(contract.tipoContratto))
      shiftList.foreach(shift => shift1.getItems.add(shift.fasciaOraria))
      shiftList.foreach(shift => shift2.getItems.add(shift.fasciaOraria))
      zoneList.foreach(zone => zones.getItems.add(zone.zones))
      day1.getItems.addAll("Lun","Mar","Mer","Gio","Ven")  //TODO da rivedere
      day2.getItems.addAll("Lun","Mar","Mer","Gio","Ven")  //TODO da rivedere
      role.getItems.addAll("risorseUmane", "manager", "autista") //TODO da rivedere
      shift2.setDisable(true)
      terminals.setDisable(true)
    }

    private def setActions: Unit = {
      //default action
      zones.setOnAction(_ => parent.loadTerminals(getIdZone))
      day1.setOnAction(_ => {
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day1.getSelectionModel.clearSelection
      })
      day2.setOnAction(_ =>{
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day2.getSelectionModel.clearSelection
      })
      save.setOnAction(event => {
        if(noEmptyField)  //TODO
          parent.recruitClicked( Persona(name.getText, surname.getText(), tel.getText(), None, //TODO creare entità assumi
            role.getSelectionModel.getSelectedIndex, true, "", getIdTerminal()))
      })
    }

    private def setPromptText(resources: ResourceBundle): Unit = {
      //set prompt test
      name.setPromptText(resources.getString("name"))
      surname.setPromptText(resources.getString("surname"))
      contractTypes.setPromptText(resources.getString("contract"))
      shift1.setPromptText(resources.getString("shift"))
      shift2.setPromptText(resources.getString("shift"))
      zones.setPromptText(resources.getString("zones"))
      role.setPromptText(resources.getString("role"))
      day1.setPromptText(resources.getString("day1"))
      day2.setPromptText(resources.getString("day2"))
      terminals.setPromptText(resources.getString("terminals"))
      tel.setPromptText(resources.getString("tel"))
      save.setText(resources.getString("save"))
    }

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def noEmptyField: Boolean = {
      true
    }

    private def getIdZone: Zona =
      zoneList.filter(zone => zone.zones.equals(zones.getSelectionModel.getSelectedItem)).head

    private def getIdTerminal(): Option[Int] ={
      terminalList.filter(terminal =>
        terminal.nomeTerminale.equals(terminals.getSelectionModel.getSelectedItem)).head.idTerminale
    }

  }
}
