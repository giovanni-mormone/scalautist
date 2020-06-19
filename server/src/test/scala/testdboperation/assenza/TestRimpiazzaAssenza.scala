package testdboperation.assenza

import caseclass.CaseClassHttpMessage.InfoAbsenceOnDay
import dbfactory.operation.{AssenzaOperation, DisponibilitaOperation}
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import utils.StartServer2

import scala.concurrent.Future

class TestRimpiazzaAssenza extends  AsyncFlatSpec with BeforeAndAfterEach {
  import AssenzaReplaceOperationValues._
  behavior of "GetAllAbsence"
  it should "return list length 4 when get all absence with date 20200618" in {
    val getAllAbsence: Future[Option[List[InfoAbsenceOnDay]]] = AssenzaOperation.getAllAbsence(date)
    getAllAbsence map {allAbsence =>  assert(allAbsence.head.length==4)}
  }
  it should "return None if terminal and shift not contains some replacement" in {
    val getAllAvailability: Future[Option[List[(Int,String,String)]]] = DisponibilitaOperation
      .allDriverWithAvailabilityForADate(idRisultatoWithoutReplace,idTerminalWithoutReplace,idTurnoWithoutReplace)
    getAllAvailability map {allAvailability =>  assert(allAvailability.isEmpty)}
  }
  it should "return list length 4 when a shift in terminal contains replace" in {
    val getAllAvailability: Future[Option[List[(Int,String,String)]]] = DisponibilitaOperation
      .allDriverWithAvailabilityForADate(idRisultatoWithReplace,idTerminalWithReplace,idTurnoWithReplace)
    getAllAvailability map {allAvailability =>  assert(allAvailability.head.length==4)}
  }
  it should "return Some(1) when Driver absence is updated in risultato table" in {
    val updateAbsence: Future[Option[Int]] = AssenzaOperation.updateAbsence(idRisultatoForUpdate,idNewPerson)
    updateAbsence map {update =>println(update);  assert(update.contains(1))}
  }
  it should "return list length 3 next to update absence with date 20200618" in {
    val getAllAbsence: Future[Option[List[InfoAbsenceOnDay]]] = AssenzaOperation.getAllAbsence(date)
    getAllAbsence map {allAbsence =>  assert(allAbsence.head.length==4)}
  }
}
