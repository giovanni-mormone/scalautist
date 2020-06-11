package testdboperation.persona

import java.sql.Date

import caseclass.CaseClassDB.{Disponibilita, Persona, StoricoContratto}
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import testdboperation.persona.LoginAndCrudValues.persona

object AssumiOperationTestsValues {
  private var daAssumere:Persona = Persona("Mattia","Mommo","1234567789",None,3,isNew = true,"",Some(2))
  private var daAssumere2:Persona = Persona("prova1","prova1","123456789",None,3,isNew = true,"",Some(2))
  private var daAssumere3:Persona = Persona("prova2","prova2","1234554321",None,3,isNew = true,"",Some(5))
  private var daAssumere4:Persona = Persona("prova3","prova3","1234554321",None,3,isNew = true,"",Some(2))
  private var contrattoFissoGoodFullTime:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,Some(3),Some(4))
  private var contrattoBad:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,99,Some(3),Some(4))
  private var contrattoRotatorioGood:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,8,None,None)
  private var contrattoFissobad2TurnoPartTime:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,3,Some(1),Some(2))
  private var contrattoFissobad1TurnoFullTime:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,Some(1),None)
  private var contrattoFissobadTurniSwitcheFullTime:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,Some(2),Some(1))
  private var contrattoFissobadTurniNotConsecutive:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,Some(2),Some(5))
  private var contrattoFissoGoodFullTimeLastFirstTurno:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,Some(6),Some(1))
  private var contrattoFissoGoodPartTime:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,3,Some(6),None)
  private var contrattoFissoBadPartTimeOnlySecondTurno:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,5,None,Some(1))
  private var disp:Disponibilita = Disponibilita("Lunes","Martes")

  val insertPersonaGood:Assumi = Assumi(daAssumere,contrattoFissoGoodFullTime,Some(disp))
  val insertPersonaNoSuchContratto: Assumi = Assumi(daAssumere,contrattoBad,Some(disp))
  val insertPersonaBadRuoloForDisp: Assumi = Assumi(persona,contrattoFissoGoodFullTime,Some(disp))
  val insertPersonaBadDispForTurno: Assumi = Assumi(daAssumere2,contrattoRotatorioGood,Some(disp))
  val insertPersonaBadDispForTurno2: Assumi = Assumi(daAssumere2,contrattoFissoGoodFullTime)
  val insertPersonaBadDispForTurno3: Assumi = Assumi(daAssumere2,contrattoFissobad2TurnoPartTime,Some(disp))
  val insertPersonaBadDispForTurno4: Assumi = Assumi(daAssumere2,contrattoFissobad1TurnoFullTime,Some(disp))
  val insertPersonaBadDispForTurno5: Assumi = Assumi(daAssumere2,contrattoFissoBadPartTimeOnlySecondTurno,Some(disp))
  val insertPersonaBadDispForTurno6: Assumi = Assumi(daAssumere2,contrattoFissobadTurniNotConsecutive,Some(disp))
  val insertPersonaBadDispForTurno7: Assumi = Assumi(daAssumere2,contrattoFissobadTurniSwitcheFullTime,Some(disp))
  val insertPersonaGoodPartTimeFisso: Assumi = Assumi(daAssumere3,contrattoFissoGoodPartTime,Some(disp))
  val insertPersonaGoodFullTimeFissoHeadTail:Assumi = Assumi(daAssumere4,contrattoFissoGoodFullTimeLastFirstTurno, Some(disp))

}
