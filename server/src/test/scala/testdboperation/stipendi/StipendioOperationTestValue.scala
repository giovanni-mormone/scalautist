package testdboperation.stipendi

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoAssenza, InfoPresenza, InfoValorePresenza, StipendioInformations}

object StipendioOperationTestValue {
  val stipendiId2: List[Stipendio] = List(Stipendio(2,1700.0,Date.valueOf(LocalDate.of(2020,5,1)),Some(1)), Stipendio(2,1700.0,Date.valueOf(LocalDate.of(2020,4,1)),Some(6)), Stipendio(2,3000.0,Date.valueOf(LocalDate.of(2020,3,1)),Some(11)),Stipendio(2,3000.0,Date.valueOf(LocalDate.of(2020,2,1)),Some(16)))
  val goodDateToCompute: Date = Date.valueOf(LocalDate.of(2020,7,11))
  val badDateToCompute: Date = Date.valueOf(LocalDate.of(2020,11,11))
  val stipendiInfoStip3: StipendioInformations = StipendioInformations(List(InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,4,22)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,23)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,24)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,25)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,26)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,27)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,28)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,29)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,30)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,1)),straordinario = false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",Date.valueOf(LocalDate.of(2020,5,1)),straordinario = false)),
                                                     InfoValorePresenza(10,0.0,457.6000000000001),
                                                     InfoAssenza(15,9))

}
