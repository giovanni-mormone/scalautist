package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util
import java.util.ResourceBundle
import java.util.regex.Pattern

import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField, TextFormatter}
import view.fxview.component.HumanResources.HRViewParent
import view.fxview.component.{AbstractComponent, Component}

import scala.language.postfixOps
import scala.util.matching.Regex

/**
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.HRViewParent]]
 */
trait RecruitBox extends Component[HRViewParent]{

  /**
   * Set a list of terminal in the view before the zone is chosen.
   *
   * @param l
   *          list of terminale to show
   */
  def setTerminals(l:List[Terminale]): Unit
}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.RecruitBox]]
 *
 */
object RecruitBox{

  def apply(contractList: List[Contratto], shiftList: List[Turno], zoneList: List[Zona]): RecruitBox =
    new RecruitBoxImpl(contractList, shiftList, zoneList)

  /**
   * RecruitBox Fx implementation. It shows Humane resource Recruit panel in home view
   *
   * @param contractList
   *                     list of type of contratto
   * @param shiftList
   *                  list of type of turno
   * @param zoneList
   *                 list of zona
   */
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
    private val fixedString = "-Fisso"
    private val rotateString = "-Rotazione"

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      fillComboBox
      setActions
      setPromptText(resources)
    }

    override def setTerminals(l: List[Terminale]): Unit = {
      terminalList = l
      resetComboBox(terminals)
      terminals.getItems.clear()
      l.flatMap(t=>List(t.nomeTerminale)).foreach(t => terminals.getItems.add(t))
      terminals.setDisable(false)
    }

    private def fillComboBox: Unit = {
      //default values
      contractList.foreach(contract =>
        contractTypes.getItems.add(contract.tipoContratto + (if(contract.turnoFisso) fixedString else rotateString)))
      shiftList.foreach(shift => shift1.getItems.add(shift.fasciaOraria))
      shiftList.foreach(shift => shift2.getItems.add(shift.fasciaOraria))
      zoneList.foreach(zone => zones.getItems.add(zone.zones))
      day1.getItems.addAll("Lun","Mar","Mer","Gio","Ven")  //TODO da rivedere
      day2.getItems.addAll("Lun","Mar","Mer","Gio","Ven")  //TODO da rivedere
      role.getItems.addAll("risorseUmane", "manager", "autista") //TODO da rivedere le stringhe
      notDrive(true)
      shift2.setDisable(true)
      terminals.setDisable(true)
    }

    private def setActions: Unit = {
      //default action
      zones.setOnAction(_ => parent.loadTerminals(getIdZone))

      role.setOnAction(_ => {
        if(getComboSelected(role).equals("risorseUmane") || getComboSelected(role).equals("manager")) { //TODO da rivedere no alle stringhe
          notDrive(true)
          terminals.setDisable(true)
        } else
          notDrive(false)
      })

      contractTypes.setOnAction(_ => contractControl(getComboSelected(contractTypes)))

      day1.setOnAction(_ => {
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day1.getSelectionModel.clearSelection
      })

      day2.setOnAction(_ => {
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day2.getSelectionModel.clearSelection
      })

      shift1.setOnAction(_ => {
        val itemSelected: Int = getComboSelectedIndex(shift1)
        if( itemSelected == shift1.getItems.size - 1)
          shift2.getSelectionModel.selectFirst()
        else
          shift2.getSelectionModel.select(itemSelected + 1)
      })

      save.setOnAction(event => {
        if(noEmptyField)  //TODO
          /*parent.recruitClicked*//*println( Persona(name.getText, surname.getText(), tel.getText(), None, //TODO creare entità assumi
            getComboSelectedIndex(role) + 1, true, "", getIdTerminal()))*/
          println("Ok dude! You recruit a big asshole " + name.getText + " " + surname.getText() + " " +
            tel.getText() + " " + getComboSelectedIndex(role) + 1)
        else
          println("holy shit man!! You can't fill a simple form? Are you an asshole?")
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

    def getContract(contract: String): Unit = {
      contractList.filter(contractE => contractE.tipoContratto.equals(contract)).head
    }

    private def contractControl(contract: String): Unit = {
      val workWeek: String = "5x2"    //tutti i 5x2 da lunedi a venerdi
      val typeWork: String = "Full"   //tutti i 6x1 da lunedi a sabato

      getContract(contract)

    }

    private def controlMainFields(): Boolean = {
      //val pattern: Pattern = Pattern.compile("-?\\d{10}?") //TODO controllo su tel
      !name.getText.equals("") && !surname.getText.equals("") && !contractTypes.getSelectionModel.isEmpty
        //pattern.matcher(tel.getText()).matches &&
    }

    private def controlDriverFields(): Boolean = {
      true
    }

    private def noEmptyField: Boolean = {
      if(role.getSelectionModel.isEmpty)
        false
      else if(getComboSelected(role).equals("risorseUmane") || getComboSelected(role).equals("manager"))
        controlMainFields()
      else
        controlMainFields() && controlDriverFields()
    }

    private def getIdZone: Zona =
      zoneList.filter(zone => zone.zones.equals(getComboSelected(zones))).head

    private def getIdTerminal(): Option[Int] ={
      terminalList.filter(terminal =>
        terminal.nomeTerminale.equals(getComboSelected(terminals))).head.idTerminale
    }

    private def notDrive(value: Boolean): Unit = {
      day1.setDisable(value)
      day2.setDisable(value)
      shift1.setDisable(value)
      zones.setDisable(value)
      resetComboBox(terminals)
      resetComboBox(day1)
      resetComboBox(day2)
      resetComboBox(zones)
      if(value)
        notFixed(value)
    }

    private def notFixed(value: Boolean): Unit = {
      shift1.setDisable(value)
      resetComboBox(shift1)
      resetComboBox(shift2)
    }

    private def resetComboBox(component: ComboBox[String]): Unit =
      component.getSelectionModel.clearSelection

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def getComboSelectedIndex(component: ComboBox[String]): Int =
      component.getSelectionModel.getSelectedIndex

  }
}
