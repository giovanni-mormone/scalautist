package model

import java.sql.Date

import caseclass.CaseClassDB.RichiestaTeorica
import caseclass.CaseClassHttpMessage.{AssignRichiestaTeorica, InfoAbsenceOnDay, InfoReplacement, Response}
import messagecodes.{StatusCodes => statusCodes}
import model.entity.ManagerModel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.ClientAkkaHttp
import utils.TransferObject.InfoRichiesta

import scala.concurrent.Future

class ManagerTest extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp {
  var model: ManagerModel = _
  val date: Date = Date.valueOf("2020-06-15")
  val date2: Date = Date.valueOf("2020-07-15")
  val richiesta = List((3, List((4,5), (5,4))))
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

  it should "return a Bad Request when saveParameters" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return a SuccessCode when saveParameters" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return a SuccessCode when saveParameters but giorno in settimana is None" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return InfoAlgorithm when idParameters exist in database" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return None if idParameter not exist in database" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return error if the list of richiestaTeorica is empty" in {
    val future: Future[Response[Int]] = model.defineTheoreticalRequest(InfoRichiesta(date, date2, richiesta, List(200)))
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }
}
