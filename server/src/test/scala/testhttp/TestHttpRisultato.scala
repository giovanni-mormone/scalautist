package testhttp

import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassHttpMessage.{Request, Response}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import jsonmessages.JsonFormats._
import utils.StartServer
import servermodel.routes.masterroute.MasterRouteRisultato._
import messagecodes.{StatusCodes => statusCodes}

object TestHttpRisultato {
  private def startServer(): Unit = MainServer
  private val replaceShift: (String, Request[(Int, Int)]) = ("/replaceshift", Request(None))
}

class TestHttpRisultato extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpRisultato._
  startServer()

  "The service" should {
    "return a bad request for getTurniInDay if a bad request is sent" in {
      Post(replaceShift._1, replaceShift._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
  }

}