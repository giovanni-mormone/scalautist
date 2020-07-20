package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Terminale, Zona}
import javafx.fxml.FXML
import javafx.scene.control._
import org.controlsfx.control.CheckComboBox
import regularexpressionutilities.{NameChecker, ZonaChecker}
import view.fxview.component.HumanResources.subcomponent.util.{CreateDatePicker, TextFieldControl}
import view.fxview.component.manager.subcomponent.parent.ChooseParamsParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/**
 * @author Francesco Cassano
 *
 * trait that allow to perform operation on the view. Extends [[view.fxview.component.Component]]
 */
trait ChooseParamsBox extends Component[ChooseParamsParent] {

  /**
   * Allow to insert the list of terminals
   * @param terminalsList
   *                      List of terminals [[caseclass.CaseClassDB.Terminale]]
   */
  def insertTerminals(terminalsList: List[Terminale]): Unit
}

/**
 * Companion object of [[ChooseParamsBox]]
 */
object ChooseParamsBox {

  def apply(zones: List[Zona]): ChooseParamsBox = new ChooseParamsBoxFX(zones)

  /**
   * Class that implements javaFX version of [[ChooseParamsBox]]
   * @param zoneList
   *                 list of zones of type [[caseclass.CaseClassDB.Zona]]
   */
  private class ChooseParamsBoxFX(zoneList: List[Zona])
    extends AbstractComponent[ChooseParamsParent](path = "manager/subcomponent/ChooseParamsBox")
      with ChooseParamsBox {

    @FXML
    var initDate: DatePicker = _
    @FXML
    var endDate: DatePicker = _
    @FXML
    var zones: ComboBox[String] = _
    @FXML
    var terminals: CheckComboBox[String] = _
    @FXML
    var sabato: CheckBox = _
    @FXML
    var normal: RadioButton = _
    @FXML
    var special: RadioButton = _
    @FXML
    var old: Button = _
    @FXML
    var reset: Button = _
    @FXML
    var run: Button = _
    @FXML
    var saveName: CheckBox = _
    @FXML
    var name: TextField = _

    var terminalsList: List[Terminale] = List.empty

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initText()
      resetComponent()
      initAction()
    }

    private def initText(): Unit = {
      sabato.setText(resources.getResource(key = "txtcheck"))
      normal.setText(resources.getResource(key = "txtnormal"))
      special.setText(resources.getResource(key = "txtspecial"))
      saveName.setText(resources.getResource(key = "txtsave"))
      old.setText(resources.getResource(key = "txtold"))
      reset.setText(resources.getResource(key = "txtreset"))
      run.setText(resources.getResource(key = "txtrun"))
    }

    private def initAction(): Unit = {
      import javafx.collections.ListChangeListener
      //combobox
      zones.setOnAction(_ => {
        val zone = getZone
        if (zone.isDefined)
          parent.getTerminals(zone.get)
      })
      terminals.getCheckModel.getCheckedItems.addListener(
      new ListChangeListener[String]() {
        override def onChanged(c: ListChangeListener.Change[_ <: String]): Unit =
          enableAlgorithm()
      })

      //buttons
      reset.setOnAction(_ => resetComponent())
      run.setOnAction(_ => println("eccolo"))
      old.setOnAction(_ => parent)

      //checkbox e radiobutton
      normal.setOnAction(_ => special.selectedProperty().setValue(false))
      special.setOnAction(_ => normal.selectedProperty().setValue(false))
      saveName.setOnAction(_ => {
        if (saveName.isSelected)
          name.setDisable(false)
        else
          enableTxtField()
        enableAlgorithm()
      })

      //date picker
      initDate.setOnAction(_ => {
        if (initDate.getValue != null) {
          CreateDatePicker.createDatePickerLMD(endDate, initDate.getValue)
          endDate.setDisable(false)
        }
      })
      endDate.setOnAction(_ => enableAlgorithm())

      //textfield
      name.textProperty().addListener((_, oldS, word) =>{
        TextFieldControl.controlNewChar(name, ZonaChecker, word, oldS)
        enableAlgorithm()
      })
    }

    private def resetComponent(): Unit = {
      zones.setPromptText(resources.getResource(key = "txtzones"))

      zones.getItems.clear()
      zoneList.foreach(zona => zones.getItems.add(zona.zones))
      terminals.getItems.clear()

      normal.selectedProperty().setValue(true)
      special.selectedProperty().setValue(false)
      saveName.selectedProperty().setValue(false)
      sabato.selectedProperty().setValue(false)

      terminals.setDisable(true)
      endDate.setDisable(true)
      run.setDisable(true)
      normal.selectedProperty().setValue(true)

      initDate.getEditor.clear()
      initDate.setValue(null)
      endDate.getEditor.clear()

      CreateDatePicker.createDatePickerFMD(initDate, LocalDate.now())
      enableTxtField()
    }

    private def getComboSelected(component: ComboBox[String]): String =
      component.getSelectionModel.getSelectedItem

    private def getZone: Option[Zona] =
      zoneList.find(zona => zona.zones.equals(getComboSelected(zones)))

    private def getTerminals: List[Terminale] =
      terminalsList.filter(terminal => terminals.getCheckModel.getCheckedItems.contains(terminal.nomeTerminale))

    private def getDate(picker: DatePicker): Option[LocalDate] =
      Option(picker.getValue)

    private def saveControl(): Boolean = {
      if(saveName.isSelected)
        !name.getText.equals("")
      else true
    }

    private def enableAlgorithm(): Unit =
      if (getZone.isDefined && getTerminals.nonEmpty && getDate(initDate).isDefined &&
        getDate(endDate).isDefined && saveControl())
        run.setDisable(false)
      else
        run.setDisable(true)

    private def enableTxtField(): Unit = {
      name.setDisable(true)
      name.setText("")
      name.setPromptText(resources.getResource(key = "txtname"))
    }

    override def insertTerminals(terminalsList: List[Terminale]): Unit = {
      terminals.setDisable(false)
      terminals.getItems.clear()
      this.terminalsList = terminalsList
      terminalsList.foreach(terminal => terminals.getItems.add(terminal.nomeTerminale))
    }
  }

}
