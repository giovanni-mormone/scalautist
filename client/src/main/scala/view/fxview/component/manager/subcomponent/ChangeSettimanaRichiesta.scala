package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, Label, TableView}
import org.controlsfx.control.CheckComboBox
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.manager.subcomponent.parent.ChangeSettimanaRichiestaParent
import view.fxview.component.manager.subcomponent.util.{ParamsForAlgoritm, ShiftTable}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait ChangeSettimanaRichiesta extends Component[ChangeSettimanaRichiestaParent] {

}

object ChangeSettimanaRichiesta{

  def apply(params: ParamsForAlgoritm, rules: List[Regola]): ChangeSettimanaRichiesta =
    new ChangeSettimanaRichiestaFX(params, rules)

  private class ChangeSettimanaRichiestaFX(params: ParamsForAlgoritm, rules: List[Regola])
  extends AbstractComponent[ChangeSettimanaRichiestaParent](path = "manager/subcomponent/ParamSettimana")
  with ChangeSettimanaRichiesta{

    @FXML
    var run: Button = _
    @FXML
    var reset: Button = _
    @FXML
    var weekS: CheckComboBox[String] = _
    @FXML
    var ruleN: ComboBox[String] = _
    @FXML
    var ruleS: ComboBox[String] = _
    @FXML
    var titleN: Label = _
    @FXML
    var titleS: Label = _
    @FXML
    var dayShiftN: TableView[ShiftTable] = _
    @FXML
    var dayShiftS: TableView[ShiftTable] = _

    case class DailyReq(day: Int, shift: Int, quantity: Int = 0)

    var daysInfo: List[DailyReq] = List.empty

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      daysInfo = params.request.getOrElse(List.empty).map(req => DailyReq(req.giornoId, req.turnoId, 2))
      initButton()
      initCombo()
      initLabel()
      initTable()
    }

    private def initButton(): Unit = {
      run.setText(resources.getResource("runtxt"))
      run.setOnAction(_ => parent.groupParam(params))
      run.setDisable(true)

      reset.setText(resources.getResource("resettxt"))
      reset.setOnAction(_ => parent.resetWeekParams())
    }

    private def initCombo(): Unit = {
      val list = calcolateWeeks()
      list.foreach(week => weekS.getItems.add(week.toString))
      weekS.getCheckModel.getCheckedItems.addListener(
        new ListChangeListener[String]() {
          override def onChanged(c: ListChangeListener.Change[_ <: String]): Unit =
            enableButton()
        })

      rules.foreach(rule => {
        ruleN.getItems.add(rule.nomeRegola)
        ruleS.getItems.add(rule.nomeRegola)
      })
      ruleN.setPromptText(resources.getResource("ruletxt"))
      ruleS.setPromptText(resources.getResource("ruletxt"))
      ruleS.setOnAction(_ => enableButton())
      ruleN.setOnAction(_ => enableButton())

      if(params.ruleNormal.isDefined) {
        rules.find(rule => rule.idRegola.contains(params.ruleNormal.head))
          .fold()(rule => ruleN.getSelectionModel.select(rule.nomeRegola))
      }
    }

    private def initLabel(): Unit = {
      titleN.setText(resources.getResource("weekN"))
      titleS.setText(resources.getResource("weekS"))
    }

    private def initTable(): Unit = {
      CreateTable.fillTable[ShiftTable](dayShiftN, getElements())
      CreateTable.createColumns[ShiftTable](dayShiftN, List("shift"))
      CreateTable.createEditableColumns[ShiftTable](dayShiftN, ShiftTable.editableShiftTable)

      CreateTable.fillTable[ShiftTable](dayShiftS, getElements(List.empty))
      CreateTable.createColumns[ShiftTable](dayShiftS, List("shift"))
      CreateTable.createEditableColumns[ShiftTable](dayShiftS, ShiftTable.editableShiftTable)
    }

    private def enableButton(): Unit =
      run.setDisable(!(!ruleN.getSelectionModel.isEmpty && controlSpecialWeek()))

    private def controlSpecialWeek(): Boolean = {
      if(getweeks.nonEmpty)
        !ruleS.getSelectionModel.isEmpty
      else true
    }

    private def getElements(infoDays: List[DailyReq] = daysInfo): List[ShiftTable] = {
      val shiftStringMap: Map[Int, String] = Map(1 -> "2-6", 2 -> "6-10", 3 -> "10-14", 4 -> "14-18", 5 -> "18-22", 6 -> "22-2")
      (1 to 6).map( shift => {
        val info: List[String] = getInfoShiftForDays(shift, infoDays)
        new ShiftTable(shiftStringMap.getOrElse(shift, "ERROR"), info.head, info(1), info(2), info(3), info(4), info(5))
      }).toList
    }

    private def getInfoShiftForDays(shift: Int, infoDays: List[DailyReq]): List[String] =
      (1 to 6).map(day => infoDays.find(info => info.day == day && info.shift == shift)
        .fold("0")(_.quantity.toString)).toList

    private def getweeks: List[Int] = {
      var weeks: List[Int] = List.empty
      weekS.getCheckModel.getCheckedItems.forEach(selected => weeks = weeks :+ selected.toInt)
      weeks
    }

    private def calcolateWeeks(): List[Int] = {
      val DECEMBER: Int = 12
      val LAST_DAY: Int = 28
      val FIRST_WEEK: Int = 1
      var res: List[Int] = List.empty

      val initWeek = weekNum(params.dateI)
      val endWeek = weekNum(params.dateF)

      if (initWeek <= endWeek)
        for(i <- initWeek to endWeek)
          res = res :+ i
      else {
        val lastWeek = weekNum(LocalDate.of(params.dateI.getYear, DECEMBER, LAST_DAY))
        for (i <- initWeek to lastWeek)
          res = res :+ i
        for (i <- FIRST_WEEK to endWeek)
          res = res :+ i
      }
      res
    }

    private def weekNum(localDate: LocalDate): Int = {
      import java.util.Calendar
      val cal = Calendar.getInstance
      cal.setTime(Date.valueOf(localDate))
      cal.get(Calendar.WEEK_OF_YEAR)
    }
  }
}
