package view.fxview.component.HumanResources.subcomponent.util

import java.sql.{Date => dateSql}
import java.time.{Instant, LocalDate, MonthDay, ZoneId}
import java.util.{Calendar, Date => dateUtil}

import caseclass.CaseClassHttpMessage.{InfoPresenza, StipendioInformations}
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.scene.control.{DateCell, DatePicker, Tooltip}

case object CreateDatePicker{

  final case class MoveDatePeriod(years: Int = 0, months: Int = 0, days: Int = 0)

  def createDataPicker(dataPicker: DatePicker, behind: MoveDatePeriod, after: MoveDatePeriod, today: LocalDate = LocalDate.now()): Unit = {
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
  def drawDatePicker(myCalendar: List[Option[(InfoPresenza,LocalDate)]]):DateCell=new DateCell{
    override def updateItem(item: LocalDate, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      val ifExist = myCalendar.map(list=>list.filter(result=>result._2.equals(item))).filter(element=>element.isDefined)
       if(ifExist.nonEmpty && ifExist.head.head._1.straordinario) {
          setTooltip(new Tooltip("Giorno Straordinario \n Valore: "+ifExist.head.head._1.valoreTurno+" \n " +
            "Nome Turno : "+ifExist.head.head._1.nomeTurno+ "\n Durata Turno :"+ifExist.head.head._1.durataTurno))
          setStyle("-fx-background-color: green;");
        } else if(ifExist.nonEmpty && !ifExist.head.head._1.straordinario) {
         setTooltip(new Tooltip("Giorno Normale \n Valore: "+ifExist.head.head._1.valoreTurno+" \n " +
           "Nome Turno : "+ifExist.head.head._1.nomeTurno+ "\n Durata Turno :"+ifExist.head.head._1.durataTurno))
         setStyle("-fx-background-color: greenyellow;");
        }

    }
  }
  def sqlDateToCalendar(date:dateSql):LocalDate=date.toLocalDate
  def createDatePickerSkin(datePicker: DatePicker): DatePickerSkin =new DatePickerSkin(datePicker)
}
