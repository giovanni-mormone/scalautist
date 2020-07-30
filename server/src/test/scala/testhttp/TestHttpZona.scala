package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB.Zona
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteZona.routeZona
import utils.StartServer

import scala.concurrent.duration.DurationInt

object TestHttpZona{
  private def startServer():Unit=MainServer
  private val getAllZona:  String               = "/getallzona"
  private val createZona: (String,Request[Zona]) = ("/createzona",Request(Some(Zona("Fabian"))))
  private val updateZona: (String,Request[Zona]) = ("/updatezona",Request(Some(Zona("%x12"))))
  private val deleteZona: (String,Request[Int]) = ("/deletezona",Request(Some(1)))
}
class TestHttpZona extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpZona._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)

  startServer()
  "The service" should {
    "return a id for request post to insert zona" in {
      Post(createZona._1, createZona._2) ~> routeZona ~> check {
        responseAs[Response[Zona]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a list of zona for Post requests to the root path" in {
      Post(getAllZona) ~> routeZona ~> check {
        responseAs[Response[List[Zona]]].payload.isDefined
      }
    }
    "return a id when update zona when zona not exist into database" in {
      Post(updateZona._1, updateZona._2) ~> routeZona ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a int equals 1 when delete one zona" in {
      Post(deleteZona._1, deleteZona._2) ~> routeZona ~> check {
        responseAs[Response[Int]].payload.contains(1)
      }
    }

  }
}
