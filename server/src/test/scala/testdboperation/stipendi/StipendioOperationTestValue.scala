package testdboperation.stipendi

import java.sql.Date

import caseclass.CaseClassDB.Stipendio

object StipendioOperationTestValue {
  val stipendiId2: List[Stipendio] = List(Stipendio(2,1700.0,new Date(120,4,1),Some(1)), Stipendio(2,1700.0,new Date(120,3,1),Some(6)), Stipendio(2,3000.0,new Date(120,2,1),Some(11)),Stipendio(2,3000.0,new Date(120,1,1),Some(16)))
  val goodDateToCompute: Date = new Date(120,5,11)
}
