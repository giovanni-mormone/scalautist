package view.fxview.component.manager.subcomponent

import java.lang.Math.abs
import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Terminale
import caseclass.CaseClassHttpMessage.ResultAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, DatePicker, Label}
import javafx.scene.layout.VBox
import view.fxview.component.manager.subcomponent.parent.SelectResultParent
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait SelectResultBox extends Component[SelectResultParent]{
  def createResult(resultList: List[ResultAlgorithm], dateList: List[Date]):Unit
}
object SelectResultBox{

  def apply(terminal: List[Terminale]): SelectResultBox =  new SelectResultBoxFX(terminal)

  private class SelectResultBoxFX(terminals: List[Terminale]) extends AbstractComponent[SelectResultParent]("manager/subcomponent/SelectResultBox")
    with SelectResultBox {

    @FXML
    var dateAndTerminal:VBox=_
    @FXML
    var title: Label = _
    @FXML
    var datepickerInit: DatePicker = _
    @FXML
    var datepickerFinish: DatePicker = _
    @FXML
    var terminal: ComboBox[String]  = _
    @FXML
    var carica: Button = _
    @FXML
    var errorLabel: Label = _

    val TERMINAL_SELECTED= true

    val TERMINAL_NOT_SELECTED= false

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      title.setText(resources.getResource("title-label"))
      carica.setText(resources.getResource("carica-button"))
      terminals.foreach(value=>terminal.getItems.add(value.nomeTerminale))
      carica.setOnAction(_=>callParent())
    }
    private def callParent(): Unit =
      checkSelectDatepickerAndCheckBox() match {
        case (Some((date,date1)),TERMINAL_SELECTED) if checkDatepicker() =>
          clearErrorLabel()
          parent.resultForTerminal(terminals.find(terminalS => terminal.getSelectionModel.getSelectedItem.equals(terminalS.nomeTerminale))
            .flatMap(_.idTerminale),Date.valueOf(date),Date.valueOf(date1))
        case (Some((_,_)),TERMINAL_SELECTED) =>
          errorLabel.setText(resources.getResource("error-greater-date"))
        case (None ,TERMINAL_SELECTED) =>clearErrorLabel()
          errorLabel.setText(resources.getResource("error-datepicker"))
        case (Some((_,_)),TERMINAL_NOT_SELECTED)=>clearErrorLabel()
          errorLabel.setText(resources.getResource("error-terminal"))
        case (None,TERMINAL_NOT_SELECTED)=>errorLabel.setText(resources.getResource("error-datepicker"))
          errorLabel.setText(resources.getResource("error-terminal"))
      }

    private def clearErrorLabel(): Unit ={
      errorLabel.setText("")
    }

    private def checkDatepicker():Boolean ={
      abs(datepickerInit.getValue.getDayOfYear-datepickerFinish.getValue.getDayOfYear)>=1
    }
    private def checkSelectDatepickerAndCheckBox(): (Option[(LocalDate, LocalDate)], Boolean) =
      (Option(datepickerInit.getValue).zip(Option(datepickerFinish.getValue)), !terminal.getItems.isEmpty)

    override def createResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit = {
      val resultBox = ResultBox(resultList,dateList)
      dateAndTerminal.getChildren.add(resultBox.setParent(parent).pane)
    }
  }
}