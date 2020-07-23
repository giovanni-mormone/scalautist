package testhttp


import akka.http.scaladsl.testkit.ScalatestRouteTest
import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, ZonaTerminale}
import caseclass.CaseClassHttpMessage.{InfoAlgorithm, Request, Response}
import jsonmessages.JsonFormats._
import messagecodes.{StatusCodes => statusCodes}
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import servermodel.routes.masterroute.MasterRouteParametro._
import utils.StartServer4

object TestHttpParametri{
  private def startServer(): Unit = MainServer
  val parametro:Parametro = Parametro(treSabato = true,"First Run")
  val parametroWithoutName:Parametro = Parametro(treSabato = true,"")
  val zonaTerminale:List[ZonaTerminale] = List(ZonaTerminale(1,1),ZonaTerminale(1,2))
  val giornoInSettimana:Option[List[GiornoInSettimana]] = Option(List(GiornoInSettimana(1,1,1,10),GiornoInSettimana(1,2,2,10),GiornoInSettimana(1,3,3,10)))
  val infoAlgorithm: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,giornoInSettimana)
  val infoAlgorithmWithoutGiornoInSettimana: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,None)
  val infoAlgorithmWithoutZonaTerminale: InfoAlgorithm = InfoAlgorithm(parametro,List.empty,None)
  val infoAlgorithmWithoutNameParameters: InfoAlgorithm = InfoAlgorithm(parametroWithoutName,zonaTerminale,None)
  private val getAllParameters: String = "/getalloldparameters"
  private val saveParameter: (String, Request[InfoAlgorithm]) = ("/saveparameter", Request(Some(infoAlgorithm)))
  private val getAllParameters2: String = "/getalloldparameters"
  private val getParameterById: (String, Request[Int]) = ("/getoldparametersbyid", Request(Some(1)))
  private val getParameterById2: (String, Request[Int]) = ("/getoldparametersbyid", Request(None))
  private val getParameterById3: (String, Request[Int]) = ("/getoldparametersbyid", Request(Some(80)))
  private val saveParameter2: (String, Request[InfoAlgorithm]) = ("/saveparameter", Request(Some(infoAlgorithmWithoutZonaTerminale)))
  private val saveParameter3: (String, Request[InfoAlgorithm]) = ("/saveparameter", Request(Some(infoAlgorithmWithoutNameParameters)))
  private val saveParameter4: (String, Request[InfoAlgorithm]) = ("/saveparameter", Request(Some(infoAlgorithmWithoutGiornoInSettimana)))
  private val saveParameter5: (String, Request[InfoAlgorithm]) = ("/saveparameter", Request(None))

}
class TestHttpParametri  extends AnyWordSpec with ScalatestRouteTest with StartServer4{
  import TestHttpParametri._
  startServer()
  "The service" should {
    "return not found if not exist parameters" in {
      Post(getAllParameters) ~> routeParametro ~> check {
        responseAs[Response[List[Parametro]]].statusCode.equals(statusCodes.NOT_FOUND)
      }
    }
    "return success code if parameters is insert with success" in {
      Post(saveParameter._1,saveParameter._2) ~> routeParametro ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.SUCCES_CODE)
      }
    }
    "return success code if found parameter in database" in {
      Post(getAllParameters2) ~> routeParametro ~> check {
        responseAs[Response[List[Parametro]]].statusCode.equals(statusCodes.SUCCES_CODE)
      }
    }
    "return list length 1 because only one parameter are in database " in {
      Post(getAllParameters2) ~> routeParametro ~> check {
        responseAs[Response[List[Parametro]]].payload.head.length==1
      }
    }
    "return success code if parameter exist in database" in {
      Post(getParameterById._1,getParameterById._2) ~> routeParametro ~> check {
        responseAs[Response[InfoAlgorithm]].statusCode.equals(statusCodes.SUCCES_CODE)
      }
    }
    "return a bad request for getParameter if send None" in {
      Post(getParameterById2._1,getParameterById2._2) ~> routeParametro ~> check {
        responseAs[Response[InfoAlgorithm]].statusCode.equals(statusCodes.BAD_REQUEST)
      }
    }
    "return a not found if parameter not exist in database" in {
      Post(getParameterById3._1,getParameterById3._2) ~> routeParametro ~> check {
        responseAs[Response[InfoAlgorithm]].statusCode.equals(statusCodes.NOT_FOUND)
      }
    }
    "return a error code 3 if zona terminal is empty" in {
      Post(saveParameter2._1,saveParameter2._2) ~> routeParametro ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE3)
      }
    }
    "return a error code 3 if name for parameters is empty" in {
      Post(saveParameter3._1,saveParameter3._2) ~> routeParametro ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.ERROR_CODE3)
      }
    }
    "return a success code if insert have success but giornoInSettimana is None" in {
      Post(saveParameter4._1,saveParameter4._2) ~> routeParametro ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.SUCCES_CODE)
      }
    }
    "return a bad request for saveParameters if a None is sent" in {
      Post(saveParameter5._1,saveParameter5._2) ~> routeParametro ~> check {
        responseAs[Response[Int]].payload.contains(statusCodes.BAD_REQUEST)
      }
    }
  }
}
