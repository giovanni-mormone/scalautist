package utils

import java.sql.Date
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

import utils.DateConverter.{createListDay, createListDayBetween, getEndDayWeek}

import scala.collection.immutable.{AbstractMap, SeqMap, SortedMap}

/**
 * Helper object to work with [[java.sql.Date]]
 */
object DateConverter {
  /**
   * Returns the first date of the month provided
   */
  val startMonthDate: Date => Date = date  =>
    converter(date,c => {
      c.set(Calendar.DAY_OF_MONTH,1)
      c
    })

  /**
   * Returns the first date of the month after the provided
   */
  val nextMonthDate: Date =>  Date = date =>
    converter(date,c =>{
      c.add(Calendar.MONTH,1)
      c.set(Calendar.DAY_OF_MONTH,1)
      c
    })


  /**
   * Returns the last date of the month provided
   */
  val endOfMonth: Date => Date = date =>
    converter(date,c => {
      c.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))
      c
    })

  /**
   * Return the first date of the year getting the year as input
   */
  val dateFromYear: Int => Date = year => {
    val calendar = Calendar.getInstance()
    calendar.set(year,0,1)
    new Date(calendar.getTimeInMillis)
  }

  /**
   * Returns the number of days between two dates.
   * It Counts the starting day as a whole day, i.e. between the same day there is 1 day.
   */
  val computeDaysBetweenDates: (Date,Date) => Int = (dateStart,dateStop) =>
    ChronoUnit.DAYS.between(dateStart.toLocalDate,dateStop.toLocalDate).toInt + 1

  /**
   * Returns whether or not two date have different year
   */
  val notSameYear: (Date,Date) => Boolean = (start,end) =>
    start.toLocalDate.getYear != end.toLocalDate.getYear

  val nextWeek: Date => Date = today => converter(today, now => {
    now.add(Calendar.WEEK_OF_YEAR, 1)
    now
  })

  val getWeekNumber: Date => Int = date => {
    dateToCalendar(date).get(Calendar.WEEK_OF_YEAR)
  }

  val getDayNumber: Date => Int = date => {
    dateToCalendar(date).get(Calendar.DAY_OF_WEEK)-1
  }
  val getFirstDayWeek:Date=>Date=date=>{
    val calendar = dateToCalendar(date)
    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
    new Date(calendar.getTimeInMillis)
  }
  val getEndDayWeek:Date=>Date=date=>{
    val calendar = dateToCalendar(getFirstDayWeek(date))
    calendar.add(Calendar.DATE,6)
    new Date(calendar.getTimeInMillis)
  }
  private val converter: (Date, Calendar => Calendar) => Date = (date, function) =>{
    var calendar = dateToCalendar(date)
    calendar = function(calendar)
    new Date(calendar.getTimeInMillis)
  }

  val nameOfDay:Date=>String = date=>{
    val days = Array[String]("Domenica", "Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato")
    days(dateToCalendar(date).get(Calendar.DAY_OF_WEEK)-1)
  }

  val createListDay:Date=>List[Date]=dateList=>{
    val calendar = dateToCalendar(dateList)
    (0 to 6).toList.map(day=>{
      calendar.setTime(getFirstDayWeek(dateList))
      getDay(calendar,day)
    })
  }
  val createListDayBetween:(Date,Date)=>List[Date]=(firstDate,endDate)=>{
      createListBetween(firstDate,endDate,List.empty)
  }
  private val createListBetween:(Date,Date,List[Date])=>List[Date]={
    case (firstDate,endDate,list) if firstDate.compareTo(endDate)<0=>
      val calendar = dateToCalendar(firstDate)
      createListBetween(getDay(calendar,1),endDate,list:+getDay(calendar,-1))
    case (firstDate,endDate,list) if firstDate.compareTo(endDate)==0=>list:+endDate
  }
  private val getDay:(Calendar,Int)=>Date=(calendar,day)=>{
    calendar.add(Calendar.DATE,day)
    new Date(calendar.getTimeInMillis)
  }
  val subtract:(Date,Int)=>Date=(date,minus)=>{
    val calendar = dateToCalendar(date)
    calendar.add(Calendar.DATE,minus)
    new Date(calendar.getTimeInMillis)
  }
  private val dateToCalendar:Date=>Calendar=date=>{
    val calendar = Calendar.getInstance()
    calendar.setTime(date)
    calendar
  }
}
