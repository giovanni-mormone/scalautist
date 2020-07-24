package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
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
import view.fxview.component.manager.subcomponent.GroupParamsBox.Group
import view.fxview.component.manager.subcomponent.parent.GroupParamsParent
import view.fxview.component.manager.subcomponent.util.{GroupSelectionTable, GroupToTable, ParamsForAlgoritm}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait GroupParamsBox extends Component[GroupParamsParent] {

  def updateGroup(group: Group): Unit
}

object GroupParamsBox {

  case class Group(date: List[LocalDate], rule: Int, ruleName: String)

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

    private var groups: List[(Group, GroupToTable)] = List.empty[(Group, GroupToTable)]

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      groups = List.empty[(Group, GroupToTable)]
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
      add.setOnAction(_ => parent.openModal(params.dateI, params.dateF, allDate(), rule))
      sub.setOnAction(_ => {
        val toDelete = getSelectedRow
        groups = groups.filter(group => !findGroup(toDelete, group))
        CreateTable.fillTable[GroupSelectionTable](groupstab, groups.map(_._2))
      })
    }

    private def getSelectedRow: GroupSelectionTable = {
      groupstab.getSelectionModel.getSelectedItem
    }

    private def allDate(): List[LocalDate] =
      groups.flatMap(_._1.date)

    private def findGroup(toFind: GroupSelectionTable, group: (Group, GroupToTable)): Boolean = {
      if(toFind == null)
        return false
      group._2.regola.equals(toFind.rule.get()) &&
        toFind.date.get().equals(group._2.date.map(_.toLocalDate)
          .map(date => date.getDayOfMonth + "/" + date.getMonth + "/" + date.getYear).reduce((dl, d) => dl + "; " + d))
    }

    private def initTable(): Unit = {
      val fieldList = List("rule", "date")
      CreateTable.createColumns[GroupSelectionTable](groupstab, fieldList, dim= 50)
      groupstab.getSelectionModel.selectedItemProperty().addListener((_,_,_) => {
        initDate(groups.find(group => findGroup(getSelectedRow, group)).map(_._1))
      })
    }

    private def initDate(group: Option[Group] = None): Unit = {
      val datePicker: DatePickerSkin =
        CreateDatePicker.changingDatePickerSkin(group.fold(List.empty[LocalDate])(_.date))._1
      val node: Node = datePicker.getPopupContent
      dateg.getChildren.add(node)
    }

    override def updateGroup(group: Group): Unit = {
      val newG = (group, GroupToTable(group.ruleName, group.date.map(day => Date.valueOf(day))))
      groups = newG :: groups
      CreateTable.fillTable[GroupSelectionTable](groupstab, groups.map(_._2))
    }
  }
}