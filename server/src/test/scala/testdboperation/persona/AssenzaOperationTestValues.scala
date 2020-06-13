package testdboperation.persona

import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie

object AssenzaOperationTestValues {
  val remainingFerieList= List(Ferie(2,"FrancescoCassano2",14), Ferie(4,"LucianoFuentes4",21), Ferie(5,"ValerioVigliano5",11)
    , Ferie(6,"ConducenteMaestro6",1), Ferie(7,"ProVa7",21), Ferie(8,"MAttoMattesi8",35))
  val remainingFerieListNext:List[Ferie]  = List(Ferie(2,"FrancescoCassano2",35), Ferie(4,"LucianoFuentes4",35), Ferie(5,"ValerioVigliano5",35)
    , Ferie(6,"ConducenteMaestro6",35), Ferie(7,"ProVa7",35), Ferie(8,"MAttoMattesi8",35))
  val alreadyAssenzaInPeriod: Assenza = Assenza(2, new Date(120,5,25), new Date(120,6,1),malattia = false)
  val tooLongFerie: Assenza = Assenza(8, new Date(120,10,1), new Date(120,11,25),malattia = false)
  val malattia: Assenza = Assenza(8, new Date(120,10,1), new Date(120,11,25),malattia = true)
  val betweenYears: Assenza = Assenza(4, new Date(120,11,20), new Date(121,0,15),malattia = false)
  val malattiaBetweenYears: Assenza = Assenza(4, new Date(120,11,20), new Date(121,0,15),malattia = true)
  val startAfterEnd: Assenza = Assenza(8, new Date(120,1,1), new Date(120,0,31),malattia = false)
  val startSameAsEnd: Assenza = Assenza(8, new Date(120,1,1), new Date(120,1,1),malattia = false)
  val notSoManyFerie: Assenza = Assenza(2, new Date(120,11,1), new Date(120,11,23),malattia = false)
  val goodFerie: Assenza = Assenza(5, new Date(120,11,1), new Date(120,11,8),malattia = false)
  val assenzaListId2: List[Assenza] = List(Assenza(2,new Date(120 ,5,22),new Date(120,5,29),false,Some(1)),
                                           Assenza(2,new Date(120,7,22),new Date(120,7,29),false,Some(2)),
                                           Assenza(2,new Date(120,4,22),new Date(120,4,29),false,Some(6)),
                                           Assenza(2,new Date(120,3,22),new Date(120,3,29),true,Some(11)),
                                           Assenza(2,new Date(120,2,22),new Date(120,5,29),true,Some(16)))

}
