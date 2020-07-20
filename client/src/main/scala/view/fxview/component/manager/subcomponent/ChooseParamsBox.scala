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
      run.setOnAction(_ => println("eccolo"))
      old.setOnAction(_ => parent.modalOldParam())

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

      CreateDatePicker.createDatePickerFMD(initDate, LocalDate.now())
      enableTxtField()
    }

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
  }

}
