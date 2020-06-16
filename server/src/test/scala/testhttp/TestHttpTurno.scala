package testhttp

import java.sql.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassDB.Turno
import caseclass.CaseClassHttpMessage.{Dates, Request, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteTurno.routeTurno
import utils.StartServer
import messagecodes.{StatusCodes => statusCodes}

object TestHttpTurno{
  private def startServer():Unit=MainServer
  private val badRequestGetTurniInDay: (String,Request[Int]) = ("/getturniinday",Request(None))
  private val badRequestGetTurniInWeek: (String,Request[Dates]) = ("/getturniinweek",Request(None))
}

class TestHttpTurno extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpTurno._
  startServer()
  "The service" should {
    "return a bad request for getTurniInDay if a bad request is sent" in {
      Post(badRequestGetTurniInDay._1,badRequestGetTurniInDay._2) ~> routeTurno ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
    "return a bad request for getTurniInWeek if a bad request is sent" in {
      Post(badRequestGetTurniInWeek._1,badRequestGetTurniInWeek._2) ~> routeTurno ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
  }
}
