package testhttp

import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassDB.Disponibilita
import caseclass.CaseClassHttpMessage.{Dates, Id, Request, Response}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import jsonmessages.JsonFormats._
import servermodel.routes.masterroute.MasterRouteDisponibilita._
import utils.StartServer
import messagecodes.{StatusCodes => statusCodes}

object TestHttpDisponibilita {
  private def startServer(): Unit = MainServer
  private val getAvailability: (String, Request[(Int, Dates)]) = ("/getdisponibilitainweek", Request(None))
  private val setAvailability: (String, Request[(Disponibilita, Id)]) = ("/setdisponibilita", Request(None))
  private val getExtraAvailability: (String, Request[(Int, Int, Int)]) = ("/extraavailability", Request(None))
}

class TestHttpDisponibilita extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpDisponibilita._
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

  }

}