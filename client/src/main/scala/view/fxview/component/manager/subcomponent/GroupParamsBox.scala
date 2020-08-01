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
import view.fxview.component.manager.subcomponent.util.{GroupSelectionTable, GroupToTable, ParamsForAlgorithm}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

/**
 * This trait allows to manage the view
 */
trait GroupParamsBox extends Component[GroupParamsParent] {

  /**
   * add the new group
   * @param group instance of [[Group]] to add
   */
  def updateGroup(group: Group): Unit
}

/**
 * Companion object of [[GroupParamsBox]]
 */
object GroupParamsBox {

  case class Group(date: List[LocalDate], rule: Int, ruleName: String)

  def apply(params: ParamsForAlgorithm, rule: List[Regola]): GroupParamsBox =
    new GroupParamsBoxFX(params, rule)

  /**
   * Java FX implementation of [[GroupParamsBox]]
   * @param params info chosen until this panel
   * @param rule list of [[Regola]] to show
   */
  private class GroupParamsBoxFX(params: ParamsForAlgorithm, rule: List[Regola])
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

    def initTable():Unit={
      date.setText(resources.getResource("date-column"))
      rules.setText(resources.getResource("rule-column"))
      selected.setText(resources.getResource("select-column"))
    }

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
  }
}