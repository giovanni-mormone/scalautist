package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Giorno, Parametro, RichiestaTeorica}
import caseclass.CaseClassHttpMessage._
import jsonmessages.ImplicitDate._
import jsonmessages.JsonFormats._
import model.AbstractModel
import receiver.ConfigReceiver
import utils.TransferObject.InfoRichiesta

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * @author Francesco Cassano
 * ManagerModel extends [[model.Model]].
 * Interface for System Manager's operation on data
 */
trait ManagerModel {

  /**
   * Returns a list that contains all absent people in the date of today
   *
   * @return
   *         Future of List of absent [[caseclass.CaseClassHttpMessage.InfoAbsenceOnDay]]
   */
  def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]]

  /**
   * Returns the list of employees available to replace the absent
   *
   * @param idTerminale
   *                    Id of terminal with empty shift
   * @param idTurno
   *                Id of shift to replace
   * @param idRisultato
   *                    Id of work shift information to search for available employee
   * @return
   *         Future of list of [[caseclass.CaseClassHttpMessage.InfoReplacement]]
   */
  def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]]

  /**
   * Reassign chosen shift to another employee
   *
   * @param idRisultato
   *                    Id of shift to reassign
   * @param idNewDriver
   *                    Id of new driver to assign to shift
   * @return
   *         result of operation
   */
  def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]]

  /**
   * Define theoretical request for shifts
   * @param info
   *             information to build request body like an instance of [[InfoRichiesta]]
   * @return
   *         result of operation
   */
  def defineTheoreticalRequest(info: InfoRichiesta): Future[Response[Int]]

  /**
   * method that call server for execute algorithm for assigment free day and shift
   * @param info case class that contains all info for algorithm and yours execution
   * @return Future Response Int that represent status operation
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if algorithm init without problem
   */
  def runAlgorithm(info:AlgorithmExecute): Future[Response[Int]]

  /**
   * method that return result of the algorithm by terminal,
   * before calling this method. we verify that terminal not exist
   * in object terminals that exist only in the object [[ManagerModel]], that contains all terminals already called
   * @param idTerminale represent a terminal in database
   * @param dataI represent init date for which you want to start
   * @param dataF represent finish date for this call
   * @return Future of Response of List of Result Algorithm, for description of this case class, view
   *         [[caseclass.CaseClassHttpMessage.ResultAlgorithm]]
   */
  def getResultAlgorithm(idTerminale: Int, dataI: Date, dataF: Date): Future[Response[List[ResultAlgorithm]]]

  /**
   * return all parameter existing in database
   */
  def getOldParameter:Future[Response[List[Parametro]]]

  /**
   * method that return specific parameters with yours configuration
   * @param idParameters represent idParameter existing in database
   * @return Future Response InfoAlgorithm with all information saved in the database
   */
  def getParameterById(idParameters:Int):Future[Response[InfoAlgorithm]]

  /**
   * method which allow save all information relationship with parameters
   * @param parameters case class that represent information for parameters, this case class contains
   *                   parametro: Parametro, giornoInSettimana: List[GiornoInSettimana]
   * @return Future Response Int with status of operation
   */
  def saveParameters(parameters:InfoAlgorithm):Future[Response[Int]]
}

/**
 * Companion object for [[ManagerModel]]
 */
object ManagerModel {

  def apply():ManagerModel = new ManagerModelHttp()

  /**
   * HTTP implementation for [[ManagerModel]]
   */

  private class ManagerModelHttp extends AbstractModel with ManagerModel {


    private val TODAY: Date = Date.valueOf(LocalDate.now())
    private val WEEK: HashMap[Int, String] = HashMap(1 -> "Lunedi", 2 -> "Martedi", 3 -> "Mercoledi", 4 -> "Giovedi",
                                                      5 -> "Venerdi", 6 -> "Sabato", 7 -> "Domenica")

