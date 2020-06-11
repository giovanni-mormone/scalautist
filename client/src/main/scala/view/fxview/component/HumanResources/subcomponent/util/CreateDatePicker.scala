package view.fxview.component.HumanResources.subcomponent.util

import java.sql.{Date => dateSql}
import java.time.{Instant, LocalDate, ZoneId}
import java.util.{Date => dateUtil}

import javafx.scene.control.{DateCell, DatePicker}

case object CreateDatePicker{

  final case class MoveDatePeriod(years: Int = 0, months: Int = 0, days: Int = 0)

  def createDataPicker(dataPicker: DatePicker, behind: MoveDatePeriod, after: MoveDatePeriod, today: LocalDate = LocalDate.now()): Unit = {
    dataPicker.setDisable(false)
    dataPicker.setEditable(false)
    dataPicker.setDayCellFactory(_=> setDate(today, behind, after))
  }

  private def setDate(today:LocalDate, behind: MoveDatePeriod, after: MoveDatePeriod): DateCell = new DateCell() {
    val minDate = today.minusYears(behind.years).minusMonths(behind.months).minusDays(behind.days)
    val maxDate = today.plusYears(after.years).plusMonths(after.months).plusDays(after.days)
    override def updateItem(date:LocalDate, empty:Boolean) {
      super.updateItem(date, empty)
      setDisable(date.isBefore(minDate) || date.isAfter(maxDate) || empty)
    }
  }

  def createDataSql(dateP:DatePicker): dateSql ={
    val localDate: LocalDate = dateP.getValue
    val instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()))
    val date = dateUtil.from(instant)
    new dateSql(date.getTime)
  }
}
