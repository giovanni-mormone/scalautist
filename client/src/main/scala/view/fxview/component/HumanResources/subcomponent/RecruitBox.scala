package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, TextField, TextFormatter}
import regularexpressionutilities.{NameChecker, NumberChecker}
import view.fxview.component.HumanResources.HRViewParent
import view.fxview.component.{AbstractComponent, Component}

import scala.language.postfixOps

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
    var tel: TextField = _
    @FXML
    var role: ComboBox[String] = _
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
    var save: Button = _

    private var terminalList = List[Terminale]()
    private val fixedString = "-Fisso"
    private val rotateString = "-Rotazione"
    private var contractType: (Boolean, Boolean, Boolean) = _   //fisso/rotazione, 5x2/6x1, fulltime/partime

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      initializeComboBox
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

    private def initializeComboBox: Unit = {
      //default values
      shiftList.foreach(shift => shift1.getItems.add(shift.fasciaOraria))
      shiftList.foreach(shift => shift2.getItems.add(shift.fasciaOraria))
      zoneList.foreach(zone => zones.getItems.add(zone.zones))
      role.getItems.addAll("risorseUmane", "manager", "autista") //TODO da rivedere le stringhe
      initialBlockComponent
    }

    private def setActions: Unit = {
      //default action
      name.textProperty().addListener((_,oldS,word) => {
        if (!NameChecker.nameRegex.matches(word.substring(word.size - 1)))
          name.setText(oldS)
      })

      surname.textProperty().addListener((_,oldS,word) => {
        if (!NameChecker.nameRegex.matches(word.substring(word.size - 1)))
          surname.setText(oldS)
      })

      tel.textProperty().addListener((_,oldS,word) => {
        if (!NumberChecker.numberRegex.matches(word.substring(word.size - 1)) || word.size > 10)
          tel.setText(oldS)
      })

      zones.setOnAction(_ => {
        parent.loadTerminals(getIdZone)
        ableSave
      })

      role.setOnAction(_ => {
        if(getComboSelected(role).equals("risorseUmane") || getComboSelected(role).equals("manager"))  //TODO da rivedere no alle stringhe
          notDrive(true)
        else
          notDrive(false)
        ableSave
      })

      contractTypes.setOnAction(_ => {
        contractControl(getComboSelected(contractTypes))
        ableSave
      })

      day1.setOnAction(_ => {
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day1.getSelectionModel.clearSelection
        ableSave
      })

      day2.setOnAction(_ => {
        if (getComboSelected(day1).equals(getComboSelected(day2)))
          day2.getSelectionModel.clearSelection
        ableSave
      })

      shift1.setOnAction(_ => {
        val itemSelected: Int = getComboSelectedIndex(shift1)
        if(contractType._3)
          if( itemSelected == shift1.getItems.size - 1)
            shift2.getSelectionModel.selectFirst()
          else
            shift2.getSelectionModel.select(itemSelected + 1)
        ableSave
      })

      terminals.setOnAction(_ => ableSave)

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

    /////////////////////////////////////////////////////////////////////////////             METODI DI CONTROLLO

    private def contractControl(contract: String): Unit = {
      if(getComboSelected(role).equals("autista")){
        val workWeek: String = "5x2"    //tutti i 5x2 da lunedi a venerdi
        val typeWork: String = "Full"   //tutti i 6x1 da lunedi a sabato
        var fisso: Boolean = false
        var settimana5x2: Boolean = false
        var full: Boolean = false

        if(chosenSomething(contractTypes)) {
          if (contract.contains(fixedString))
            fisso = true
          if (contract.contains(workWeek))
            settimana5x2 = true
          if (contract.contains(typeWork))
            full = true
        }

        contractType = (fisso, settimana5x2, full)

        fixedShift(contractType._1)
        if(contractType._1)
          refillDays
      }
    }

    private def controlMainFields(): Boolean = {
      !name.getText.equals("") && !surname.getText.equals("") &&
        !contractTypes.getSelectionModel.isEmpty && !tel.getText.equals("")
    }

    private def controlDriverFields(): Boolean = {
      var terminalChosen: Boolean = chosenSomething(terminals)
      if(contractType._1 && terminalChosen){
        terminalChosen = chosenSomething(shift1) && chosenSomething(day2) && chosenSomething(day1)
        if(contractType._3 && terminalChosen)
          terminalChosen = chosenSomething(shift2)
      }
      terminalChosen
    }

    private def noEmptyField: Boolean = {
      if(role.getSelectionModel.isEmpty)
        false
      else if(getComboSelected(role).equals("risorseUmane") || getComboSelected(role).equals("manager"))
        controlMainFields()
      else
        controlMainFields() && controlDriverFields()
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI GET

    private def getIdZone: Zona =
      zoneList.filter(zone => zone.zones.equals(getComboSelected(zones))).head

    private def getIdTerminal(): Option[Int] ={
      terminalList.filter(terminal =>
        terminal.nomeTerminale.equals(getComboSelected(terminals))).head.idTerminale
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI BLOCCO COMPONENTI

    private def ableSave: Unit = {
      save.setDisable(!noEmptyField)
    }

    private def initialBlockComponent: Unit = {
      shift1.setDisable(true)
      shift2.setDisable(true)
      zones.setDisable(true)
      contractTypes.setDisable(true)
      day1.setDisable(true)
      day2.setDisable(true)
      terminals.setDisable(true)
      save.setDisable(true)
    }

    private def refillDays: Unit = {
      var baseDays  = List("Lun","Mar","Mer","Gio","Ven")   //TODO da rivedere
      if(!contractType._2)
        baseDays = baseDays.appended("Sab")
      refillComponent(day2, baseDays)
      refillComponent(day1, baseDays)
    }

    private def notDrive(value: Boolean): Unit = {
      contractTypes.setDisable(false)
      if(!value)
        refillComponent(contractTypes, contractList.map(contract =>
          contract.tipoContratto + (if(contract.turnoFisso) fixedString else rotateString)))
      else {
        refillComponent(contractTypes, List(getComboSelected(role)))
        contractTypes.getSelectionModel.selectFirst()
        shift1.setDisable(value)
        day1.setDisable(value)
        day2.setDisable(value)
        terminals.setDisable(value)
        resetComboBox(day1)
        resetComboBox(day2)
      }

      zones.setDisable(value)
      resetComboBox(day1)
      resetComboBox(day2)
      resetComboBox(zones)
      resetComboBox(terminals)
    }

    private def fixedShift(value: Boolean): Unit = {
      shift1.setDisable(!value)
      day1.setDisable(!value)
      day2.setDisable(!value)
      resetComboBox(shift1)
      resetComboBox(shift2)
    }

    /////////////////////////////////////////////////////////////////////////////             METODI DI UTILS

    private def refillComponent(component: ComboBox[String], list: List[String]): Unit = {
      resetComboBox(component)
      emptyComboBox(component)
      list.foreach(day => component.getItems.add(day))
      component.setDisable(false)
    }

    private def chosenSomething(component: ComboBox[String]): Boolean =
      !component.getSelectionModel.isEmpty

    private def emptyComboBox(component: ComboBox[String]): Unit =
      component.getItems.clear

    private def resetComboBox(component: ComboBox[String]): Unit =
      component.getSelectionModel.clearSelection

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def getComboSelectedIndex(component: ComboBox[String]): Int =
      component.getSelectionModel.getSelectedIndex

  }
}
