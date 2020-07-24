package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Regola, Terminale}
import caseclass.CaseClassHttpMessage.AlgorithmExecute
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextArea}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent
import view.fxview.util.ResourceBundleUtil._

trait ShowParamAlgorithmBox  extends Component[ShowParamAlgorithmBoxParent] {

}

object ShowParamAlgorithmBox {

  def apply(info: AlgorithmExecute, name: Option[String], rules: List[Regola], terminal: List[Terminale]): ShowParamAlgorithmBox =
    new ShowParamAlgorithmBoxFX(info, name, rules, terminal)

  private class ShowParamAlgorithmBoxFX (info: AlgorithmExecute, name: Option[String], rules: List[Regola], terminal: List[Terminale])
    extends AbstractComponent[ShowParamAlgorithmBoxParent](path = "manager/subcomponent/ShowParamsBox")
    with ShowParamAlgorithmBox {

    @FXML
    var sabato: Label = _
    @FXML
    var nome: Label = _
    @FXML
    var terminals: TextArea = _
    @FXML
    var group: TextArea = _
    @FXML
    var weekN: TextArea = _
    @FXML
    var weekS: TextArea = _
    @FXML
    var run: Button = _
    @FXML
    var reset: Button = _

    private val NONE: String = "NONE"

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initLabel()
      initTextArea()
      initButton()
    }

    private def initLabel(): Unit = {
      if(info.regolaTreSabato) sabato.setText(resources.getResource("sabatotxt"))
      name.fold()(nameP => nome.setText(resources.getResource("nametxt") + " " + nameP))
    }

    private def writeOnTextArea(textArea: TextArea, title: String, strings: List[String]): Unit =
      if(strings.isEmpty)
        textArea.setText(title + "\n")
      else
        textArea.setText(title + "\n" + strings.reduce((finalS, str) => finalS + "\n" + str))

    private def initTextArea(): Unit = {
      terminals.setEditable(false)
      group.setEditable(false)
      weekN.setEditable(false)
      weekS.setEditable(false)

      writeOnTextArea(terminals, resources.getResource("terminaltxt"), info.idTerminal.map(_.toString))
      writeOnTextArea(group, resources.getResource("grouptxt"),
        info.gruppo.fold(List.empty[String])(_.flatMap(_.date.map(_.toString))))
      writeOnTextArea(weekN, resources.getResource("nweek"),
        info.settimanaNormale.fold(List.empty[String])
        (_.map(day => day.idDay + " " + getShiftName(day.turnoId) + " " + day.quantita + " " + day.regola)))
      writeOnTextArea(weekS, resources.getResource("sweek"),
        info.settimanaSpeciale.fold(List.empty[String])
        (_.map(day=> day.date.toString + " " + getShiftName(day.turnoId) + " " + day.quantita + " " + day.regola)))
    }

    private def initButton(): Unit = {
      run.setText(resources.getResource("runtxt"))
      reset.setText(resources.getResource("resettxt"))

      reset.setOnAction(_ => parent.resetParams())
    }

    private def getShiftName(key: Int): String = {
      val SHIFT_STRING_MAP: Map[Int, String] = Map(1 -> "2-6", 2 -> "6-10", 3 -> "10-14", 4 -> "14-18", 5 -> "18-22", 6 -> "22-2")
      SHIFT_STRING_MAP.getOrElse(key, NONE)
    }

  }
}
