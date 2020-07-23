package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage._
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.Future

/**
 * @author Fabian Aspee Encina
 * RisorseUmaneModel extends [[model.Model]].
 * Interface for Human Resource Manager's operation on data
 */
trait HumanResourceModel{
  /**
   * Recruit operations, add people on the database
   * @param persona
   * Instance of Persona to save
   * @return
   * Future of type Response of Login that contains username and password
   */
  def recruit(persona:Assumi):Future[Response[Login]]

  /**
   * Layoff operations, delete one people from the database
   * @param ids
   *  id of the persona
   * @return
   * Future of type Response of Int
   */
  def fires(ids:Int): Future[Response[Int]]
  /**
   * Layoff operations, delete a set of people from the database
   * @param ids
   * Set of Persona ids
   * @return
   * Future of Response of type Int
   */
  def firesAll(ids: Set[Int]): Future[Response[Int]]


  /**
   * Return employee list, list of Persona in the database
   * @return
   * Future of Response of List of Persona
   */
  def getAllPersone: Future[Response[List[Persona]]]

  /**
   *  Assign an illness to an employee
   * @param assenza case class that represent absence
   * @return Future of Response of type Int
   */
  def illnessPeriod(assenza: Assenza): Future[Response[Int]]

  /**
   *  Assign an illness to an employee
   * @param assenza case class that represent absence
   * @return Future of Response of type Int
   */
  def holidays(assenza: Assenza):Future[Response[Int]]

  /**
   * Recover an employee's password
   * @param user
   * User that lost password
   * @return
   * Future of Response of type Login data (only new password)
   */
  def passwordRecovery(user: Int): Future[Response[Login]]

  /**
   * method that return a Response of list of terminal, this can be empty if zone not contains a terminal
   * @param id identifies a zone into database, then select all terminale associate to id
   * @return Response of list of terminal that can be empty
   */
  def getTerminalByZone(id:Int): Future[Response[List[Terminale]]]

  /**
   * method that return a Response of terminal, this can be empty if terminal not exist
   * @param id identifies a terminal into database, then select this
   * @return Response of terminal that can be empty
   */
  def getTerminale(id:Int): Future[Response[Terminale]]

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

  /**
   * method that return Option of List of Terminale if exist
   * @return Option of List of Terminale
   */
  def getAllTerminale: Future[Response[List[Terminale]]]

  /**
   * method that return Option of List of zone if exists
   * @return Option of List of zone if exists
   */
  def getAllZone:Future[Response[List[Zona]]]

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
   * method that return all contract in database
   * @return Option of list with all contract existing into database
   */
  def getAllContract:Future[Response[List[Contratto]]]

  /**
   * method that return all shift in database
   * @return Option of list with all shift existing into database
   */
  def getAllShift:Future[Response[List[Turno]]]

  /**
   * Method that calculus salary for all person in the system, this method is call every 30 days
   * @return Future of Option of List of Stipendio, for details of Stipendio,
   *         see [[caseclass.CaseClassDB.Stipendio]]
   */
  def salaryCalculation():Future[Response[List[Stipendio]]]



  /**
   * Method that obtains all day of holiday of a persona
   * @return Option of List with all day of holiday of a persona
   */
  def getHolidayByPerson:Future[Response[List[Ferie]]]

  /**
   * Method that obtains all day of holiday of a persona
   * @return Option of List with all day of holiday of a persona
   */
  def getAbsenceInYearForPerson(idPersona:Int):Future[Response[List[Assenza]]]

}

/**
 * Companin object of [[model.entity.HumanResourceModel]]. [Singleton]
 * Human Resource Model interface implementation with http request.
 */
object HumanResourceModel {

  def apply(): HumanResourceModel = new HumanResourceHttp()

  private class HumanResourceHttp extends AbstractModel with HumanResourceModel{

    override def recruit(assumi: Assumi): Future[Response[Login]] = {
      val request = Post(getURI("hireperson"), transform(assumi))
      callServer(request)
    }

