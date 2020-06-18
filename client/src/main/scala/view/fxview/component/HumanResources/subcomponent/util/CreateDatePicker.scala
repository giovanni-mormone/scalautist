package view.fxview.component.HumanResources.subcomponent.util

import java.sql.{Date => dateSql}
import java.time.{Instant, LocalDate, ZoneId}
import java.util.{Calendar, Date => dateUtil}

import caseclass.CaseClassHttpMessage.InfoPresenza
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.scene.control.{DateCell, DatePicker, Tooltip}

case object CreateDatePicker{

  final case class MoveDatePeriod(years: Int = 0, months: Int = 0, days: Int = 0)
  final case class DatePickerC(finishDate:DatePicker,behind: MoveDatePeriod, after: MoveDatePeriod,today:LocalDate=LocalDate.now())
  def createDataPicker(dataPicker: DatePickerC,assenza: Option[List[Option[(LocalDate,LocalDate)]]]=None): Unit = {
    dataPicker.finishDate.setEditable(false)
    dataPicker.finishDate.setDayCellFactory(_=> setDate(dataPicker,assenza))
  }

  private def setDate(dataPicker: DatePickerC,assenza: Option[List[Option[(LocalDate,LocalDate)]]]=None): DateCell = new DateCell() {
    val minDate: LocalDate = dataPicker.today.minusYears(dataPicker.behind.years).minusMonths(dataPicker.behind.months).minusDays(dataPicker.behind.days)
    val maxDate: LocalDate = dataPicker.today.plusYears(dataPicker.after.years).plusMonths(dataPicker.after.months).plusDays(dataPicker.after.days)
    override def updateItem(date:LocalDate, empty:Boolean):Unit={
      super.updateItem(date, empty)
      val finalDayInYear = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR), 12, 31)
      val ifExist: Option[List[(LocalDate, LocalDate)]] = assenza.map(_.flatten.filter(myDate=>date.compareTo(myDate._1)>=0 && date.compareTo(myDate._2)<=0)).filter(result=>result.nonEmpty)orElse None

      setDisable(date.isBefore(minDate) || date.isAfter(maxDate) || empty || ifExist.isDefined || date.isAfter(finalDayInYear))
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
      val ifExist: List[Option[(InfoPresenza, LocalDate)]] = myCalendar.map(_.filter(result=>result._2.equals(item))).filter(result=>result.isDefined)
       if(ifExist.nonEmpty && ifExist.head.head._1.straordinario) {
          setTooltip(new Tooltip("Giorno Straordinario \n Valore: "+ifExist.head.head._1.valoreTurno+" \n " +
            "Nome Turno : "+ifExist.head.head._1.nomeTurno+ "\n Durata Turno :"+ifExist.head.head._1.durataTurno))
          setStyle("-fx-background-color: green;")
        } else if(ifExist.nonEmpty && !ifExist.head.head._1.straordinario) {
         setTooltip(new Tooltip("Giorno Normale \n Valore: "+ifExist.head.head._1.valoreTurno+" \n " +
           "Nome Turno : "+ifExist.head.head._1.nomeTurno+ "\n Durata Turno :"+ifExist.head.head._1.durataTurno))
         setStyle("-fx-background-color: greenyellow;")
        }

    }
  }
  def sqlDateToCalendar(date:dateSql):LocalDate=date.toLocalDate

  def createDatePickerSkin(localDate:LocalDate): (DatePickerSkin,DatePicker) ={
    val datepicker= new DatePicker(localDate)
    (new DatePickerSkin(datepicker),datepicker)
  }

}
