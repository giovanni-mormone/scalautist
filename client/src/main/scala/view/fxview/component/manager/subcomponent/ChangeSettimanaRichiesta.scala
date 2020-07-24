package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle
import java.util.stream.Collectors

import caseclass.CaseClassDB.{GiornoInSettimana, Regola}
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox, Label, TableView}
import org.controlsfx.control.CheckComboBox
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.manager.subcomponent.parent.ChangeSettimanaRichiestaParent
import view.fxview.component.manager.subcomponent.util.{ParamsForAlgoritm, ShiftTable}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

import scala.jdk.CollectionConverters

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
    var titleN: Label = _
    @FXML
    var titleS: Label = _
    @FXML
    var error: Label = _
    @FXML
    var dayShiftN: TableView[ShiftTable] = _
    @FXML
    var dayShiftS: TableView[ShiftTable] = _

    case class DailyReq(day: Int, shift: Int, quantity: Int = 0, rule: String)

    var daysInfo: List[DailyReq] = List.empty
    val NONE: String = "NONE"

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      daysInfo = params.requestN.getOrElse(List.empty)
        .map(req => DailyReq(req.giornoId, req.turnoId, req.quantita, rules.find(_.idRegola.contains(req.regolaId))
          .fold(Regola(NONE, Some(0)))(x => x).nomeRegola))
      initButton()
      initCombo()
      initLabel()
      initTable()
    }

    private def initButton(): Unit = {
      run.setText(resources.getResource("runtxt"))
      run.setOnAction(_ => {
        if(control())
          parent.groupParam(params)//RemakeParams())
        else
          error.setVisible(true)
        println(remakeParams())
      })
      run.setDisable(false)

      reset.setText(resources.getResource("resettxt"))
      reset.setOnAction(_ => parent.resetWeekParams())
    }

    private def initCombo(): Unit = {
      val list = calcolateWeeks()
      list.foreach(week => weekS.getItems.add(week.toString))
    }

    private def initLabel(): Unit = {
      titleN.setText(resources.getResource("weekN"))
      titleS.setText(resources.getResource("weekS"))
      error.setText(resources.getResource("errortxt"))
      error.setVisible(false)
    }

    private def initTable(): Unit = {
      CreateTable.fillTable[ShiftTable](dayShiftN, getElements())
      CreateTable.createColumns[ShiftTable](dayShiftN, List("shift"))
      CreateTable.createEditableColumns[ShiftTable](dayShiftN, ShiftTable.editableShiftTable)
      CreateTable.createColumns[ShiftTable](dayShiftN, List("combo"))

      CreateTable.fillTable[ShiftTable](dayShiftS, getElements(List.empty))
      CreateTable.createColumns[ShiftTable](dayShiftS, List("shift"))
      CreateTable.createEditableColumns[ShiftTable](dayShiftS, ShiftTable.editableShiftTable)
      CreateTable.createColumns[ShiftTable](dayShiftS, List("combo"))
    }

    private def control(): Boolean =
      getElements(dayShiftN).size == getSelectedElements(dayShiftN).size

    private def getElements(infoDays: List[DailyReq] = daysInfo): List[ShiftTable] = {
      val SHIFT_STRING_MAP: Map[Int, String] = Map(1 -> "2-6", 2 -> "6-10", 3 -> "10-14", 4 -> "14-18", 5 -> "18-22", 6 -> "22-2")
      (1 to 6).map( shift => {
        val info: List[String] = getInfoShiftForDays(shift, infoDays)
        new ShiftTable(SHIFT_STRING_MAP.getOrElse(shift, NONE), info.head, info(1), info(2), info(3), info(4), info(5),
          rules.map(_.nomeRegola), infoDays.find(day => day.shift == shift).map(_.rule))
      }).toList
    }

    private def getInfoShiftForDays(shift: Int, infoDays: List[DailyReq]): List[String] =
      (1 to 6).map(day => infoDays.find(info => info.day == day && info.shift == shift)
        .fold("0")(_.quantity.toString)).toList

    private def getElements(tableView: TableView[ShiftTable]): Set[ShiftTable] = {
      new CollectionConverters.ListHasAsScala[ShiftTable](
        tableView.getItems.stream().map[ShiftTable](x => x)
          .collect(Collectors.toList[ShiftTable])).asScala.toSet
    }

    private def getSelectedElements(tableView: TableView[ShiftTable]): Set[ShiftTable] = {
      new CollectionConverters.ListHasAsScala[ShiftTable](
        tableView.getItems.filtered(_.getSelected.isDefined)
          .stream().map[ShiftTable](x => x).collect(Collectors.toList[ShiftTable])).asScala.toSet
    }

    private def remakeParams(): ParamsForAlgoritm = {
      val SHIFT_INT_MAP: Map[String, Int] = Map("2-6" -> 1 , "6-10" -> 2, "10-14" -> 3 , "14-18" -> 4, "18-22" -> 5, "22-2" -> 6)
      val ERROR_ID = -1
      val singleDays: List[DailyReq] = getElements(dayShiftN).toList
        .flatMap(day => {
          val shift = SHIFT_INT_MAP.getOrElse(day.getShift, ERROR_ID)
          val rule = day.getSelected.getOrElse(NONE)
          List(
            DailyReq(1, shift, day.getMonday.toInt, rule),
            DailyReq(2, shift, day.getTuesday.toInt, rule),
            DailyReq(3, shift, day.getWednesday.toInt, rule),
            DailyReq(4, shift, day.getThursday.toInt, rule),
            DailyReq(5, shift, day.getFriday.toInt, rule),
            DailyReq(6, shift, day.getSaturday.toInt, rule)
          )
        }).filter(_.quantity != 0)
      val daysReq = calcolateWeeks().filter(week => !getSelectedWeeks.contains(week))
        .flatMap(week => singleDays.map(req => GiornoInSettimana(req.day, req.shift,
        rules.find(_.nomeRegola.equals(req.rule)).fold(ERROR_ID)(_.idRegola.head), req.quantity, idSettimana = Some(week))))
      params.copy(requestN = Some(daysReq))
    }

    private def getSelectedWeeks: List[Int] = {
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
