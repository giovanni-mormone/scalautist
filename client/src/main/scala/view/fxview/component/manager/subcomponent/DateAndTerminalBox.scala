package view.fxview.component.manager.subcomponent

import java.lang.Math.abs
import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Terminale
import javafx.fxml.FXML
import javafx.scene.control._
import org.controlsfx.control.CheckComboBox
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
trait DateAndTerminalBox extends Component[ManagerRichiestaBox]{

}
object DateAndTerminalBox{

  def apply(terminals: List[Terminale]): DateAndTerminalBox = new DateAndTerminalBoxFX(terminals)

  private class DateAndTerminalBoxFX(terminals: List[Terminale]) extends AbstractComponent[ManagerRichiestaBox]("manager/subcomponent/DateAndTerminalBox")
    with DateAndTerminalBox{

    @FXML
    var title: Label = _
    @FXML
    var datepickerInit: DatePicker = _
    @FXML
    var datepickerFinish: DatePicker = _
    @FXML
    var terminal: CheckComboBox[String]  = _
    @FXML
    var next: Button = _
    @FXML
    var errorLabel: Label = _
    @FXML
    var errorLabelTerminal: Label = _

    val TERMINAL_SELECTED= true

    val TERMINAL_NOT_SELECTED= false

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      title.setText(resources.getResource("title-label"))
      next.setText(resources.getResource("next-button"))
      terminals.foreach(value=>terminal.getItems.add(value.nomeTerminale))


      next.setOnAction(_=>callParent())
    }
    private def callParent(): Unit =
      checkSelectDatepickerAndCheckBox() match {
        case (Some((date,date1)),TERMINAL_SELECTED) if checkDatepicker() =>
          clearErrorLabel()
          parent.terminalSelected(terminals.filter(terminalS => terminal.getCheckModel
            .getCheckedItems.contains(terminalS.nomeTerminale)).flatMap(_.idTerminale.toList)
            ,Date.valueOf(date),Date.valueOf(date1))
        case (Some((_,_)),TERMINAL_SELECTED) =>
          errorLabel.setText(resources.getResource("error-greater-date"))
        case (None ,TERMINAL_SELECTED) =>clearErrorLabel()
          errorLabel.setText(resources.getResource("error-datepicker"))
        case (Some((_,_)),TERMINAL_NOT_SELECTED)=>clearErrorLabel()
          errorLabelTerminal.setText(resources.getResource("error-terminal"))
        case (None,TERMINAL_NOT_SELECTED)=>errorLabel.setText(resources.getResource("error-datepicker"))
          errorLabelTerminal.setText(resources.getResource("error-terminal"))
      }

    private def clearErrorLabel(): Unit ={
      errorLabel.setText("")
      errorLabelTerminal.setText("")
    }

    private def checkDatepicker():Boolean ={
      abs(datepickerInit.getValue.getDayOfYear-datepickerFinish.getValue.getDayOfYear)>=28
    }
    private def checkSelectDatepickerAndCheckBox2():(Boolean,Boolean)=
      (datepickerInit.getValue != null && datepickerFinish.getValue != null,terminal.getCheckModel.getCheckedIndices.size()>0)
    private def checkSelectDatepickerAndCheckBox(): (Option[(LocalDate, LocalDate)], Boolean) =
      (Option(datepickerInit.getValue).zip(Option(datepickerFinish.getValue)),terminal.getCheckModel.getCheckedIndices.size()>0)
  }
}