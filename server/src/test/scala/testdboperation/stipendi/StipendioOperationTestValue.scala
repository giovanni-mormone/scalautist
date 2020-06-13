package testdboperation.stipendi

import java.sql.Date

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoAssenza, InfoPresenza, InfoValorePresenza, StipendioInformations}

object StipendioOperationTestValue {
  val stipendiId2: List[Stipendio] = List(Stipendio(2,1700.0,new Date(120,4,1),Some(1)), Stipendio(2,1700.0,new Date(120,3,1),Some(6)), Stipendio(2,3000.0,new Date(120,2,1),Some(11)),Stipendio(2,3000.0,new Date(120,1,1),Some(16)))
  val goodDateToCompute: Date = new Date(120,5,11)
  val badDateToCompute: Date = new Date(120,9,11)
  val stipendiInfoStip3 = StipendioInformations(List(InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,22),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,23),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,24),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,25),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,26),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,27),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,28),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,29),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,30),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,01),false),
                                                     InfoPresenza(41.6,"Tarda Serata","22:00 alle 2:00",new Date(120,4,01),false)),
                                                     InfoValorePresenza(10,0.0,457.6000000000001),
                                                     InfoAssenza(15,9))

}
