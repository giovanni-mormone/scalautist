package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.{LocalDate, ZoneId}
import java.util.ResourceBundle

import caseclass.CaseClassDB.{GiornoInSettimana, Regola}
import caseclass.CaseClassHttpMessage._
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{Pane, VBox}
import org.controlsfx.control.CheckComboBox
import view.fxview.component.manager.subcomponent.parent.ChangeSettimanaRichiestaParent
import view.fxview.component.manager.subcomponent.util.{ParamsForAlgoritm, ShiftUtil, ShiftTable}
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
    @FXML
    var alert: Label = _

    case class DailyReq(day: Int, shift: Int, quantity: Int = 0, rule: String)

    private var daysInfo: List[DailyReq] = List.empty
    private val NONE: String = "NONE"
    private var specialWeeks: List[TableParamSettimana] = List.empty
    private var normalWeek: TableParamSettimana = _
    private val ERROR_ID: Int = -1
    private var LAST_WEEK_DAY: Int = 6
    private var FIRST_WEEK_DAY_SHIFT: Int = 1

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      specialWeeks = List.empty
      FIRST_WEEK_DAY_SHIFT = 1
      LAST_WEEK_DAY = 6
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
      alert.setText(resources.getResource("alert"))
      error.setText(resources.getResource("errortxt"))
      error.setVisible(false)
    }

    private def initTable(): Unit = {
      normalWeek = TableParamSettimana(getElements())
      dayShiftN.getChildren.add(normalWeek.pane)
    }

    private def control(): Boolean = {
      val normalRows = normalWeek.getElements().toList.filter(yesDataInRow)
      val specialRows = specialWeeks.flatMap(week => week.getElements().toList.filter(yesDataInRow))
      normalRows.size == normalRows.count(_.getSelected.isDefined) &&
      specialRows.size == specialRows.count(_.getSelected.isDefined)
    }

    private def yesDataInRow(week: ShiftTable): Boolean =
      !(FIRST_WEEK_DAY_SHIFT to ShiftUtil.N_SHIFT).map(week.get).forall(_.toInt == 0)

    private def getElements(infoDays: List[DailyReq] = daysInfo): List[ShiftTable] = {
      val n = (FIRST_WEEK_DAY_SHIFT to ShiftUtil.N_SHIFT).map(shift => {
        val info: List[String] = getInfoShiftForDays(shift, infoDays)
        new ShiftTable(ShiftUtil.getShiftName(shift), info.head, info(1), info(2), info(3), info(4), info(5),
          rules.map(_.nomeRegola), infoDays.find(day => day.shift == shift).map(_.rule))
      }).toList
      n
    }

    private def getInfoShiftForDays(shift: Int, infoDays: List[DailyReq]): List[String] =
      (FIRST_WEEK_DAY_SHIFT to LAST_WEEK_DAY).map(day => infoDays.find(info => info.day == day && info.shift == shift)
        .fold("0")(_.quantity.toString)).toList

    private def remakeParams(): ParamsForAlgoritm = {

      val daysReqN = getReqFromTable(normalWeek)
      val daysReqS = getSpecialReqFromTable

      params.copy(requestN = Some(daysReqN), requestS = Some(daysReqS))
    }


    private def getSpecialReqFromTable: List[SettimanaS] = {
      val weeksNum: List[Int] = getSelectedWeeks
      val weekInfo = for {
        i <- specialWeeks.indices
        weekTab = specialWeeks(i)
        nWeek = weeksNum(i)
      } yield (weekTab, nWeek)

      weekInfo.toList.flatMap(info => {
        val daysDate = getDates(info._2, params.dateI)
        info._1.getElements().toList.flatMap(shiftInfo => {
          val shiftId = ShiftUtil.getShiftId(shiftInfo.getShift)
          val rule: Int = rules.find(_.nomeRegola.equals(shiftInfo.getSelected.getOrElse(0))).fold(ERROR_ID)(_.idRegola.get)
          (FIRST_WEEK_DAY_SHIFT to LAST_WEEK_DAY).map(index =>
                SettimanaS(index, shiftId, shiftInfo.get(index).toInt, rule, Date.valueOf(daysDate(index - 1))))
        })
      }).filter(_.quantita != 0)

    }

    private def getDates(week: Int, init: LocalDate): List[LocalDate] = {
      val first: LocalDate = getMonday(week, if(weekNum(init) > week) init.getYear + 1 else init.getYear)
      var days: List[LocalDate] = List(first)

      for(i <- FIRST_WEEK_DAY_SHIFT until LAST_WEEK_DAY)
        days = days :+ first.plusDays(i)
      days
    }

    private def getReqFromTable(table: TableParamSettimana): List[GiornoInSettimana] = {
      val singleDays: List[DailyReq] = getDailyReqFromTable(table).filter(_.quantity > 0)
      composeRequest(singleDays)
    }

    private def composeRequest(singleDays: List[DailyReq]): List[GiornoInSettimana] = {
      singleDays.map(req => GiornoInSettimana(req.day, req.shift,
          rules.find(_.nomeRegola.equals(req.rule)).fold(ERROR_ID)(_.idRegola.head), req.quantity))
    }

    private def getDailyReqFromTable(table: TableParamSettimana): List[DailyReq] = {
      table.getElements().toList
        .flatMap(day => {
          val shift = ShiftUtil.getShiftId(day.getShift)
          val rule = day.getSelected.getOrElse(NONE)
          (FIRST_WEEK_DAY_SHIFT to LAST_WEEK_DAY).map(index => DailyReq(index, shift, day.get(index).toInt, rule))
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

    private def getMonday(week: Int, year: Int = LocalDate.now().getYear): LocalDate = {
      import java.util.Calendar
      val cal = Calendar.getInstance
      cal.set(Calendar.YEAR, year)
      cal.set(Calendar.WEEK_OF_YEAR, week)
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
      cal.getTime.toInstant.atZone(ZoneId.systemDefault()).toLocalDate
    }
  }
}
