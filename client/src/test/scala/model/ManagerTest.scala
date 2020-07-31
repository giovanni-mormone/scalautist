package model

import java.sql.Date

import caseclass.CaseClassDB.Parametro
import caseclass.CaseClassHttpMessage._
import messagecodes.{StatusCodes => statusCodes}
import model.entity.ManagerModel
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.ClientAkkaHttp
import utils.TransferObject.InfoRichiesta

import scala.concurrent.Future

class ManagerTest extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp {
  import ManagerOperationTest._

  var model: ManagerModel = _
  val date: Date = Date.valueOf("2020-06-15")
  val date2: Date = Date.valueOf("2020-07-15")
  val richiesta = List((3, List((4,5), (5,4))))
  val idTerminale: Int = 3
  val idTurno: Int = 5
  val idRisultato: Int = 291
  val idNewDriver: Int = 6


  override def beforeEach(): Unit = {
    model = ManagerModel()
  }

  behavior of "Absences manager"

  it should "return all absences but not Bad Request" in {
    val future: Future[Response[List[InfoAbsenceOnDay]]] = model.allAbsences()
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST)}
  }

  it should "return available driver but not Bad Request" in {
    val future: Future[Response[List[InfoReplacement]]] = model.extraAvailability(idTerminale, idTurno, idRisultato)
    future map { info => assert(info.statusCode != statusCodes.BAD_REQUEST)}
  }

  it should "return a result but not Bad Request" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return a Bad Request when saveParameters" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return a SuccessCode when saveParameters" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return a SuccessCode when saveParameters but giorno in settimana is None" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return InfoAlgorithm when idParameters exist in database" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return None if idParameter not exist in database" in {
    val future: Future[Response[Int]] = model.replaceShift(idRisultato, idNewDriver)
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  it should "return error if the list of richiestaTeorica is empty" in {
    val future: Future[Response[Int]] = model.defineTheoreticalRequest(InfoRichiesta(date, date2, richiesta, List(200)))
    future map {info => assert(info.statusCode != statusCodes.BAD_REQUEST) }
  }

  behavior of "Result algorithm"
  it should "return None if terminal not contains result" in {
    val future: Future[Response[(List[ResultAlgorithm],List[Date])]] = model.getResultAlgorithm(idTerminaleNotContainsResult, dataINotContainsResult,dataFNotContainsResult)
    future map {info => assert(info.statusCode ==statusCodes.NOT_FOUND) }
  }
  it should "return list length 14 if terminal contains result" in {
    val future: Future[Response[(List[ResultAlgorithm],List[Date])]] = model.getResultAlgorithm(idTerminaleContainsResult2,dataIContainsResult, dataFContainsResult)
    future map {info => assert(info.payload.head._1.length==14) }
  }
  it should "return list length 14 if terminal contains result without call server" in {
    val future: Future[Response[(List[ResultAlgorithm],List[Date])]] = model.getResultAlgorithm(idTerminaleContainsResult2,dataIContainsResult, dataFContainsResult)
    future map {info => assert(info.payload.head._1.length==14) }
  }
  it should "return list length 3 if terminal contains result" in {
    val future: Future[Response[(List[ResultAlgorithm],List[Date])]] = model.getResultAlgorithm(idTerminaleContainsResult,dataIContainsResult, dataFContainsResult)
    future map {info => assert(info.payload.head._1.length==3) }
  }


  behavior of "Save Parameters"
  it should "return None if not exist parameters in database" in {
    val future: Future[Response[List[Parametro]]] = model.getOldParameter
    future map {info => assert(info.payload.isEmpty) }
  }
  it should "return success code if parameters is insert with success" in {
    val future: Future[Response[Int]] = model.saveParameters(infoAlgorithm)
    future map {info => assert(info.statusCode == statusCodes.SUCCES_CODE) }
  }
  it should "return InternalServer error if regola not exist in database" in {
    val future: Future[Response[Int]] = model.saveParameters(infoAlgorithmWithBadIdRegola)
    future map {info => assert(info.statusCode == statusCodes.ERROR_CODE4) }
  }
  it should "return list length 1 if save parameters have success and exist parameters in database" in {
    val future: Future[Response[List[Parametro]]] = model.getOldParameter
    future map {info => assert(info.payload.head.length==1) }
  }
  it should "return success code if insert ended with success and parameters not contains giorno in settimana" in {
    val future: Future[Response[Int]] = model.saveParameters(infoAlgorithmWithoutGiornoInSettimana)
    future map {info => assert(info.statusCode==statusCodes.SUCCES_CODE) }
  }
  it should "return error code 3 if parameters not contains zonaTerminale" in {
    val future:Future[Response[Int]] = model.saveParameters(infoAlgorithmWithoutZonaTerminale)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE3) }
  }
  it should "return error code 3 if name parameters is empty" in {
    val future: Future[Response[Int]] = model.saveParameters(infoAlgorithmWithoutNameParameters)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE3) }
  }
  it should "return list length 2 if save parameters have success and exist parameters in database" in {
    val future: Future[Response[List[Parametro]]] = model.getOldParameter
    future map {info => assert(info.payload.head.length==2) }
  }
  it should "return None if id parameters in database not exists" in {
    val future: Future[Response[InfoAlgorithm]] = model.getParameterById(parameterNotExist)
    future map {info => assert(info.payload.isEmpty) }
  }
  it should "return a instance of InfoAlgorithm with info of parameters" in {
    val future: Future[Response[InfoAlgorithm]] = model.getParameterById(parametersExist)
    future map {info => assert(info.payload.isDefined) }
  }
  it should "if parameters not contains giorno in settimana so this is None" in {
    val future: Future[Response[InfoAlgorithm]] = model.getParameterById(parametersWithoutGiornoInSettimana)
    future map {info => assert(info.payload.head.giornoInSettimana.isEmpty) }
  }
  it should "if parameters contains giorno in settimana so this isDefined" in {
    val future: Future[Response[InfoAlgorithm]] = model.getParameterById(parametersExist)
    future map {info => assert(info.payload.head.giornoInSettimana.isDefined) }
  }

  behavior of "Run Algorithm"

  it should "Return Error code 1 if difference between date is less that 28" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteDateError,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE1) }
  }
  it should "Return Error code 1 if date to the contrary" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteDateContrary,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE1) }
  }
  it should "Return Error code 2 if some terminal in list not exist in database " in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteTerminalNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE2) }
  }
  it should "Return Error code 2 if list of terminal is empty" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteTerminalEmpty,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE2) }
  }
  it should "Return Error code 3 if group not contains minimum two date" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteGroupWithOneDate,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE3) }
  }
  it should "Return Error code 3 if group contains a ruler that not exist in database" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteGroupWithRulerNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE3) }
  }
  it should "Return Error code 3 if group contains a date outside time frame algorithm" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteGroupContainDateOutsideTimeFrame,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE3) }
  }
  it should "Return Error code 4 if normal week contains idDay grater that 7" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteNormalWeekWithIdDayGreater7,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE4) }
  }
  it should "Return Error code 4 if normal week contains ruler that not exist in database" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteNormalWeekWithRulerNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE4) }
  }
  it should "Return Error code 4 if normal week contains a shift that not exist in database" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteNormalWeekWithShiftNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE4) }
  }
  it should "Return Error code 5 if special week contains idDay grater that 7" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteSpecialWeekWithIdDayGreater7,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE5) }
  }
  it should "Return Error code 5 if special week contains date outside time frame algorithm" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteSpecialWeekWithDateOutside,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE5) }
  }
  it should "Return Error code 5 if special week contains ruler that not exist in database" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteSpecialWeekWithRulerNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE5) }
  }
  it should "Return Error code 5 if special week contains a shift that not exist in database" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteSpecialWeekWithShiftNotExist,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE5) }
  }
  it should "Return Error code 6 if terminal not contains theorical request" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteTerminalWithoutTheoricRequest,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE6) }
  }
  it should "Return Error code 6 if some terminal in list not contains theorical request" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteTerminalsWithoutTheoricRequest,f)
    future map {info => assert(info.statusCode==statusCodes.ERROR_CODE6) }
  }
  it should "Return Success code if algorithm init without problem" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecute,f)
    future map {info => assert(info.statusCode==statusCodes.SUCCES_CODE) }
  }

  it should "Return Success code if algorithm init without problem and Group is None" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteWithGroupNone,f)
    future map {info => assert(info.statusCode==statusCodes.SUCCES_CODE) }
  }
  it should "Return Success code if algorithm init without problem and normal week is None" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteWithNormalWeekNone,f)
    future map {info => assert(info.statusCode==statusCodes.SUCCES_CODE) }
  }
  it should "Return Success code if algorithm init without problem and special week is None" in {
    val future: Future[Response[Int]] = model.runAlgorithm(algorithmExecuteWithSpecialWeekNone,f)
    future map {info => assert(info.statusCode==statusCodes.SUCCES_CODE) }
  }
}
