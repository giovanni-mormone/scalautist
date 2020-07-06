package testhttp

import java.sql.Date
import java.time.LocalDate

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, GruppoA, Request, Response, SettimanaN, SettimanaS}
import jsonmessages.JsonFormats._
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteRisultato.routeRisultato
import utils.StartServer

import scala.concurrent.duration.DurationInt

object TestHttpAlgorithm{
  private def startServer():Unit=MainServer
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,7,31))
  val timeFrameInitError: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinishError: Date =Date.valueOf(LocalDate.of(2020,6,15))
  val terminals=List(1,2,3)
  val terminalWithoutTheoricRequest=List(4)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,6,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,6,11))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup),2))
  val gruppiWithRulerNotExist = List(GruppoA(1,List(firstDateGroup,secondDateGroup),20),GruppoA(1,List(firstDateGroup),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2))
  val normalWeekWithIdDayGreater7 = List(SettimanaN(1,2,15,3),SettimanaN(9,2,15,2))
  val normalWeekWithShiftNotExist = List(SettimanaN(1,2,15,3),SettimanaN(2,50,15,2))
  val specialWeek = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,6,8))))
  val specialWeekWithDateOutside  = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,6,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,12,8))))
  val threeSaturday=false

  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteWithGroupNone: AlgorithmExecute =algorithmExecute.copy(gruppo = None)
  val algorithmExecuteWithNormalWeekNone: AlgorithmExecute =algorithmExecute.copy(settimanaNormale = None)
  val algorithmExecuteWithSpecialWeekNone: AlgorithmExecute =algorithmExecute.copy(settimanaSpeciale = None)

  val algorithmExecuteDateError: AlgorithmExecute =
    AlgorithmExecute(timeFrameInitError,timeFrameFinishError,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteTerminalEmpty: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,List.empty,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteGroupWithRulerNotExist: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppiWithRulerNotExist),Some(normalWeek),Some(specialWeek),threeSaturday)
  val algorithmExecuteNormalWeekWithIdDayGreater7: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeekWithIdDayGreater7),Some(specialWeek),threeSaturday)
  val algorithmExecuteNormalWeekWithShiftNotExist: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeekWithShiftNotExist),Some(specialWeek),threeSaturday)

  val algorithmExecuteSpecialWeekWithDateOutside: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeekWithDateOutside),threeSaturday)

  val algorithmExecuteTerminalWithoutTheoricRequest: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminalWithoutTheoricRequest,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)

  private val badRequest1: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteDateError)))
  private val badRequest2: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteTerminalEmpty)))
  private val badRequest3: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteGroupWithRulerNotExist)))
  private val badRequest4: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteNormalWeekWithShiftNotExist)))
  private val badRequest5: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteTerminalWithoutTheoricRequest)))
  private val badRequest6: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteNormalWeekWithIdDayGreater7)))
  private val badRequest7: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteSpecialWeekWithDateOutside)))
  private val successCode1: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecute)))
  private val successCode2: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteWithGroupNone)))
  private val successCode3: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteWithNormalWeekNone)))
  private val successCode4: (String,Request[AlgorithmExecute]) = ("/executealgorithm",Request(Some(algorithmExecuteWithSpecialWeekNone)))

}
class TestHttpAlgorithm extends AnyWordSpec with ScalatestRouteTest with StartServer{
  import TestHttpAlgorithm._
  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(new DurationInt(5).second)
  startServer()
  "The service" should {
    "return badRequest if case class send have problem with time frame" in {
      Post(badRequest1._1, badRequest1._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return a bad request if terminal is empty" in {
      Post(badRequest2._1, badRequest2._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return a bad request if ruler not exist in database" in {
      Post(badRequest3._1, badRequest3._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return a bad request if shift not exist in database" in {
      Post(badRequest4._1, badRequest4._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return bad request if terminal not contains theorical request" in {
      Post(badRequest5._1, badRequest5._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return bad request if idDay is grater that 7" in {
      Post(badRequest6._1, badRequest6._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
    "return a success code if algorithm init without problem" in {
      Post(successCode1._1, successCode1._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a success code if algorithm init without problem and group is None" in {
      Post(successCode2._1, successCode2._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a success code if algorithm init without problem and normal week is None" in {
      Post(successCode3._1, successCode3._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a success code if algorithm init without problem and special week is None" in {
      Post(successCode4._1, successCode4._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.OK.intValue
      }
    }
    "return a bad request if date in special week is outside of time frame of the algorithm" in {
      Post(badRequest7._1, badRequest7._2) ~> routeRisultato ~> check {
        responseAs[Response[Int]].statusCode == StatusCodes.BadRequest.intValue
      }
    }
  }
}
