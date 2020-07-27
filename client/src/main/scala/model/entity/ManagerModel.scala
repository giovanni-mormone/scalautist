package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage._
import jsonmessages.ImplicitDate._
import jsonmessages.JsonFormats._
import messagecodes.StatusCodes.{ERROR_CODE4, ERROR_CODE5, ERROR_CODE6}
import model.AbstractModel
import persistence.ConfigReceiverPersistence
import receiver.ConfigReceiver
import utils.TransferObject.InfoRichiesta

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.concurrent.Future

/**
 * @author Francesco Cassano
 *         ManagerModel extends [[model.Model]].
 *         Interface for System Manager's operation on data
 */
trait ManagerModel {

  /**
   * Method that return all rule that contains a group for the algorithm
   *
   * @return Future of Response of List of Regola that contains all Rule for group for the algorithm
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.NOT_FOUND]] if no rules was found
   */
  def groupRule():Future[Response[List[Regola]]]

  /**
   * Method that return all rule that contains a normal week and special week for the algorithm
   *
   * @return Future of Response of List of Regola that contains all Rule for a normal
   *         week and special week for the algorithm
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.NOT_FOUND]] if no rules was found
   */
  def weekRule():Future[Response[List[Regola]]]

  def consumeNotification(tag: Long,userId: Option[Int]): Unit

  def verifyExistedQueue(userId: Option[Int], f: (String, Long) => Unit): Unit


