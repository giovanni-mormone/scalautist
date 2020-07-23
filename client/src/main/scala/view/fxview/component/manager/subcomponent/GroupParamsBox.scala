package view.fxview.component.manager.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import caseclass.CaseClassHttpMessage.GruppoA
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Button, TableView}
import javafx.scene.layout.AnchorPane
import view.fxview.component.HumanResources.subcomponent.util.{CreateDatePicker, CreateTable}
import view.fxview.component.manager.subcomponent.parent.GroupParamsParent
import view.fxview.component.manager.subcomponent.util.{GroupSelectionTable, ParamsForAlgoritm}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait GroupParamsBox extends Component[GroupParamsParent] {

}

object GroupParamsBox {

  def apply(params: ParamsForAlgoritm, rule: List[Regola]): GroupParamsBox =
    new GroupParamsBoxFX(params, rule)

  private class GroupParamsBoxFX(params: ParamsForAlgoritm, rule: List[Regola])
    extends AbstractComponent[GroupParamsParent](path = "manager/subcomponent/ParamGruppo" )
    with GroupParamsBox{

    @FXML
    var groupstab: TableView[GroupSelectionTable] = _
    @FXML
    var add: Button = _
    @FXML
    var sub: Button = _
    @FXML
    var reset: Button = _
    @FXML
    var run: Button = _
    @FXML
    var dateg: AnchorPane = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initButtons()
      initTable()
      initDate()
    }

    private def initButtons(): Unit = {
      add.setText(resources.getResource("addtxt"))
      sub.setText(resources.getResource("subtxt"))
      reset.setText(resources.getResource("resetftxt"))
      run.setText(resources.getResource("runftxt"))

      reset.setOnAction(_ => parent.resetGroupsParams())
      add.setOnAction(_ => parent.openModal(params.dateI, params.dateF))
    }

    private def initTable(): Unit = {
      val fieldList = List("id", "rule", "date")
      CreateTable.createColumns[GroupSelectionTable](groupstab, fieldList, dim= 50)
    }

    private def initDate(group: Option[GruppoA] = None): Unit = {
      val datePicker: DatePickerSkin =
        CreateDatePicker.changingDatePickerSkin(group.fold(List.empty[LocalDate])(_.date.map(_.toLocalDate)))._1
      val node: Node = datePicker.getPopupContent
      dateg.getChildren.add(node)
    }
  }
}