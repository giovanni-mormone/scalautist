package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, SettimanaN}
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Button, Label, TableView}
import javafx.scene.layout.{AnchorPane, VBox}
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
    var add: Button = _
    @FXML
    var sub: Button = _
    @FXML
    var reset: Button = _
    @FXML
    var run: Button = _
    @FXML
    var dateg: AnchorPane = _
    @FXML
    var tableInfo: VBox = _
    @FXML
    var rules: Label = _
    @FXML
    var date: Label = _
    @FXML
    var selected: Label = _
    private var listFieldCheckBox:List[TwoFieldCheckBox] = List.empty
    private var groups: List[(Group, GroupToTable)] = List.empty[(Group, GroupToTable)]
    private var gruopss:List[Group]= List.empty
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
      sub.setOnAction(_=>removeSelectedGroup())
      /*sub.setOnAction(_ => {
        val toDelete = getSelectedRow
        groups = groups.filter(group => !findGroup(toDelete, group))
        CreateTable.fillTable[GroupSelectionTable](groupstab, groups.map(_._2))
      })*/
      run.setOnAction(_ => {
        var i = 0
        val gruppi = Option(gruopss.map(group => {
            i += 1
            GruppoA(i,group.date.map(x=>Date.valueOf(x)), group.rule)
          })).filter(_.nonEmpty)

        parent.showParams(AlgorithmExecute(
          Date.valueOf(params.dateI), Date.valueOf(params.dateF),
          params.terminals.map(_.idTerminale.head),
          gruppi,
          Option(params.requestN.toList.flatten.map(req => SettimanaN(req.giornoId, req.turnoId, req.quantita, req.regolaId))).filter(_.nonEmpty),
          params.requestS.filter(_.nonEmpty),
          params.roleS
        ), params.name)
      })
    }

    /*private def getSelectedRow: GroupSelectionTable = {
      groupstab.getSelectionModel.getSelectedItem
    }*/
    def removeSelectedGroup(): Unit ={
      listFieldCheckBox.foreach(son=>{
        if(son.selectedItem()){
          val result =  tableInfo.getChildren.remove(son.setParent(parent).pane)
          if(result){
            gruopss = gruopss.filter(x=> x.date.map(x=>Date.valueOf(x)).forall(date=>son.getDateList.contains(date)))

          }

        }
      })
    }
    private def allDate(): List[LocalDate] =
      gruopss.flatMap(_.date)

    private def findGroup(toFind: GroupSelectionTable, group: (Group, GroupToTable)): Boolean = {
      if(toFind == null)
        return false
      group._2.regola.equals(toFind.rule.get()) &&
        toFind.date.get().equals(group._2.date.map(_.toLocalDate)
          .map(date => date.getDayOfMonth.toString + "/" + date.getMonth.toString + "/" + date.getYear.toString).reduce((dl, d) => dl + "; " + d))
    }
    def initTable():Unit={
      date.setText(resources.getResource("date-column"))
      rules.setText(resources.getResource("rule-column"))
      selected.setText(resources.getResource("select-column"))
    }
    /*private def initTable(): Unit = {
      val fieldList = List("rule", "date")
      CreateTable.createColumns[GroupSelectionTable](groupstab, fieldList, dim= 50)
      groupstab.getSelectionModel.selectedItemProperty().addListener((_,_,_) => {
        initDate(groups.find(group => findGroup(getSelectedRow, group)).map(_._1))
      })
    }*/

    private def initDate(group: List[Group]=List.empty): Unit = {
      val datePicker: DatePickerSkin =
        CreateDatePicker.changingDatePickerSkin(group.flatMap(_.date))._1
      val node: Node = datePicker.getPopupContent
      dateg.getChildren.add(node)
    }

    override def updateGroup(group: Group): Unit = {
      val groupBox = TwoFieldCheckBox(group.ruleName,group.date.map(_.toString))
      tableInfo.getChildren.add(groupBox.setParent(parent).pane)
      gruopss = gruopss:+group
      listFieldCheckBox=listFieldCheckBox:+groupBox
      initDate(gruopss)
    }
    /*override def updateGroup(group: Group): Unit = {
      val newG = (group, GroupToTable(group.ruleName, group.date.map(day => Date.valueOf(day))))
      groups = newG :: groups
      CreateTable.fillTable[GroupSelectionTable](groupstab, groups.map(_._2))
    }*/
  }
}