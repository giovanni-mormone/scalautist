package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassHttpMessage.{Dates, Request, Response}
import jsonmessages.JsonFormats._
import messagecodes.{StatusCodes => statusCodes}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteTurno.routeTurno
import utils.StartServer

import scala.concurrent.duration.DurationInt

object TestHttpTurno{
  private def startServer():Unit=MainServer
  private val badRequestGetTurniInDay: (String,Request[Int]) = ("/getturniinday",Request(None))
  private val badRequestGetTurniInWeek: (String,Request[Dates]) = ("/getturniinweek",Request(None))
}

class TestHttpTurno extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpTurno._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)

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
