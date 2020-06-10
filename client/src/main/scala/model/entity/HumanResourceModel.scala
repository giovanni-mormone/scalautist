package model.entity

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie, Id}
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Fabian Aspee Encina
 * RisorseUmaneModel extends [[model.Model]].
 * Interface for Human Resource Manager's operation on data
 */
trait HumanResourceModel extends AbstractModel{
  /**
   * Recruit operations, add people on the database
   * @param persona
   * Instance of Persona to save
   * @return
   * Future of type Login that contains username and password
   */
  def recruit(persona:Assumi):Future[Option[Login]]

  /**
   * Layoff operations, delete one people from the database
   * @param ids
   *  id of the persona
   * @return
   * Future of type Unit
   */
  def fires(ids:Id): Future[Option[Int]]
  /**
   * Layoff operations, delete a set of people from the database
   * @param ids
   * Set of Persona ids
   * @return
   * Future of type Unit
   */
  def firesAll(ids: Set[Int]): Future[Option[Int]]


  /**
   * Return employee list, list of Persona in the database
   * @return
   * Future of List of Persona
   */
  def getAllPersone: Future[Option[List[Persona]]]

  /**
   *  Assign an illness to an employee
   * @param assenza case class that represent absence
   * @return Future of type Int
   */
  def illnessPeriod(assenza: Assenza): Future[Option[Int]]

  /**
   *  Assign an illness to an employee
   * @param assenza case class that represent absence
   * @return Future of type Int
   */
  def holidays(assenza: Assenza):Future[Option[Int]]

  /**
   * Recover an employee's password
   * @param user
   * User that lost password
   * @return
   * Future of type Login data (only new password)
   */
  def passwordRecovery(user: Id): Future[Option[Login]]

  /**
   * method that return a Option of list of terminal, this can be empty if zone not contains a terminal
   * @param id identifies a zone into database, then select all terminale associate to id
   * @return Option of list of terminal that can be empty
   */
  def getTerminalByZone(id:Id): Future[Option[List[Terminale]]]

  /**
   * method that return Option of List of zone if exists
   * @return Option of zone if exists
   */
  def getAllZone:Future[Option[List[Zona]]]

  /**
   * Insert zona into database, this case class not contains id for zona
   *  in the init operation, but result contain your id
   *
   * @param zona case class that represent struct for a zona in database
   * @return Future of Option of zona that represent zona insert into database
   *          with your Id
   */
  def setZona(zona: Zona):Future[Option[Zona]]

  /**
   * Insert terminal into database, this case class not contains id for terminal
   * in the init operation, but result contain your id
   * @param terminale case class that represent struct for a terminal in database
   * @return Future of Option of Terminal that represent terminal insert into database
   *         with your Id
   */
  def setTerminal(terminale: Terminale):Future[Option[Terminale]]

  /**
   * method that return all contract in database
   * @return Option of list with all contract existing into database
   */
  def getAllContract:Future[Option[List[Contratto]]]

  /**
   * method that return all shift in database
   * @return Option of list with all shift existing into database
   */
  def getAllShift:Future[Option[List[Turno]]]

  /**
   * Method that calculus salary for all person in the system, this method is call every 30 days
   * @return Future of Option of List of Stipendio, for details of Stipendio,
   *         see [[caseclass.CaseClassDB.Stipendio]]
   */
  def salaryCalculation():Future[Option[List[Stipendio]]]

  /**
   *  Method that obtains salary for a person
   * @param id id that represent case class that contains id of a persona
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getSalary(id:Id):Future[Option[List[Stipendio]]]

  /**
   * Method that obtains all day of holiday of a persona
   * @return Option of List with all day of holiday of a persona
   */
  def getHolidayByPerson:Future[Option[List[Ferie]]]

}

/**
 * Companin object of [[model.entity.HumanResourceModel]]. [Singleton]
 * Human Resource Model interface implementation with http request.
 */
object HumanResourceModel {

  private val instance = new HumanResourceHttp()

  def apply(): HumanResourceModel = instance

  private class HumanResourceHttp extends HumanResourceModel{

    override def recruit(assumi: Assumi): Future[Option[Login]] = {
      val request = Post(getURI("hirePerson"), assumi)
      callServer(request)
    }

    override def passwordRecovery(idUser: Id): Future[Option[Login]] = {
      val request = Post(getURI("recoverypassword"),idUser)
      callServer(request)
    }
    private def callServer(request: HttpRequest) =
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Option[Login]])

    override def illnessPeriod(assenza: Assenza): Future[Option[Int]] = createRequest(assenza)

    override def holidays(assenza: Assenza): Future[Option[Int]] = createRequest(assenza)


    override def fires(ids: Id): Future[Option[Int]] = {
      val request = Post(getURI("deletepersona"), ids)
      callRequest(request)
    }

    override def firesAll(ids: Set[Int]): Future[Option[Int]] = {
      val request = Post(getURI("deleteallpersona"), ids.map(id=>Id(id)).toList)
      callRequest(request)
    }

    private def createRequest(absence: Assenza):Future[Option[Int]] = {
      val request = Post(getURI("addabsence"), absence)
      callRequest(request)
    }

    private def callRequest(request:HttpRequest):Future[Option[Int]] =
      callHtpp(request).collect{case value if value.isDefined=> Some(value.head.status.intValue());case _ => None}

    override def getTerminalByZone(id: Id): Future[Option[List[Terminale]]] = {
      val request: HttpRequest = Post(getURI("getterminalebyzona"), id)
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Option[List[Terminale]]])
    }

    override def getAllZone: Future[Option[List[Zona]]] = {
      val request = Post(getURI("getallzona"))
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Option[List[Zona]]])
    }

    override def getAllContract: Future[Option[List[Contratto]]] = {
      val request = Post(getURI("getallcontratto"))
      callHtpp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Option[List[Contratto]]])
    }

    override def getAllPersone: Future[Option[List[Persona]]] = {
      val request = Post(getURI("getallpersona"))
      callHtpp(request).flatMap(resultRequest =>Unmarshal(resultRequest).to[Option[List[Persona]]])
    }

    override def getAllShift: Future[Option[List[Turno]]] = {
      val request = Post(getURI("getallturno"))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[List[Turno]]])
    }

    override def salaryCalculation():Future[Option[List[Stipendio]]] = {
      val request = Post(getURI("calcolostipendio"))
      callServerSalary(request)
    }

    override def getSalary(id: Id): Future[Option[List[Stipendio]]] = {
      val request = Post(getURI("getstipendio"))
      callServerSalary(request)
    }
    private def callServerSalary(request: HttpRequest)=
        callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[List[Stipendio]]])

    override def setZona(zona: Zona): Future[Option[Zona]] = {
      val request = Post(getURI("createzona"))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[Zona]])
    }

    override def setTerminal(terminale: Terminale): Future[Option[Terminale]] = {
      val request = Post(getURI("createterminale"))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[Terminale]])
    }

    override def getHolidayByPerson: Future[Option[List[Ferie]]] = {
      val request = Post(getURI("getholidaybypersona"))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Option[List[Ferie]]])
    }
  }

}
