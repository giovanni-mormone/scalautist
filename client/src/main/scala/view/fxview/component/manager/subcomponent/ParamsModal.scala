package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Parametro
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Button, CheckBox, Label, TableView}
import javafx.scene.layout.{HBox, VBox}
import utils.TransferObject.DataForParamasModel
import view.fxview.component.AbstractComponent
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.manager.subcomponent.parent.ModalParamParent
import view.fxview.component.manager.subcomponent.util.{ParamsTable, ShiftUtil}
import view.fxview.util.ResourceBundleUtil._

/**
 * @author Francesco Cassano
 *
 * trait that allow to perform operation on the view. Extends [[view.fxview.component.Component]]
 */
trait ParamsModal extends AbstractComponent[ModalParamParent] {

}

/**
 * Companion object of [[ParamsModal]]. The Object allows to choose old params to load
 */
object ParamsModal {

  def apply(data: DataForParamasModel): ParamsModal = new ParamsModalFX(data)

  /**
   * Java FX implementation for [[ParamsModal]]
   * @param data all info that allow to draw the view
   */
  private class ParamsModalFX(data: DataForParamasModel)
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
    var dayColumn4: Label = _
    @FXML
    var sabato: CheckBox = _
    @FXML
    var terminalsHeader: HBox = _
    @FXML
    var daysHeader: HBox = _

    var daysStringMap: Map[Int, String] = Map.empty
    var infoAlgorithm: InfoAlgorithm = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      data.info.foreach(info => infoAlgorithm = info)
      initButton()
      initTable()
      initCheckBox()
      data.info.foreach(info => {
        printParamsInfo(info)
      })
    }

    private def printParamsInfo(info: InfoAlgorithm): Unit = {
      initDays()
      val INIT_DAY: String = resources.getResource(key = "daystxtA") + "\n"
      val INIT_TERMINAL: String = resources.getResource(key = "terminaltxtA") + "\n"
      val NONE: String = resources.getResource(key = "none")

      terminals.getChildren.clear()
      terminalColumn1.setText(resources.getResource("terminal-label-id"))
      terminalColumn2.setText(resources.getResource("terminal-label-name"))
      terminals.getChildren.addAll(terminalsHeader)

      info.zonaTerminale.map(_.terminaleId)
        .foreach(idTer => data.terminals.find(_.idTerminale.contains(idTer))
          .foreach(terminal =>
            terminals.getChildren.add(TerminalModalLabels(idTer.toString, terminal.nomeTerminale).setParent(parent).pane)
          ))

      days.getChildren.clear()
      dayColumn1.setText(resources.getResource("day-label"))
      dayColumn2.setText(resources.getResource("shift-label"))
      dayColumn3.setText(resources.getResource("variation-label"))
      dayColumn4.setText(resources.getResource("rules-label"))
      days.getChildren.addAll(daysHeader)

      info.giornoInSettimana
        .foreach(_.foreach(day => {
          days.getChildren.add(DayInWeekModalLabels(daysStringMap.getOrElse(day.giornoId, NONE),
            ShiftUtil.getShiftName(day.turnoId),
            day.quantita.toString,
            data.rules.find(_.idRegola.contains(day.regolaId)).fold(NONE)(_.nomeRegola)
          ).setParent(parent).pane)
        }))

      sabato.setSelected(info.parametro.treSabato)

    }

    private def initButton(): Unit = {
      open.setText(resources.getResource(key = "buttontxt"))
      data.info.fold(open.setDisable(true))(_ => open.setDisable(false))
      open.setOnAction(_ => selectedItem(selectedItemId()).foreach(_ => {
        val n = infoAlgorithm
        parent.loadParam(infoAlgorithm)
      }))
    }

    private def initTable(): Unit = {
      val fieldsList: List[String] = List("id", "name")
      CreateTable.createColumns[ParamsTable](params, fieldsList)
      CreateTable.fillTable[ParamsTable](params, data.oldsParam)

      data.info.foreach(_.parametro.idParametri.foreach(id => {
        Option(CreateTable.getElements(params).toList.map(_.id.get().toInt).sorted.indexWhere(_ == id))
          .filter(_ >= 0).foreach(params.getSelectionModel.select)
      }))

      params.getSelectionModel.selectedItemProperty().addListener((_,_,_) => {
        showParamsInfo(selectedItemId())
      })

    }

    private def initCheckBox(): Unit = {
      sabato.setText(resources.getResource("sabato"))
      sabato.setDisable(true)
    }

    private def initDays(): Unit =
      daysStringMap = Map(1 -> resources.getResource("monday"),
        2 -> resources.getResource("tuesday"), 3 -> resources.getResource("wednesday"),
        4 -> resources.getResource("thursday"), 5 -> resources.getResource("friday"),
        6 -> resources.getResource("saturday"), 7 -> resources.getResource("sunday"))

    private def showParamsInfo(idp: Int): Unit = {
      parent.getInfoToShow(idp, data)
    }

    private def selectedItem(idp: Int): Option[Parametro] = {
      data.oldsParam.find(_.idParametri.contains(idp))
    }

    private def selectedItemId(): Int = {
      params.getSelectionModel.getSelectedItem.id.get().toInt
    }
  }

}
