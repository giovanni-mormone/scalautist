package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Regola, Terminale}
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Button, CheckBox, Label, TableView, TextArea, TextField}
import javafx.scene.layout.{HBox, VBox}
import view.fxview.component.AbstractComponent
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.manager.subcomponent.parent.ModalParamParent
import view.fxview.component.manager.subcomponent.util.ParamsTable
import view.fxview.util.ResourceBundleUtil._

trait ParamsModal extends AbstractComponent[ModalParamParent] {

}

object ParamsModal {

  def apply(oldsParam: List[InfoAlgorithm], terminals: List[Terminale], rules: List[Regola]): ParamsModal =
    new ParamsModalFX(oldsParam, terminals, rules)

  private class ParamsModalFX(paramsList: List[InfoAlgorithm], temrinals: List[Terminale], rules: List[Regola])
    extends AbstractComponent[ModalParamParent]("manager/subcomponent/ParamsModal")
    with ParamsModal {

    @FXML
    var params: TableView[ParamsTable] = _
    @FXML
    var open: Button = _
    @FXML
    var days: VBox = _
    @FXML
    var terminals: VBox = _
    @FXML
    var terminalColumn1: Label = _
    @FXML
    var terminalColumn2: Label = _
    @FXML
    var dayColumn1: Label = _
    @FXML
    var dayColumn2: Label = _
    @FXML
    var dayColumn3: Label = _
    @FXML
    var sabato: CheckBox = _
    @FXML
    var rule: TextField = _
    @FXML
    var terminalsHeader: HBox = _
    @FXML
    var daysHeader: HBox = _

    var daysStringMap: Map[Int, String] = Map.empty
    val shiftStringMap: Map[Int, String] = Map(1 -> "2-6", 2 -> "6-10", 3 -> "10-14", 4 -> "14-18", 5 -> "18-22", 6 -> "22-2")

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initButton()
      initTable()
      initCheckBox()
      initTextField()
    }

    private def initButton(): Unit = {
      open.setText(resources.getResource(key = "buttontxt"))
      open.setDisable(true)
      open.setOnAction(_ => selectedItem(selectedItemId()).fold()(info => parent.loadParam(info)))
    }

    private def initTable(): Unit = {
      val fieldsList: List[String] = List("id", "name")
      CreateTable.createColumns[ParamsTable](params, fieldsList)
      CreateTable.fillTable[ParamsTable](params, paramsList.map(_.parametro))
      params.getSelectionModel.selectedItemProperty().addListener((_,_,_) => {
        enableButton()
        showParamsInfo(selectedItemId())
      })
    }

    private def initCheckBox(): Unit = {
      sabato.setText(resources.getResource("sabato"))
      sabato.setDisable(true)
    }

    private def initTextField(): Unit = {
      rule.setEditable(false)
    }

    private def initDays(): Unit =
      daysStringMap = Map(1 -> resources.getResource("monday"),
        2 -> resources.getResource("tuesday"), 3 -> resources.getResource("wednesday"),
        4 -> resources.getResource("thursday"), 5 -> resources.getResource("friday"),
        6 -> resources.getResource("saturday"), 7 -> resources.getResource("sunday"))

    private def enableButton(): Unit =
      open.setDisable(params.getSelectionModel.getSelectedItem == null)

    private def showInTextArea(textArea: TextArea, elements: List[String], init: String = ""): Unit = {
      val text: String = init + elements.reduce( (data, el) => data + "\n" + el)
      textArea.setText(text)
    }

    private def showParamsInfo(idp: Int): Unit = {
      initDays()
      val INIT_DAY: String = resources.getResource(key = "daystxtA") + "\n"
      val INIT_TERMINAL: String = resources.getResource(key = "terminaltxtA") + "\n"
      val NONE: String = "NONE"

      val chosen = selectedItem(idp)
      if (chosen.isDefined) {

        terminals.getChildren.clear()
        terminalColumn1.setText(resources.getResource("terminal-label-id"))
        terminalColumn2.setText(resources.getResource("terminal-label-name"))
        terminals.getChildren.addAll(terminalsHeader)
        temrinals.collect{
                    case terminal if chosen.exists(x => x.zonaTerminale.exists(zt => terminal.idTerminale.contains(zt.terminaleId))) =>
                      terminal.idTerminale.foreach( x => terminals.getChildren.add(TerminalModalLabels(x.toString,terminal.nomeTerminale).setParent(parent).pane))
        }

        days.getChildren.clear()
        dayColumn1.setText(resources.getResource("day-label"))
        dayColumn2.setText(resources.getResource("shift-label"))
        dayColumn3.setText(resources.getResource("variation-label"))
        days.getChildren.addAll(daysHeader)
        chosen.toList.flatMap(_.giornoInSettimana).flatten.foreach(info =>
          days.getChildren.add(DayInWeekModalLabels(daysStringMap.getOrElse(info.giornoId,""),
            shiftStringMap.getOrElse(info.turnoId,""),
            info.quantita.toString).setParent(parent).pane))

        sabato.setSelected(chosen.head.parametro.treSabato)
        rules.find(_.idRegola.contains(chosen.head.giornoInSettimana.head.head.regolaId))
          .fold(rule.setText(NONE))(rulef => rule.setText(rulef.nomeRegola))
      }
    }

    private def selectedItem(idp: Int): Option[InfoAlgorithm] = {
      paramsList.find(_.parametro.idParametri.contains(idp))
    }

    private def selectedItemId(): Int = {
      params.getSelectionModel.getSelectedItem.id.get().toInt
    }
  }

}