  /**
   * Returns a list that contains all absent people in the date of today
   *
   * @return
   * Future of List of absent [[caseclass.CaseClassHttpMessage.InfoAbsenceOnDay]]
   *
   */
  def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]]

  /**
   * Returns the list of employees available to replace the absent
   *
   * @param idTerminale
   * Id of terminal with empty shift
   * @param idTurno
   * Id of shift to replace
   * @param idRisultato
   * Id of work shift information to search for available employee
   * @return
   * Future of list of [[caseclass.CaseClassHttpMessage.InfoReplacement]]
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.NOT_FOUND]] if no rules was found
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if idResult not exist in database
   * [[messagecodes.StatusCodes.ERROR_CODE2]]  if idTerminal not exist in database
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if idTurno not exist in database
   */
  def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]]

  /**
   * Reassign chosen shift to another employee
   *
   * @param idRisultato
   * Id of shift to reassign
   * @param idNewDriver
   * Id of new driver to assign to shift
   * @return
   * result of operation
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if idRisultato not exist
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if idPersona not exist
   */
  def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]]

  /**
   * Define theoretical request for shifts
   *
   * @param info
   * information to build request body like an instance of [[InfoRichiesta]]
   * @return
   * result of operation
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the conditions on the dates are not respected
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the RichiesteTeoriche to insert are None
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if it fails the insert of [[caseclass.CaseClassDB.Richiesta]]
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the id of richiesta teorica are None
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the days to insert are None
   * [[messagecodes.StatusCodes.ERROR_CODE6]] if it's been asked to update some RichiestaTeorica but it not finds the old RichiestaTeorica in the db
   * [[messagecodes.StatusCodes.ERROR_CODE7]] if it's been asked to update some RichiestaTeorica but it not finds the Richiesta associated to it
   * [[messagecodes.StatusCodes.ERROR_CODE8]] if it's been asked to update some RichiestaTeorica but it not finds the Giorno to associate to it
   * [[messagecodes.StatusCodes.ERROR_CODE9]] if there is some terminaleID duplicated in the insert request
   * [[messagecodes.StatusCodes.ERROR_CODE10]]: Error in [[caseclass.CaseClassDB.RichiestaTeorica]] set
   * [[messagecodes.StatusCodes.ERROR_CODE11]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some days in the set don't exist
   * [[messagecodes.StatusCodes.ERROR_CODE12]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some shifts in the set don't exist
   */
  def defineTheoreticalRequest(info: InfoRichiesta): Future[Response[Int]]

  /**
   * method that call server for execute algorithm for assigment free day and shift
   *
   * @param info case class that contains all info for algorithm and yours execution
   * @return Future Response Int that represent status operation
   *         [[messagecodes.StatusCodes.SUCCES_CODE]] if algorithm init without problem
   *
   * Possible error codes:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if time frame have a problem, this can be:
   *                                          time frame less that 28 days, dates to the contrary
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if list with terminal contains some terminal that not exist in database
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if group contains some error, this can be:
   *                                          group with one date, date in group outside time frame, ruler in
   *                                          group not exist in database
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if normal week contains some error, this can be:
   *                                          idDay not correspond to day in week, ruler in week not exist in
   *                                          database, shift in week not exist in database
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if special week contains some error, this can be:
   *                                          idDay not correspond to day in week, ruler in week not exist in
   *                                          database, shift in week not exist in database or date in week
   *                                          is outside to time frame
   * [[messagecodes.StatusCodes.ERROR_CODE6]] if time frame not contains theoretical request
   * [[messagecodes.StatusCodes.ERROR_CODE7]] if some terminal not contains drivers
   * [[messagecodes.StatusCodes.ERROR_CODE8]] if not exist shift in database
   * [[messagecodes.StatusCodes.ERROR_CODE9]] if a driver not contains a contract
   * [[messagecodes.StatusCodes.ERROR_CODE10]] if the algorithm is already running
   */
  def runAlgorithm(info: AlgorithmExecute, method: String => Unit): Future[Response[Int]]

  /**
   * method that return result of the algorithm by terminal,
   * before calling this method. we verify that terminal not exist
   * in object terminals that exist only in the object [[ManagerModel]], that contains all terminals already called
   *
   * @param idTerminale represent a terminal in database
   * @param dataI       represent init date for which you want to start
   * @param dataF       represent finish date for this call
   * @return Future of Response of List of Result Algorithm, for description of this case class, view
   *         [[caseclass.CaseClassHttpMessage.ResultAlgorithm]]
   */
  def getResultAlgorithm(idTerminale: Int, dataI: Date, dataF: Date): Future[Response[(List[ResultAlgorithm], List[Date])]]

  /**
   * return all parameter existing in database
   */
  def getOldParameter: Future[Response[List[Parametro]]]

  /**
   * method that return specific parameters with yours configuration
   *
   * @param idParameters represent idParameter existing in database
   * @return Future Response InfoAlgorithm with all information saved in the database
   */
  def getParameterById(idParameters: Int): Future[Response[InfoAlgorithm]]

  /**
   * method which allow save all information relationship with parameters
   *
   * @param parameters case class that represent information for parameters, this case class contains
   *                   parametro: Parametro, giornoInSettimana: List[GiornoInSettimana]
   * @return Future Response Int with status of operation
   */
  def saveParameters(parameters: InfoAlgorithm): Future[Response[Int]]

  /**
   * method that return a Response of terminal, this can be empty if terminal not exist
   *
   * @param id identifies a terminal into database, then select this
   * @return Response of terminal that can be empty
   */
  def getTerminale(id: Int): Future[Response[Terminale]]

  /**
   * method that return Option of List of zone if exists
   *
   * @return Option of List of zone if exists
   */
  def getAllZone: Future[Response[List[Zona]]]

  def verifyOldResult(dataToCheck: CheckResultRequest): Future[Response[List[Option[Int]]]]

  /**
   * method that return Option of List of Terminale if exist
   * @return Option of List of Terminale
   */
  def getAllTerminale: Future[Response[List[Terminale]]]

  /**
   * Insert zona into database, this case class not contains id for zona
   *  in the init operation, but result contain your id
   *
   * @param zona case class that represent struct for a zona in database
   * @return Future of Option of zona that represent zona insert into database
   *          with your Id
   */
  def setZona(zona: Zona):Future[Response[Zona]]

  /**
   * method that update a Zona instance and return a Response of Zona, this can be empty if it fails
   *
   * @param zona zona we want update in database
   * @return Future of Response of Int, can be None if operation if update another case is
   *         some with id of zona
   */
  def updateZona(zona: Zona): Future[Response[Int]]

  /**
   * method that delete a Zona instance and return a Response of Zona, this can be empty if it fails
   *
   * @param zona id that represent a zone in database
   * @return Future of Response of Zona
   */
  def deleteZona(zona: Int): Future[Response[Zona]]

  /**
   * Insert terminal into database, this case class not contains id for terminal
   * in the init operation, but result contain your id
   * @param terminale case class that represent struct for a terminal in database
   * @return Future of Response of Terminal that represent terminal insert into database
   *         with your Id
   */
  def createTerminale(terminale:Terminale): Future[Response[Terminale]]

  /**
   * method that return a None if terminal if update and Int if terminal if insert
   * @param terminale identifies a terminal we want update into database
   * @return Response of Int that can be empty
   */
  def updateTerminale(terminale:Terminale): Future[Response[Int]]

  /**
   * method that delete a terminal by id
   * @param id identifies a terminal into database, then select terminale associate to id and delete
   * @return Option of list of terminal that can be empty
   */
  def deleteTerminale(id:Int): Future[Response[Int]]

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

    private val notificationReceiver = ConfigReceiver("person_emitter")

    override def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]] = {
      val request = Post(getURI("allabsences"), transform(Dates(TODAY)))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoAbsenceOnDay]]])
    }

    override def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]] = {
      val request = Post(getURI("extraavailability"), transform((idTerminale, idTurno, idRisultato)))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoReplacement]]])
    }

    override def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]] = {
      val request = Post(getURI("replaceshift"), transform((idRisultato, idNewDriver)))
      callHttp(request).flatMap(unMarshall)
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
      callHttp(request).flatMap(unMarshall)
    }

    override def runAlgorithm(info: AlgorithmExecute,method:String=>Unit): Future[Response[Int]] = {
      val receiver = ConfigReceiver("info_algorithm")
      receiver.start()
      receiver.receiveMessage(method)
      val request = Post(getURI("executealgorithm"), transform(info))
      callHttp(request).flatMap(unMarshall)
    }

    val terminals: mutable.Map[(Int,Date,Date), Future[Response[(List[ResultAlgorithm],List[Date])]]] = collection.mutable.Map[(Int,Date,Date), Future[Response[(List[ResultAlgorithm],List[Date])]]]()

    override def getResultAlgorithm(idTerminale: Int, dataI: Date, dataF: Date): Future[Response[(List[ResultAlgorithm],List[Date])]] =
      terminals.getOrElseUpdate((idTerminale,dataI,dataF),getResultAlgorithmMemorize(idTerminale,dataI,dataF))


    def getResultAlgorithmMemorize(ids:Int,dateI:Date,dateF:Date):Future[Response[(List[ResultAlgorithm],List[Date])]] = {
      val request = Post(getURI("getresultalgorithm"), transform((ids, dateI,dateF)))
      callHttp(request).flatMap(response => {
        println(response)
        Unmarshal(response).to[Response[(List[ResultAlgorithm],List[Date])]]
      }
      )
    }

    override def getOldParameter: Future[Response[List[Parametro]]] = {
      val request = Post(getURI("getalloldparameters"))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[Parametro]]])
    }

    override def getParameterById(idParameters: Int): Future[Response[InfoAlgorithm]] = {
      val request = Post(getURI("getoldparametersbyid"), transform(idParameters))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[InfoAlgorithm]])
    }

    override def saveParameters(parameters: InfoAlgorithm): Future[Response[Int]] = {
      val request = Post(getURI("saveparameter"), transform(parameters))
      callHttp(request).flatMap(unMarshall)
    }

    override def verifyOldResult(dataToCheck: CheckResultRequest): Future[Response[List[Option[Int]]]] = {
      val request = Post(getURI("checkresultprealgorithm"), transform(dataToCheck))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[Option[Int]]]])
    }

    override def verifyExistedQueue(userId: Option[Int],f:(String,Long)=>Unit): Unit = {
      val nameQueue = "manager"+userId
      val receiver = ConfigReceiverPersistence(nameQueue,"assumi","licenzia","malattie","vacanze")
      receiver.start()
      receiver.receiveMessage(f)
    }

    override def consumeNotification(tag: Long,userId: Option[Int]): Unit = {
      val receiver = ConfigReceiverPersistence("manager"+userId,"assumi")
      receiver.consumeNotification(tag)
    }

    override def groupRule(): Future[Response[List[Regola]]] = {
      val request = Post(getURI("regolagroup"))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[Regola]]])
    }

    override def weekRule(): Future[Response[List[Regola]]] = {
      val request = Post(getURI("regolaweek"))
      callHttp(request).flatMap(response => Unmarshal(response).to[Response[List[Regola]]])
    }

    override def getTerminale(id: Int): Future[Response[Terminale]] = {
      val request = Post(getURI("getterminale"),transform(id))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Terminale]])
    }

    override def getAllZone: Future[Response[List[Zona]]] = {
      val request = Post(getURI("getallzona"))
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Zona]]])
    }

    override def getAllTerminale: Future[Response[List[Terminale]]] = {
      val request: HttpRequest = Post(getURI("getallterminale"))
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Terminale]]])
    }

    override def createTerminale(terminale: Terminale): Future[Response[Terminale]] = {
      val request = Post(getURI("createterminale"),transform(terminale))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Terminale]])
    }

    override def updateZona(zona: Zona): Future[Response[Int]] = {
      val request = Post(getURI("updatezona"), transform(zona))
      callRequest(request)
    }

    override def deleteZona(zona: Int): Future[Response[Zona]] = {
      val request = Post(getURI("deletezona"), transform(zona))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Zona]])
    }

    override def setZona(zona: Zona): Future[Response[Zona]] = {
      val request = Post(getURI("createzona"),transform(zona))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Zona]])
    }

    override def updateTerminale(terminale: Terminale): Future[Response[Int]] = {
      val request = Post(getURI("updateterminale"),transform(terminale))
      callRequest(request)
    }


    override def deleteTerminale(id: Int): Future[Response[Int]] = {
      val request = Post(getURI("deleteterminale"),transform(id))
      callRequest(request)
    }

  }
}
