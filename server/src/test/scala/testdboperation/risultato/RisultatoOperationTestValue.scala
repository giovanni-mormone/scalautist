package testdboperation.risultato

import java.sql.Date

import utils.DateConverter

object RisultatoOperationTestValue {
  val idPersona: Int = 1
  val date: Date = Date.valueOf("2020-06-01")
  val week: Int = DateConverter.getWeekNumber(date)
}
