package testhttp

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassDB.Contratto
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteContratto.routeContratto
import utils.StartServer

import scala.concurrent.duration.DurationInt

object TestHttpContratto{
  private def startServer():Unit=MainServer
  private val getContratto: (String,Request[Int]) = ("/getcontratto",Request(Some(1)))
  private val getAllContratto: String = "/getallcontratto"
  private val createContratto: (String,Request[Contratto]) = ("/createcontratto",Request(Some(Contratto("Fisso",turnoFisso = true,partTime = false,1))))
  private val updateContratto: (String,Request[Contratto]) = ("/updatecontratto",Request(Some(Contratto("Rottatorio",turnoFisso = false,partTime = true,1,Some(1)))))
}

class TestHttpContratto extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpContratto._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(60).second)
  startServer()
  "The service" should {
    "return a contract for Post requests to the root path" in {
      Post(getContratto._1,getContratto._2) ~> routeContratto ~> check {
        responseAs[Response[Contratto]].statusCode==StatusCodes.OK.intValue
      }
    }
    "return a list of contract for Post requests to the root path" in {
      Post(getAllContratto) ~> routeContratto ~> check {
        responseAs[Response[List[Contratto]]].payload.isDefined
      }
    }
    "return a id for post request to the create contract" in {
      Post(createContratto._1,createContratto._2) ~> routeContratto ~> check {
        responseAs[Response[Contratto]].statusCode==StatusCodes.Created.intValue
      }
    }
    "return a int with value equals 1 when call update contract send id" in {
      Post(updateContratto._1,updateContratto._2) ~> routeContratto ~> check {
        responseAs[Response[Int]].payload.contains(1)
      }
    }

    }
}
