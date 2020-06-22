package testdboperation.assenza

import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
import dbfactory.operation.{AssenzaOperation, DisponibilitaOperation}
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import utils.StartServer2

import scala.concurrent.Future

class TestRimpiazzaAssenza extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer2 {
  import AssenzaReplaceOperationValues._
  behavior of "GetAllAbsence"
  it should "return list length 4 when get all absence with date 20200618" in {
    val getAllAbsence: Future[Option[List[InfoAbsenceOnDay]]] = AssenzaOperation.getAllAbsence(date)
    getAllAbsence map {allAbsence =>  assert(allAbsence.head.length==7)}
  }
  it should "return None if terminal and shift not contains some replacement" in {
    val getAllAvailability: Future[Option[List[InfoReplacement]]] = DisponibilitaOperation
      .allDriverWithAvailabilityForADate(idRisultatoWithoutReplace,idTerminalWithoutReplace,idTurnoWithoutReplace)
    getAllAvailability map {allAvailability =>  assert(allAvailability.isEmpty)}
  }
  it should "return list length 6 when a shift in terminal contains replace" in {
    val getAllAvailability: Future[Option[List[InfoReplacement]]] = DisponibilitaOperation
      .allDriverWithAvailabilityForADate(idRisultatoWithReplace,idTerminalWithReplace,idTurnoWithReplace)
    getAllAvailability map {allAvailability =>  assert(allAvailability.head.length==6)}
  }
  it should "return Some(1) when Driver absence is updated in risultato table" in {
    val updateAbsence: Future[Option[Int]] = AssenzaOperation.updateAbsence(idRisultatoForUpdate,idNewPerson)
    updateAbsence map {update =>println(update);  assert(update.contains(1))}
  }
  it should "return list length 6 next to update absence with date 20200618" in {
    val getAllAbsence: Future[Option[List[InfoAbsenceOnDay]]] = AssenzaOperation.getAllAbsence(date)
    getAllAbsence map {allAbsence =>  assert(allAbsence.head.length==6)}
  }
  it should "return list length 5 next to update absence and driver have 3 shift in a day" in {
    val getAllAvailability: Future[Option[List[InfoReplacement]]] = DisponibilitaOperation
      .allDriverWithAvailabilityForADate(idRisultatoWithReplace,idTerminalWithReplace,idTurnoWithReplace)
    getAllAvailability map {allAvailability =>  assert(allAvailability.head.length==5)}
  }
  it should "return None if not exist absence in on day" in {
    val getAllAbsence: Future[Option[List[InfoAbsenceOnDay]]] = AssenzaOperation.getAllAbsence(date)
    getAllAbsence map {allAbsence =>  assert(allAbsence.head.length==6)}
  }
  it should "return StatusCodes.ERROR_CODE1 if idRisultato not exist" in {
    val idResultNotExist:Future[Option[Int]] = AssenzaOperation.updateAbsence(idRisultatoForUpdateNotExist,idNewPerson)
    idResultNotExist map {result => assert(result.contains(-1))}
  }
  it should "return StatusCodes.ERROR_CODE2 if idPersona not exist" in {
    val idPersonNotExist: Future[Option[Int]] = AssenzaOperation.updateAbsence(idRisultatoForUpdate,idNewPersonNotExist)
    idPersonNotExist map {person => assert(person.contains(-2))}
  }
  it should "return StatusCodes.ERROR_CODE1 if idResult not exist in database" in {
    val getAllAvailabilityIdResultNotExist: Future[Option[Int]] = DisponibilitaOperation
      .verifyIdRisultatoAndTerminalAndShift(idRisultatoForUpdateNotExist,idTerminal,idTurno)
    getAllAvailabilityIdResultNotExist map {allAvailability =>  assert(allAvailability.contains(-1))}
  }
  it should "return StatusCodes.ERROR_CODE2  if idTerminal not exist in database" in {
    val getAllAvailabilityIdTerminalNotExist: Future[Option[Int]] = DisponibilitaOperation
      .verifyIdRisultatoAndTerminalAndShift(idRisultato,idTerminalNotExist,idTurno)
    getAllAvailabilityIdTerminalNotExist map {allAvailability =>  assert(allAvailability.contains(-2))}
  }
  it should "return StatusCodes.ERROR_CODE3 if idTurno not exist in database" in {
    val getAllAvailabilityIdTurnoNotExist: Future[Option[Int]] = DisponibilitaOperation
      .verifyIdRisultatoAndTerminalAndShift(idRisultato,idTerminal,idTurnoNotExist)
    getAllAvailabilityIdTurnoNotExist map {allAvailability =>  assert(allAvailability.contains(-3))}
  }

  it should "return StatusCodes.SUCCESS_CODE if idResult, idTerminal and idTurno exist in database" in {
    val getAllAvailabilitySuccessCode: Future[Option[Int]] = DisponibilitaOperation
      .verifyIdRisultatoAndTerminalAndShift(idRisultato,idTerminal,idTurno)
    getAllAvailabilitySuccessCode map {allAvailability =>  assert(allAvailability.contains(1))}
  }
}
