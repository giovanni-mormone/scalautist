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
   *
   * Possible error code:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the contratto provided not exits in the db
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the contratto not allows for disponibilita to be defined but the disponibilita is present
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if the contratto is not fisso but there is a disponibilità present
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the contratto is fisso but there is not a disponibilità present
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the turni are not right according to the contratto
   */
  def recruit(persona:Assumi):Future[Response[Login]]

  /**
   * Layoff operations, delete one people from the database
   * @param ids
   *  id of the persona
   * @return
   * Future of type Response of Int
   *
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
   * Possible error code:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the persona alredy has an assenza in the period provided.
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the days between the given day are > of GIORNI_FERIE_ANNUI
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if the dates given in input are not of the same year.
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the start date is after the end date.
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the days of the assenza to insert are greater than the remaninig day of assenza for the persona.
   */
  def illnessPeriod(assenza: Assenza): Future[Response[Int]]

  /**
   *  Assign an illness to an employee
   * @param assenza case class that represent absence
   * @return Future of Response of type Int
   * Possible error code:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the persona alredy has an assenza in the period provided.
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the days between the given day are > of GIORNI_FERIE_ANNUI
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if the dates given in input are not of the same year.
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the start date is after the end date.
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the days of the assenza to insert are greater than the remaninig day of assenza for the persona.
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
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Terminale]]])
    }

    override def getAllZone: Future[Response[List[Zona]]] = {
      val request = Post(getURI("getallzona"))
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Zona]]])
    }

    override def getAllContract: Future[Response[List[Contratto]]] = {
      val request = Post(getURI("getallcontratto"))
      callHttp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Response[List[Contratto]]])
    }

    override def getAllPersone: Future[Response[List[Persona]]] = {
      val request = Post(getURI("getallpersona"))
      callHttp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Response[List[Persona]]])
    }

    override def getAllShift: Future[Response[List[Turno]]] = {
      val request = Post(getURI("getallturno"))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Turno]]])
    }

    override def salaryCalculation():Future[Response[List[Stipendio]]] = {
      val request = Post(getURI("calcolostipendio"),transform(Dates(Date.valueOf(LocalDate.now()))))
      callServerSalary(request)
    }


    override def getHolidayByPerson: Future[Response[List[Ferie]]] = {
      val request = Post(getURI("getholidaybyperson"),Request(Some(getYear)))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Ferie]]])
    }

    override def getTerminale(id: Int): Future[Response[Terminale]] = {
      val request = Post(getURI("getterminale"),transform(id))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[Terminale]])
    }

    override def getAllTerminale: Future[Response[List[Terminale]]] = {
      val request: HttpRequest = Post(getURI("getallterminale"))
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Terminale]]])
    }

    override def getAbsenceInYearForPerson(idPersona:Int): Future[Response[List[Assenza]]] = {
      val request: HttpRequest = Post(getURI("getAbsenceInYearForPerson"),Request(Some((idPersona,getYear))))
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[List[Assenza]]])
    }


    private def callServer(request: HttpRequest):Future[Response[Login]] =
      callHttp(request).flatMap(result=>Unmarshal(result).to[Response[Login]])

    private def callServerSalary(request: HttpRequest)=
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Stipendio]]])

  }

}
