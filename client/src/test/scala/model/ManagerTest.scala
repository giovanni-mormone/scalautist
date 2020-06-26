package model

import java.sql.Date

import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement, Response}
import messagecodes.{StatusCodes => statusCodes}
import model.entity.ManagerModel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.ClientAkkaHttp

import scala.concurrent.Future

class ManagerTest extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp {
  var model: ManagerModel = _
  val date: Date = Date.valueOf("2020-06-15")
  val idTerminale: Int = 3
  val idTurno: Int = 5
  val idRisultato: Int = 291
  val idNewDriver: Int = 6


  override def beforeEach(): Unit = {
    model = ManagerModel()
  }

  behavior of "Absences manager"

  it should "return all absences but not Bad Request" in {
    val future: Future[Response[List[InfoAbsenceOnDay]]] = model.allAbsences()
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST)}
  }

  it should "return available driver but not Bad Request" in {
    val future: Future[Response[List[InfoReplacement]]] = model.extraAvailability(idTerminale, idTurno, idRisultato)
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST)}
  }

  it should "return a result but not Bad Request" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }
}
