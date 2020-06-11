package testdboperation.persona

import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie

object AssenzaOperationTestValues {
  val remainingFerieList= List(Ferie(2,"FrancescoCassano2",21), Ferie(4,"LucianoFuentes4",35), Ferie(5,"ValerioVigliano5",26)
    , Ferie(6,"ConducenteMaestro6",1), Ferie(7,"ProVa7",28), Ferie(8,"MAttoMattesi8",35))
  val remainingFerieListNext:List[Ferie]  = List(Ferie(2,"FrancescoCassano2",35), Ferie(4,"LucianoFuentes4",35), Ferie(5,"ValerioVigliano5",35)
    , Ferie(6,"ConducenteMaestro6",35), Ferie(7,"ProVa7",35), Ferie(8,"MAttoMattesi8",35))
  val alreadyAssenzaInPeriod: Assenza = Assenza(2, new Date(120,5,25), new Date(120,6,1),malattia = false)
  val tooLongFerie: Assenza = Assenza(5, new Date(120,9,1), new Date(120,11,1),malattia = false)
  val malattia: Assenza = Assenza(5, new Date(120,9,1), new Date(120,11,1),malattia = true)
  val betweenYears: Assenza = Assenza(8, new Date(120,11,1), new Date(121,1,1),malattia = false)
  val malattiaBetweenYears: Assenza = Assenza(8, new Date(120,11,1), new Date(121,1,1),malattia = true)
  val startAfterEnd: Assenza = Assenza(8, new Date(120,11,1), new Date(120,10,31),malattia = false)
  val startSameAsEnd: Assenza = Assenza(8, new Date(120,11,1), new Date(120,11,1),malattia = false)
  val notSoManyFerie: Assenza = Assenza(2, new Date(120,11,1), new Date(120,11,23),malattia = false)
  val goodFerie: Assenza = Assenza(2, new Date(120,11,1), new Date(120,11,19),malattia = false)

}
