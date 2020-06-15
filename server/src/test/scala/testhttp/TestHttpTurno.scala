package testhttp

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassDB.Turno
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteTurno.routeTurno
import utils.StartServer
object TestHttpTurno{
  private def startServer():Unit=MainServer
  private val getTurno: (String,Request[Int]) = ("/getturno",Request(Some(1)))
  private val getAllTurno: String = "/getallturno"
  private val updateTurno: (String,Request[Turno]) = ("/updateturno",Request(Some(Turno("%x12","10-10",35))))
  private val deleteTurno: (String,Request[Int]) = ("/deleteturno",Request(Some(1)))
  private val createTurno: (String,Request[Turno]) = ("/createturno",Request(Some(Turno("%x12","10-10",35))))
}

class TestHttpTurno extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpTurno._
  startServer()
  "The service" should {
    "return a shift for Post requests to the root path" in {
      Post(getTurno._1, getTurno._2) ~> routeTurno ~> check {
        responseAs[Response[Turno]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a list of shift for Post requests to the root path" in {
      Post(getAllTurno) ~> routeTurno ~> check {
        responseAs[Response[List[Turno]]].payload.isDefined
      }
    }
    "return a id for update because the shift not exist" in {
      Post(updateTurno._1, updateTurno._2) ~> routeTurno ~> check {
        responseAs[Response[Int]].payload.isDefined
      }
    }
    "return int with value 1 when delete shift" in {
      Post(deleteTurno._1, deleteTurno._2) ~> routeTurno ~> check {
        responseAs[Response[Int]].payload.contains(1)
      }
    }
    "return a Int that represent id for insert shift" in {
      Post(createTurno._1, createTurno._2) ~> routeTurno ~> check {
        responseAs[Response[Turno]].payload.isDefined
      }
    }
  }
}
