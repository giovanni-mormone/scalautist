package testdboperation.persona

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie

object AssenzaOperationTestValues {
  val remainingFerieList= List(Ferie(2,"FrancescoCassano2",11), Ferie(4,"LucianoFuentes4",19), Ferie(5,"ValerioVigliano5",7)
    , Ferie(6,"ConducenteMaestro6"), Ferie(7,"ProVa7",19), Ferie(8,"MAttoMattesi8",35))
  val remainingFerieListNext:List[Ferie]  = List(Ferie(2,"FrancescoCassano2",35), Ferie(4,"LucianoFuentes4",35), Ferie(5,"ValerioVigliano5",35)
    , Ferie(6,"ConducenteMaestro6",35), Ferie(7,"ProVa7",35), Ferie(8,"MAttoMattesi8",35))
  val alreadyAssenzaInPeriod: Assenza = Assenza(2, Date.valueOf(LocalDate.of(2020,6,25)), Date.valueOf(LocalDate.of(2020,7,1)),malattia = false)
  val tooLongFerie: Assenza = Assenza(8, Date.valueOf(LocalDate.of(2020,11,1)), Date.valueOf(LocalDate.of(2020,12,25)),malattia = false)
  val malattia: Assenza = Assenza(8, Date.valueOf(LocalDate.of(2020,11,1)), Date.valueOf(LocalDate.of(2020,11,25)),malattia = true)
  val betweenYears: Assenza = Assenza(4, Date.valueOf(LocalDate.of(2020,12,20)), Date.valueOf(LocalDate.of(2021,1,3)),malattia = false)
  val malattiaBetweenYears: Assenza = Assenza(4, Date.valueOf(LocalDate.of(2020,12,20)), Date.valueOf(LocalDate.of(2020,1,15)),malattia = true)
  val startAfterEnd: Assenza = Assenza(8, Date.valueOf(LocalDate.of(2020,2,1)), Date.valueOf(LocalDate.of(2020,1,31)),malattia = false)
  val startSameAsEnd: Assenza = Assenza(8, Date.valueOf(LocalDate.of(2020,2,1)), Date.valueOf(LocalDate.of(2020,2,1)),malattia = false)
  val notSoManyFerie: Assenza = Assenza(2, Date.valueOf(LocalDate.of(2020,12,1)), Date.valueOf(LocalDate.of(2020,12,23)),malattia = false)
  val goodFerie: Assenza = Assenza(5, Date.valueOf(LocalDate.of(2020,12,1)), Date.valueOf(LocalDate.of(2020,12,7)),malattia = false)
  val assenzaListId2: List[Assenza] = List(Assenza(2,Date.valueOf(LocalDate.of(2020 ,6,22)),Date.valueOf(LocalDate.of(2020,6,29)),malattia = false,Some(1)),
                                           Assenza(2,Date.valueOf(LocalDate.of(2020,8,22)),Date.valueOf(LocalDate.of(2020,8,29)),malattia = false,Some(2)),
                                           Assenza(2,Date.valueOf(LocalDate.of(2020,5,22)),Date.valueOf(LocalDate.of(2020,5,29)),malattia = false,Some(6)),
                                           Assenza(2,Date.valueOf(LocalDate.of(2020,4,22)),Date.valueOf(LocalDate.of(2020,4,29)),malattia = true,Some(11)),
                                           Assenza(2,Date.valueOf(LocalDate.of(2020,3,22)),Date.valueOf(LocalDate.of(2020,6,29)),malattia = true,Some(16)))

}
