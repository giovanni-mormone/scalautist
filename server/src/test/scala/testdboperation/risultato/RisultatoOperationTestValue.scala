package testdboperation.risultato

import java.sql.Date

import utils.DateConverter

object RisultatoOperationTestValue {
  val idAutist: Int = 2
  val idManager: Int = 1
  val idNobody: Int = 9999
  val date: Date = Date.valueOf("2020-06-01")
  val week: Int = DateConverter.getWeekNumber(date)
}
