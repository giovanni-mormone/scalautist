package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.CaseClassDB.Terminale
import javafx.fxml.FXML
import javafx.scene.control.{Button, ChoiceBox, ComboBox, DatePicker, Label}
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
    var terminal: ChoiceBox[Terminale] = _
    @FXML
    var next: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      title.setText(resources.getResource("title-label"))
      next.setText(resources.getResource("next-button"))
      next.setOnAction(_=>parent.terminalSelected(1,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis())))
    }
  }
}