    override def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]] = {
      val request = Post(getURI("allabsences"), transform(Dates(TODAY)))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoAbsenceOnDay]]])
    }

    override def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]] = {
      val request = Post(getURI("extraavailability"), transform((idTerminale, idTurno, idRisultato)))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoReplacement]]])
    }

    override def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]] = {
      val request = Post(getURI("replaceshift"), transform((idRisultato, idNewDriver)))
      callHtpp(request).flatMap(unMarshall)
    }

    override def defineTheoreticalRequest(info: InfoRichiesta): Future[Response[Int]] = {
      val theoreticalRequest: List[RichiestaTeorica] =
        info.idTerminal.map(terminal => RichiestaTeorica(info.date, info.dateF, terminal))
      val dailyRequest: List[RequestGiorno] =
        info.info.flatMap(giorno => giorno._2.map(needed =>
          RequestGiorno(Giorno(needed._2, WEEK.getOrElse(giorno._1, "Vacanza"), giorno._1), needed._1)))
      val requestBody: AssignRichiestaTeorica = AssignRichiestaTeorica(theoreticalRequest, dailyRequest)
      println(requestBody)
      val request = Post(getURI("definedailyrequest"), transform(requestBody))
      callHtpp(request).flatMap(unMarshall)
    }

    override def runAlgorithm(info: AlgorithmExecute): Future[Response[Int]] = {
      val receiver = ConfigReceiver()
      receiver.start()
      receiver.receiveMessage()
      val request = Post(getURI("executealgorithm"), transform(info))
      callHtpp(request).flatMap(unMarshall)
    }

   val terminals: mutable.Map[(Int,Date,Date), Future[Response[List[ResultAlgorithm]]]] = collection.mutable.Map[(Int,Date,Date), Future[Response[List[ResultAlgorithm]]]]()

    override def getResultAlgorithm(idTerminale: Int, dataI: Date, dataF: Date): Future[Response[List[ResultAlgorithm]]] =
      terminals.getOrElseUpdate((idTerminale,dataI,dataF),getResultAlgorithmMemorize(idTerminale,dataI,dataF))


    def getResultAlgorithmMemorize(ids:Int,dateI:Date,dateF:Date):Future[Response[List[ResultAlgorithm]]] = {
      val request = Post(getURI("getresultalgorithm"), transform((ids, dateI,dateF)))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[ResultAlgorithm]]])
    }

    override def getOldParameter: Future[Response[List[Parametro]]] = {
      val request = Post(getURI("getalloldparameters"))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[Parametro]]])
    }

    override def getParameterById(idParameters: Int): Future[Response[InfoAlgorithm]] = {
      val request = Post(getURI("getoldparametersbyid"), transform(idParameters))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[InfoAlgorithm]])
    }

    override def saveParameters(parameters: InfoAlgorithm): Future[Response[Int]] = {
      val request = Post(getURI("saveparameter"), transform(parameters))
      callHtpp(request).flatMap(unMarshall)
    }
}
}

object t extends App{
  import scala.concurrent.ExecutionContext.Implicits.global
  val timeFrameInit: Date =Date.valueOf(LocalDate.of(2020,6,1))
  val timeFrameFinish: Date =Date.valueOf(LocalDate.of(2020,9,30))
  val terminals=List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)
  val firstDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val secondDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val thirdDateGroup: Date =Date.valueOf(LocalDate.of(2020,7,24))
  val secondDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,15))
  val firstDateGroup2: Date =Date.valueOf(LocalDate.of(2020,8,10))
  val secondDateGroup3: Date =Date.valueOf(LocalDate.of(2020,9,16))
  val firstDateGroup3: Date =Date.valueOf(LocalDate.of(2020,7,10))
  val thirdDateGroup2: Date =Date.valueOf(LocalDate.of(2020,7,15))
  val gruppi = List(GruppoA(1,List(firstDateGroup,secondDateGroup,thirdDateGroup),1),GruppoA(2,List(firstDateGroup2,secondDateGroup2,secondDateGroup3),2))
  val normalWeek = List(SettimanaN(1,2,15,3),SettimanaN(2,2,15,2))
  val specialWeek = List(SettimanaS(1,2,15,3,Date.valueOf(LocalDate.of(2020,7,8))),SettimanaS(1,3,15,3,Date.valueOf(LocalDate.of(2020,7,8))))
  val threeSaturday=false
  val algorithmExecute: AlgorithmExecute =
    AlgorithmExecute(timeFrameInit,timeFrameFinish,terminals,Some(gruppi),Some(normalWeek),Some(specialWeek),threeSaturday)
  ManagerModel().runAlgorithm(algorithmExecute).onComplete {
    case Failure(exception) => println(exception)
    case Success(value) =>println(":)")
  }
  while (true){}
}