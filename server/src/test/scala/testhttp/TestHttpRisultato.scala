package testhttp

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassHttpMessage.{Request, Response, ResultAlgorithm}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import jsonmessages.JsonFormats._
import jsonmessages.ImplicitDate._
import utils.StartServer5
import servermodel.routes.masterroute.MasterRouteRisultato._
import messagecodes.{StatusCodes => statusCodes}

object TestHttpRisultato {
  private def startServer(): Unit = MainServer
  private val replaceShift: (String, Request[(Int, Int)]) = ("/replaceshift", Request(None))
  private val getResultNotFound: (String, Request[(Int,Date,Date)]) = ("/getresultalgorithm", Request(Option((1,Date.valueOf(LocalDate.of(2020,1,18)),Date.valueOf(LocalDate.of(2020,2,18))))))
  private val getResultNotFound2: (String, Request[(Int,Date,Date)]) = ("/getresultalgorithm", Request(Option((30,Date.valueOf(LocalDate.of(2020,1,18)),Date.valueOf(LocalDate.of(2020,2,18))))))
  private val getResultBadRequest: (String, Request[(Int,Date,Date)]) = ("/getresultalgorithm", Request(None))
  private val getResultSuccess: (String, Request[(Int,Date,Date)]) = ("/getresultalgorithm", Request(Option((1,Date.valueOf(LocalDate.of(2020,6,1)),Date.valueOf(LocalDate.of(2020,7,31))))))
  private val getResultSuccess2: (String, Request[(Int,Date,Date)]) = ("/getresultalgorithm", Request(Option((3,Date.valueOf(LocalDate.of(2020,6,1)),Date.valueOf(LocalDate.of(2020,7,31))))))
}

class TestHttpRisultato extends AnyWordSpec with ScalatestRouteTest with StartServer5 {
  import TestHttpRisultato._
  startServer()

  "The service" should {
    "return a bad request for getTurniInDay if a bad request is sent" in {
      Post(replaceShift._1, replaceShift._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
    "return a notFound per getResult if not existing" in {
      Post(getResultNotFound._1, getResultNotFound._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].statusCode==statusCodes.NOT_FOUND
      }
    }
    "return a None per getResult if not existing" in {
      Post(getResultNotFound._1, getResultNotFound._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].payload.isEmpty
      }
    }
    "return a notFound per getResult if not existing in another terminal" in {
      Post(getResultNotFound2._1, getResultNotFound2._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].statusCode==statusCodes.NOT_FOUND
      }
    }
    "return a bad request for getResult if none is sent" in {
      Post(getResultBadRequest._1, getResultBadRequest._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].statusCode==statusCodes.BAD_REQUEST
      }
    }

    "return a success code for getResult  if a result is search" in {
      Post(getResultSuccess._1, getResultSuccess._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].statusCode==statusCodes.SUCCES_CODE
      }
    }
    "return a list with 14 person for specific terminal. 3" in {
      Post(getResultSuccess._1, getResultSuccess._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].payload.head.length==14
      }
    }
    "return a list with 3 person for specific terminal. 1" in {
      Post(getResultSuccess2._1, getResultSuccess2._2) ~> routeRisultato ~> check {
        responseAs[Response[List[ResultAlgorithm]]].payload.head.length==3
      }
    }

  }

}