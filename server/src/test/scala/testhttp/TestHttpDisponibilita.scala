package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB.Disponibilita
import caseclass.CaseClassHttpMessage.{Dates, Id, Request, Response}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import jsonmessages.JsonFormats._
import servermodel.routes.masterroute.MasterRouteDisponibilita._
import utils.StartServer
import messagecodes.{StatusCodes => statusCodes}

import scala.concurrent.duration.DurationInt

object TestHttpDisponibilita {
  private def startServer(): Unit = MainServer
  private val getAvailability: (String, Request[(Int, Dates)]) = ("/getdisponibilitainweek", Request(None))
  private val setAvailability: (String, Request[(Disponibilita, Id)]) = ("/setdisponibilita", Request(None))
  private val getExtraAvailability: (String, Request[(Int, Int, Int)]) = ("/extraavailability", Request(None))
  private val getExtraAvailability_Error1: (String, Request[(Int, Int, Int)]) = ("/extraavailability", Request(Some((9999, 1, 1))))
  private val getExtraAvailability_Error2: (String, Request[(Int, Int, Int)]) = ("/extraavailability", Request(Some((1, 9999, 1))))
  private val getExtraAvailability_Error3: (String, Request[(Int, Int, Int)]) = ("/extraavailability", Request(Some((1, 1, 9999))))
}

class TestHttpDisponibilita extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpDisponibilita._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)

  startServer()

  "The service" should {
    "return a bad request for getdisponibilitainweek if a bad request is sent" in {
      Post(getAvailability._1, getAvailability._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }

    "return a bad request for setDisponibilita if a bad request is sent" in {
      Post(setAvailability._1, setAvailability._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }

    "return a bad request for extraAvailability if a bad request is sent" in {
      Post(getExtraAvailability._1, getExtraAvailability._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }

    "return a error 1 for extraAvailability if wrong idRisultato is sent" in {
      Post(getExtraAvailability_Error1._1, getExtraAvailability_Error1._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE1)
      }
    }

    "return a error 2 for extraAvailability if wrong idTerminale is sent" in {
      Post(getExtraAvailability_Error2._1, getExtraAvailability_Error2._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE2)
      }
    }

    "return a error 3 for extraAvailability if wrong idTurno is sent" in {
      Post(getExtraAvailability_Error3._1, getExtraAvailability_Error3._2) ~> routeDisponibilita ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE3)
      }
    }

  }

}