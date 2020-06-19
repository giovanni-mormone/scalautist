package testdboperation.assenza

import java.sql.Date
import java.time.LocalDate

object AssenzaReplaceOperationValues {
  val date:Date = Date.valueOf(LocalDate.of(2020,6,18))
  val idRisultatoWithoutReplace=818
  val idTerminalWithoutReplace=2
  val idTurnoWithoutReplace=4
  val idRisultatoWithReplace=140
  val idTerminalWithReplace=3
  val idTurnoWithReplace=5
  val idRisultatoForUpdate = 140
  val idNewPerson = 12
}
