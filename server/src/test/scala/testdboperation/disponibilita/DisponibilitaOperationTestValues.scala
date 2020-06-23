package testdboperation.disponibilita

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.Disponibilita

object DisponibilitaOperationTestValues {
  val idUser=20
  val date:Date= Date.valueOf(LocalDate.of(2020,6,18))
  val idUserWithoutAvailability=5
  val dateWithoutAvailability:Date= Date.valueOf(LocalDate.of(2020,6,18))
  val dateInitWeek:Date= Date.valueOf(LocalDate.of(2020,6,15))
  val idUserInitWeek=4
  val idUserFisso=19
  val dateFisso:Date= Date.valueOf(LocalDate.of(2020,6,18))
  val days: Array[String] = Array[String]("Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato","Domenica")
  val daysWithFree: Array[String] = Array[String]("Lunedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato","Domenica")
  val idUserWithAbsence=18
  val daysForAvailability: Array[String] = Array[String]("Giovedi",  "Domenica")
  val disponibilita: Disponibilita = Disponibilita(26,"Lunedi","Martedi")
  val idUserNotExist=888
  val disponibilitaWithError: Disponibilita = Disponibilita(26,"Lunedi","Lunedi")
  val idUserExist=18
  val dateSunday:Date= Date.valueOf(LocalDate.of(2020,6,21))
  val dateSaturday:Date= Date.valueOf(LocalDate.of(2020,6,20))
  val idUserWithOneDay=21
  val datInit:Date= Date.valueOf(LocalDate.of(2020,6,18))
}