    override def passwordRecovery(idUser: Int): Future[Response[Login]] = {
      val request = Post(getURI("recoverypassword"),transform(idUser))
      callServer(request)
    }

    override def illnessPeriod(assenza: Assenza): Future[Response[Int]] = createRequest(assenza)

    override def holidays(assenza: Assenza): Future[Response[Int]] = createRequest(assenza)

    override def fires(ids: Int): Future[Response[Int]] = {
      val request = Post(getURI("deletepersona"), transform(ids))
      callRequest(request)
    }

    override def firesAll(ids: Set[Int]): Future[Response[Int]] = {
      val request = Post(getURI("deleteallpersona"), transform(ids.map(id=>id).toList))
      callRequest(request)
    }

    private def createRequest(absence: Assenza):Future[Response[Int]] = {
      val request = Post(getURI("addabsence"), transform(absence))
      callRequest(request)
    }

    override def getTerminalByZone(id: Int): Future[Response[List[Terminale]]] = {
      val request: HttpRequest = Post(getURI("getterminalebyzona"), transform(id))
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Terminale]]])
    }

    override def getAllZone: Future[Response[List[Zona]]] = {
      val request = Post(getURI("getallzona"))
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Zona]]])
    }

    override def getAllContract: Future[Response[List[Contratto]]] = {
      val request = Post(getURI("getallcontratto"))
      callHtpp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Response[List[Contratto]]])
    }

    override def getAllPersone: Future[Response[List[Persona]]] = {
      val request = Post(getURI("getallpersona"))
      callHtpp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Response[List[Persona]]])
    }

    override def getAllShift: Future[Response[List[Turno]]] = {
      val request = Post(getURI("getallturno"))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Turno]]])
    }

    override def salaryCalculation():Future[Response[List[Stipendio]]] = {
      val request = Post(getURI("calcolostipendio"),transform(Dates(Date.valueOf(LocalDate.now()))))
      callServerSalary(request)
    }

    override def setZona(zona: Zona): Future[Response[Zona]] = {
      val request = Post(getURI("createzona"),transform(zona))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Zona]])
    }

    override def getHolidayByPerson: Future[Response[List[Ferie]]] = {
      val request = Post(getURI("getholidaybyperson"),Request(Some(getYear)))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Ferie]]])
    }

    override def getTerminale(id: Int): Future[Response[Terminale]] = {
      val request = Post(getURI("getterminale"),transform(id))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Terminale]])
    }

    override def updateTerminale(terminale: Terminale): Future[Response[Int]] = {
      val request = Post(getURI("updateterminale"),transform(terminale))
      callRequest(request)
    }
    override def createTerminale(terminale: Terminale): Future[Response[Terminale]] = {
      val request = Post(getURI("createterminale"),transform(terminale))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Terminale]])
    }

    override def deleteTerminale(id: Int): Future[Response[Int]] = {
      val request = Post(getURI("deleteterminale"),transform(id))
      callRequest(request)
    }

    override def updateZona(zona: Zona): Future[Response[Int]] = {
      val request = Post(getURI("updatezona"), transform(zona))
      callRequest(request)
    }

    override def deleteZona(zona: Int): Future[Response[Zona]] = {
      val request = Post(getURI("deletezona"), transform(zona))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Zona]])
    }

    override def getAllTerminale: Future[Response[List[Terminale]]] = {
      val request: HttpRequest = Post(getURI("getallterminale"))
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Terminale]]])
    }

    override def getAbsenceInYearForPerson(idPersona:Int): Future[Response[List[Assenza]]] = {
      val request: HttpRequest = Post(getURI("getAbsenceInYearForPerson"),Request(Some((idPersona,getYear))))
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Assenza]]])
    }


    private def callServer(request: HttpRequest):Future[Response[Login]] =
      callHtpp(request).flatMap(result=>Unmarshal(result).to[Response[Login]])

    private def callRequest(request:HttpRequest):Future[Response[Int]] =
      callHtpp(request).flatMap(unMarshall)

    private def callServerSalary(request: HttpRequest)=
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Stipendio]]])

  }

}
