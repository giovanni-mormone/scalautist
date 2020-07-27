package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.{GiornoInSettimana, Terminale}
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import javafx.fxml.FXML
import javafx.scene.control._
import org.controlsfx.control.CheckComboBox
import regularexpressionutilities.ZonaChecker
import view.fxview.component.HumanResources.subcomponent.util.{CreateDatePicker, TextFieldControl}
import view.fxview.component.manager.subcomponent.parent.ChooseParamsParent
import view.fxview.component.manager.subcomponent.util.ParamsForAlgorithm
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/**
 * @author Francesco Cassano
 *
 * trait that allow to perform operation on the view. Extends [[view.fxview.component.Component]]
 */
trait ChooseParamsBox extends Component[ChooseParamsParent] {

  /**
   * Load params chosen in modal and save information to use in the rest of session
   * @param param instance of [[InfoAlgorithm]] that contains information chosen
   */
  def loadParam(param: InfoAlgorithm): Unit
}

/**
 * Companion object of [[ChooseParamsBox]]
 */
object ChooseParamsBox {

  def apply(terminalsList: List[Terminale]): ChooseParamsBox = new ChooseParamsBoxFX(terminalsList)

  /**
   * Class that implements javaFX version of [[ChooseParamsBox]]
   * @param terminalsList
   *                 list of zones of type [[caseclass.CaseClassDB.Terminale]]
   */
  private class ChooseParamsBoxFX(terminalsList: List[Terminale])
    extends AbstractComponent[ChooseParamsParent](path = "manager/subcomponent/ChooseParamsBox")
      with ChooseParamsBox {

    @FXML
    var initDate: DatePicker = _
    @FXML
    var endDate: DatePicker = _
    @FXML
    var terminals: CheckComboBox[String] = _
    @FXML
    var sabato: CheckBox = _
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

    private var days: Option[List[GiornoInSettimana]] = None
    private var ruleN: Option[Int] = None

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initText()
      resetComponent()
      initAction()
    }

    private def initText(): Unit = {
      sabato.setText(resources.getResource(key = "txtcheck"))
      saveName.setText(resources.getResource(key = "txtsave"))
      old.setText(resources.getResource(key = "txtold"))
      reset.setText(resources.getResource(key = "txtreset"))
      run.setText(resources.getResource(key = "txtrun"))
    }

    private def initAction(): Unit = {
      import javafx.collections.ListChangeListener
      //combobox
      terminals.getCheckModel.getCheckedItems.addListener(
      new ListChangeListener[String]() {
        override def onChanged(c: ListChangeListener.Change[_ <: String]): Unit =
          enableAlgorithm()
      })

      //buttons
      reset.setOnAction(_ => resetComponent())
      run.setOnAction(_ => parent.weekParams(getParams))
      old.setOnAction(_ => parent.modalOldParam(terminalsList))

      //checkbox e radiobutton
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
      insertTerminals()

      saveName.selectedProperty().setValue(false)
      sabato.selectedProperty().setValue(false)

      endDate.setDisable(true)
      run.setDisable(true)

      initDate.getEditor.clear()
      initDate.setValue(null)
      endDate.getEditor.clear()
      endDate.setValue(null)

      CreateDatePicker.createDatePickerFMD(initDate, LocalDate.now())
      enableTxtField()
    }

    private def getTerminals: List[Terminale] =
      terminalsList.filter(terminal => terminals.getCheckModel.getCheckedItems.contains(terminal.nomeTerminale))

    private def getDate(picker: DatePicker): Option[LocalDate] =
      Option(picker.getValue)

    private def saveControl(): Boolean =
      if(saveName.isSelected)
        !name.getText.equals("")
      else
        true

    private def getName: Option[String] =
      if(saveName.isSelected)
        Option(name.getText)
      else
        None

    private def enableAlgorithm(): Unit =
      if (getTerminals.nonEmpty && getDate(initDate).isDefined &&
        getDate(endDate).isDefined && saveControl())
        run.setDisable(false)
      else
        run.setDisable(true)

    private def enableTxtField(): Unit = {
      name.setDisable(true)
      name.setText("")
      name.setPromptText(resources.getResource(key = "txtname"))
    }

    private def insertTerminals(): Unit = {
      terminals.setDisable(false)
      terminals.getCheckModel.clearChecks()
      terminals.getItems.clear()
      terminalsList.foreach(terminal => terminals.getItems.add(terminal.nomeTerminale))
    }

    override def loadParam(param: InfoAlgorithm): Unit = {
      sabato.setSelected(param.parametro.treSabato)

      terminals.getCheckModel.clearChecks()
      terminalsList.collect{
        case terminal if param.zonaTerminale.exists(zTerm => terminal.idTerminale.contains(zTerm.terminaleId)) =>
          terminal.nomeTerminale
      }.foreach(toSelect => terminals.getItemBooleanProperty(toSelect).set(true))

      days = param.giornoInSettimana
      ruleN = param.giornoInSettimana.collect {
        case head :: _ => head.regolaId
      }
    }

    private def getParams: ParamsForAlgorithm =
      ParamsForAlgorithm(initDate.getValue, endDate.getValue, getTerminals, sabato.isSelected, getName, days)
  }

}
