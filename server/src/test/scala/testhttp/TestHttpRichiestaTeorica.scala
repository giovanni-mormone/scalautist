package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB.RichiestaTeorica
import caseclass.CaseClassHttpMessage.{AssignRichiestaTeorica, Request, RequestGiorno, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import utils.StartServer
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.masterroute.MasterRouteRichiestaTeorica._

import scala.concurrent.duration.DurationInt

object TestHttpRichiestaTeorica{
  private case class RichiestaTeoricaOp(path: String, request: Request[AssignRichiestaTeorica])
  private def startServer(): Unit = MainServer
  private val reqTeoBadRequest: RichiestaTeoricaOp = RichiestaTeoricaOp("/definedailyrequest", Request(None))
  private val reqTeoError: RichiestaTeoricaOp = RichiestaTeoricaOp("/definedailyrequest",
    Request(Some(AssignRichiestaTeorica(List.empty[RichiestaTeorica], List.empty[RequestGiorno]))))
}

class TestHttpRichiestaTeorica extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpRichiestaTeorica._
    implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)

  startServer()

  "The service" should {
    "return a bad request for new daily request if a bad request is sent" in {
      Post(reqTeoBadRequest.path, reqTeoBadRequest.request) ~> routeRichiestaTeorica ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }

    "return an different error code from bad request" in {
      Post(reqTeoError.path, reqTeoError.request) ~> routeRichiestaTeorica ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE5)
      }
    }
  }

}
