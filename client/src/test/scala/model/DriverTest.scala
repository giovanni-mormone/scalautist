package model

import caseclass.CaseClassHttpMessage.Response
import messagecodes.{StatusCodes => statusCodes}
import model.entity.{DriverModel, ManagerModel}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.{ClientAkkaHttp, StartServer}

import scala.annotation.nowarn
import scala.concurrent.Future

class DriverTest extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp with StartServer{
  var model: DriverModel = _
  val user: Int = 6
  val day1: String = "Lunedi"
  val day2: String = "Martedi"

  override def beforeEach(): Unit = {
    model = DriverModel()
  }

  behavior of "Driver Availability"

  it should "return Availability" in {
    val future: Future[Response[List[String]]] = model.getDisponibilita(user)
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "set Availability without problems" in {
    val future: Future[Response[Int]] = model.setDisponibilita(day1, day2, user)
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }
}
