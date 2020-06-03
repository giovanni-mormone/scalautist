package model.entity

import model.AbstractModel
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import caseclass.CaseClassDB.{Assenza, Contratto, Login, Persona, Stipendio, Terminale, Turno, Zona}
import java.sql.Date

import akka.http.scaladsl.model.HttpRequest
import caseclass.CaseClassHttpMessage.{Assumi, Id}
import model.utils.ModelUtils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
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
   * Assign an illness to an employee
   *
   * @param idPersona
   * Employee id
   * @param startDate
   * Date of start of illness period
   * @param endDate
   * Date of end of illness period
   * @return
   * Future of type Unit
   */
  def illnessPeriod(idPersona: Int, startDate: Date, endDate: Date): Future[Option[Int]]

  /**
   * Assign a holiday period to an employee
   * @param idPersona
   * Employee id
   * @param startDate
   * Date of start of holiday period
   * @param endDate
   * Date of end of holiday period
   * @return
   * Future of type Unit
   */
  def holidays(idPersona: Int, startDate: Date, endDate: Date):Future[Option[Int]]

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
   * method that return all contract in database
   * @return Option of list with all contract existing into database
   */
  def getAllContract:Future[Option[List[Contratto]]]

  /**
   * method that return all shift in database
   * @return Option of list with all shift existing into database
   */
  def getAllShift:Future[Option[List[Turno]]]

  def salaryCalculation():Future[Option[List[Stipendio]]]

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
      loginCase(request)
      promise.future
    }

    override def passwordRecovery(idUser: Id): Future[Option[Login]] = {
      val request = Post(getURI("recoverypassword"),idUser)
      loginCase(request)
      promise.future
    }

    private def loginCase(request:HttpRequest)(implicit credential: Promise[Option[Login]]): Unit={
      doHttp(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Option[Login]].onComplete{result=>success(result,credential)}
        case t => failure(t.failed,credential)
      }
    }

    override def fires(ids: Id): Future[Option[Int]] = {
      val request = Post(getURI("deletepersona"), ids)
      callRequest(request)
      result.future
    }

    override def firesAll(ids: Set[Int]): Future[Option[Int]] = {
      val request = Post(getURI("deleteallpersona"), ids.map(id=>Id(id)).toList)
      callRequest(request)
      result.future
    }

    override def getAllPersone: Future[Option[List[Persona]]] = {
      val request = Post(getURI("getallpersona"))
      doHttp(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Option[List[Persona]]].onComplete(result => success(result,list))
        case t => failure(t.failed,list)
      }
      list.future
    }

    override def illnessPeriod(idPersona: Int, startDate: Date, endDate: Date): Future[Option[Int]] = {
      val absence = Assenza(idPersona, startDate, endDate, malattia = true)
      createRequest(absence)
      result.future
    }

    override def holidays(idPersona: Int, startDate: Date, endDate: Date): Future[Option[Int]] = {
      val absence = Assenza(idPersona, startDate, endDate, malattia = false)
      createRequest(absence)
      result.future
    }

    private def createRequest(absence: Assenza):Unit = {
      val request = Post(getURI("addabsence"), absence)
      callRequest(request)
    }

    private def callRequest(request:HttpRequest)(implicit promise:Promise[Option[Int]]):Unit ={
      doHttp(request).onComplete{
        case Success(value) => promise.success(Some(value.status.intValue()))
        case Failure(_) => promise.success(None)
      }
    }

    override def getTerminalByZone(id: Id): Future[Option[List[Terminale]]] = {

      val request = Post(getURI("getterminalebyzona"), id)
      doHttp(request).onComplete{
        case Success(terminale) =>
          Unmarshal(terminale).to[Option[List[Terminale]]].onComplete{
            result=>success(result,promiseTer)
          }
        case t => failure(t.failed,promiseTer)
      }
      promiseTer.future
    }

    override def getAllZone: Future[Option[List[Zona]]] = {
      val request = Post(getURI("getallzona"))
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Zona]]].onComplete{
            result=>success(result,promiseZona)
          }
        case t => failure(t.failed,promiseZona)
      }
      promiseZona.future
    }

    override def getAllContract: Future[Option[List[Contratto]]] = {
      val request = Post(getURI("getallcontratto"))
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Contratto]]].onComplete{
            result=>success(result,promiseCont)
          }
        case t => failure(t.failed,promiseCont)
      }
      promiseCont.future
    }

    override def getAllShift: Future[Option[List[Turno]]] = {
      val request = Post(getURI("getallturno"))
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Turno]]].onComplete{
            result=>success(result,promiseTurn)
          }
        case t => failure(t.failed,promiseTurn)
      }
      promiseTurn.future
    }

    override def salaryCalculation():Future[Option[List[Stipendio]]] = {
      val request = Post(getURI("calcolostipendio"))
      doHttp(request).onComplete{
        case Success(stipendio) =>
          Unmarshal(stipendio).to[Option[List[Stipendio]]].onComplete{
            result=>success(result,promiseStipendio)
          }
        case t => failure(t.failed,promiseStipendio)
      }
      promiseStipendio.future
    }
  }

}