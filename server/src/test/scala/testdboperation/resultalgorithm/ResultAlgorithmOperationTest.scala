package testdboperation.resultalgorithm

import java.sql.Date
import java.time.LocalDate

object ResultAlgorithmOperationTest {
  val idTerminalNotExist = 20
  val idTerminal = 1
  val dateI: Date = Date.valueOf(LocalDate.of(2020,6,18))
  val dateF: Date = Date.valueOf(LocalDate.of(2020,6,30))
  val dateIWithoutInfo: Date = Date.valueOf(LocalDate.of(2020,1,18))
  val dateFWithoutInfo: Date = Date.valueOf(LocalDate.of(2020,2,28))
}
