package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB.Terminale
import jsonmessages.JsonFormats._
import caseclass.CaseClassHttpMessage.{Request, Response}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteTerminale.routeTerminale
import utils.StartServer

import scala.concurrent.duration.DurationInt

object TestHttpTerminal{
  private def startServer():Unit=MainServer
  private val getTerminalByZone: (String,Request[Int]) = ("/getterminalebyzona",Request(None))
}
class TestHttpTerminal extends AnyWordSpec with ScalatestRouteTest with StartServer {

  import TestHttpTerminal._

  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)

  startServer()
  "The service" should {
    "return a contract for Post requests to the root path" in {
      Post(getTerminalByZone._1, getTerminalByZone._2) ~> routeTerminale ~> check {
        responseAs[Response[List[Terminale]]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
  }
}
