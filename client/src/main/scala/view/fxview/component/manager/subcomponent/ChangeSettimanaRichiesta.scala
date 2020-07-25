package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.{GiornoInSettimana, Regola}
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{Pane, VBox}
import org.controlsfx.control.CheckComboBox
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
    var titleN: Label = _
    @FXML
    var titleS: Label = _
    @FXML
    var error: Label = _
    @FXML
    var dayShiftN: VBox = _
    @FXML
    var dayShiftS: VBox = _

    case class DailyReq(day: Int, shift: Int, quantity: Int = 0, rule: String)

    var daysInfo: List[DailyReq] = List.empty
    val NONE: String = "NONE"
    var specialWeeks: List[TableParamSettimana] = List.empty
    var normalWeek: TableParamSettimana = _
    val ERROR_ID: Int = -1

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      specialWeeks = List.empty
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
          parent.groupParam(remakeParams())
        else
          error.setVisible(true)
      })
      run.setDisable(false)

      reset.setText(resources.getResource("resettxt"))
      reset.setOnAction(_ => parent.resetWeekParams())
    }

    private def initCombo(): Unit = {
      val list = calcolateWeeks()
      list.foreach(week => weekS.getItems.add(week.toString))
      weekS.getCheckModel.getCheckedItems.addListener(
        new ListChangeListener[String]() {
          override def onChanged(c: ListChangeListener.Change[_ <: String]): Unit = {
            if(weekS.getCheckModel.getCheckedItems.size() < specialWeeks.size)
              dropTable()
            else
              drawTable()
          }
        })
    }

    private def dropTable(): Unit = {
      specialWeeks = specialWeeks.tail.map(_ => TableParamSettimana(getElements(List.empty[DailyReq])))
      dayShiftS.getChildren.clear()
      specialWeeks.foreach(table => dayShiftS.getChildren.add(table.pane))
    }

    private def drawTable(): Unit = {
      specialWeeks = TableParamSettimana(getElements(List.empty[DailyReq])) ::
        specialWeeks.map(_ => TableParamSettimana(getElements(List.empty[DailyReq])))
      dayShiftS.getChildren.clear()
      specialWeeks.foreach(table => dayShiftS.getChildren.add(table.pane))
    }

    private def initLabel(): Unit = {
      titleN.setText(resources.getResource("weekN"))
      titleS.setText(resources.getResource("weekS"))
      error.setText(resources.getResource("errortxt"))
      error.setVisible(false)
    }

    private def initTable(): Unit = {
      normalWeek = TableParamSettimana(getElements())
      dayShiftN.getChildren.add(normalWeek.pane)
    }

    private def control(): Boolean = {
      val normalRows = normalWeek.getElements()
      val specialRows = specialWeeks.flatMap(week => week.getElements())
      normalRows.size == normalRows.count(_.getSelected.isDefined) &&
      specialRows.size == specialRows.count(_.getSelected.isDefined)
    }


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

    private def remakeParams(): ParamsForAlgoritm = {

      val daysReqN = getReqFromTable(normalWeek)
      val daysReqS = specialWeeks.flatMap(weekTab => getReqFromTable(weekTab, special = true))

      params.copy(requestN = Some(daysReqN), requestS = Some(daysReqS))
    }

    private def getReqFromTable(table: TableParamSettimana, special: Boolean = false): List[GiornoInSettimana] = {
      val singleDays: List[DailyReq] = getDailyReqFromTable(table)
      composeRequest(singleDays, special)
    }

    private def composeRequest(singleDays: List[DailyReq], special: Boolean): List[GiornoInSettimana] = {
      val condition = (week: Int) => {
        val result = getSelectedWeeks.contains(week)
        if(!special) !result else result
      }
      calcolateWeeks().filter(week => condition(week))
        .flatMap(week => singleDays.map(req => GiornoInSettimana(req.day, req.shift,
          rules.find(_.nomeRegola.equals(req.rule)).fold(ERROR_ID)(_.idRegola.head), req.quantity, idSettimana = Some(week))))
    }

    private def getDailyReqFromTable(table: TableParamSettimana): List[DailyReq] = {
      val SHIFT_INT_MAP: Map[String, Int] = Map("2-6" -> 1 , "6-10" -> 2, "10-14" -> 3 , "14-18" -> 4, "18-22" -> 5, "22-2" -> 6)
      table.getElements().toList
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
        })
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
