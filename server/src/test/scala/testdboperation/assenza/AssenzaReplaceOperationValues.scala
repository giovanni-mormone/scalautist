package testdboperation.assenza

import java.sql.Date
import java.time.LocalDate

object AssenzaReplaceOperationValues {
  val date:Date = Date.valueOf(LocalDate.of(2020,6,18))
  val dateWithoutAbsence:Date = Date.valueOf(LocalDate.of(2022,6,18))
  val idRisultatoWithoutReplace=18
  val idTerminalWithoutReplace=1
  val idTurnoWithoutReplace=2
  val idRisultatoWithReplace=391
  val idTerminalWithReplace=3
  val idTurnoWithReplace=5
  val idRisultatoForUpdate = 391
  val idNewPerson = 12
  val idRisultatoForUpdateNotExist = 8888
  val idNewPersonNotExist = 88
  val idRisultato=18
  val idTerminal=3
  val idTurno=2
  val idTerminalNotExist= 100
  val idTurnoNotExist = 200
}
