package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Parametro, Regola, Terminale}
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

  case class DataForParamasModel(oldsParam: List[Parametro], terminals: List[Terminale],
                                 rules: List[Regola], info: Option[InfoAlgorithm] = None)

  def apply(data: DataForParamasModel): ParamsModal = new ParamsModalFX(data)

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
    var sabato: CheckBox = _
    @FXML
    var rule: TextField = _
    @FXML
    var terminalsHeader: HBox = _
    @FXML
    var daysHeader: HBox = _

    var daysStringMap: Map[Int, String] = Map.empty
    var infoAlgorithm: InfoAlgorithm = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      data.info.fold()(i => infoAlgorithm = i)
      initButton()
      initTable()
      initCheckBox()
      initTextField()
    }

    private def initButton(): Unit = {
      open.setText(resources.getResource(key = "buttontxt"))
      open.setDisable(true)
      open.setOnAction(_ => selectedItem(selectedItemId()).fold()(info => parent.loadParam(infoAlgorithm)))
    }

    private def initTable(): Unit = {
      val fieldsList: List[String] = List("id", "name")
      CreateTable.createColumns[ParamsTable](params, fieldsList)
      CreateTable.fillTable[ParamsTable](params, data.oldsParam)
      params.getSelectionModel.selectedItemProperty().addListener((_,_,_) => {
        enableButton()
        showParamsInfo(selectedItemId())
      })
      data.info.fold()(_.parametro.idParametri.fold()(id => data.oldsParam.find(_.idParametri.contains(id))
        .fold()(param => params.getSelectionModel.select(param))))
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
