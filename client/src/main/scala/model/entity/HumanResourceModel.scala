package model.entity

import model.Model
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.{Get, Post}
import caseclass.CaseClassDB.{Assenza, Contratto, Login, Persona, Terminale, Turno, Zona}
import java.sql.Date

import caseclass.CaseClassHttpMessage.Id

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

/**
 * RisorseUmaneModel extends [[model.Model]].
 * Interface for Human Resource Manager's operation on data
 */
trait HumanResourceModel extends Model {
  /**
   * Recruit operations, add people on the database
   * @param persona
   * Instance of Persona to save
   * @return
   * Future of type Login that contains username and password
   */
  def recruit(persona:Persona):Future[Login]

  /**
   * Layoff operations, delete a set of people from the database
   * @param ids
   * Set of Persona ids
   * @return
   * Future of type Unit
   */
  def fires(ids:Set[Int]): Future[Unit]

  def getPersone(id:Int): Future[Option[Persona]]

  /**
   * Return employee list, list of Persona in the database
   * @return
   * Future of List of Persona
   */
  def getAllPersone: Future[List[Persona]]


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
  def illnessPeriod(idPersona: Int, startDate: Date, endDate: Date): Future[Unit]

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
  def holidays(idPersona: Int, startDate: Date, endDate: Date): Future[Unit]

  /**
   * Recover an employee's password
   * @param user
   * User that lost password
   * @return
   * Future of type Login data (only new password)
   */
  def passwordRecovery(user: Int): Future[Login]

  def getTerminalByZone(id:Int): Future[Option[List[Terminale]]]
  def getZone(id:Int):Future[Option[Zona]]
  def getAllContract:Future[Option[List[Contratto]]]
  def getAllShift:Future[Option[List[Turno]]]
}

/**
 * Companin object of [[model.entity.HumanResourceModel]]. [Singleton]
 * Human Resource Model interface implementation with http request.
 */
object HumanResourceModel {

  private val instance = new HumanResourceHttp()

  def apply(): HumanResourceModel = instance

  private class HumanResourceHttp extends HumanResourceModel{


    override def recruit(persona: Persona): Future[Login] = {
      val credential = Promise[Login]
      val request = Post(getURI("createpersona"), persona)
      dispatcher.serverRequest(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Login].onComplete(data => credential.success(data.get))
        case Failure(exception) => credential.failure(exception)
      }
      credential.future
    }

    override def fires(ids: Set[Int]): Future[Unit] = {
      val result = Promise[Unit]
      var list: List[Persona] = List()

      ids.foreach(x => list = Persona("","","",None,1,isNew = false,"",None,Some(x))::list)
      val request = Post(getURI("deleteallpersona"), list)
      dispatcher.serverRequest(request).onComplete(_ => result.success())
      result.future
    }

    override def getAllPersone: Future[List[Persona]] = {
      val list = Promise[List[Persona]]
      val request = Post(getURI("getallpersona"))
      dispatcher.serverRequest(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[List[Persona]].onComplete(t => list.success(t.get))
      }
      list.future
    }

    override def illnessPeriod(idPersona: Int, startDate: Date, endDate: Date): Future[Unit] = {
      val result = Promise[Unit]
      val absence = Assenza(idPersona, startDate, endDate, malattia = true)
      val request = Post(getURI("addabsence"), absence)
      dispatcher.serverRequest(request).onComplete{
        case Success(_) => result.success()
        case Failure(exception) => result.failure(exception)
      }
      result.future
    }


    override def holidays(idPersona: Int, startDate: Date, endDate: Date): Future[Unit] = {
      val result = Promise[Unit]
      val absence = Assenza(idPersona, startDate, endDate, malattia = false)
      val request = Post(getURI("addabsence"), absence)
      dispatcher.serverRequest(request).onComplete{
        case Success(_) => result.success()
        case Failure(exception) => result.failure(exception)
      }
      result.future
    }

    override def passwordRecovery(user: Int): Future[Login] = {
      val result = Promise[Login]
      val request = Get(getURI("getnewpassword" + "?user=" + user.toString))
      dispatcher.serverRequest(request).onComplete{
        case Success(newCredential) =>
          Unmarshal(newCredential).to[Login].onComplete(t => result.success(t.get))
      }
      result.future
    }

    override def getPersone(id: Int): Future[Option[Persona]] = {
      val promise = Promise[Option[Persona]]
      val idPersona = Id(id)
      val request = Post(getURI("getpersona"), idPersona)
      dispatcher.serverRequest(request).onComplete{
        case Success(persona) =>
          Unmarshal(persona).to[Option[Persona]].onComplete{
            result=>genericSuccess(promise,result)
          }
      }
      promise.future
    }

    override def getTerminalByZone(id: Int): Future[Option[List[Terminale]]] = {
      val promise = Promise[Option[List[Terminale]]]
      val idZona = Id(id)
      val request = Post(getURI("getterminalebyzona"), idZona)
      dispatcher.serverRequest(request).onComplete{
        case Success(terminale) =>
          Unmarshal(terminale).to[Option[List[Terminale]]].onComplete{
            result=>genericSuccess(promise,result)
          }
      }
      promise.future
    }

    override def getZone(id: Int): Future[Option[Zona]] = {
      val promise = Promise[Option[Zona]]
      val idZona = Id(id)
      val request = Post(getURI("getzona"), idZona)
      dispatcher.serverRequest(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[Zona]].onComplete{
            result=>genericSuccess(promise,result)
          }
      }
      promise.future

    }

    override def getAllContract: Future[Option[List[Contratto]]] = {
      val promise = Promise[Option[List[Contratto]]]
      val request = Post(getURI("getallcontratto"))
      dispatcher.serverRequest(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Contratto]]].onComplete{
            result=>genericSuccess(promise,result)
          }
      }
      promise.future

    }

    override def getAllShift: Future[Option[List[Turno]]] = {
      val promise = Promise[Option[List[Turno]]]
      val request = Post(getURI("getallturno"))
      dispatcher.serverRequest(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Turno]]].onComplete{
            result=>genericSuccess(promise,result)
          }
      }
      promise.future

    }
    private val none=None
    def genericSuccess[A](promise:Promise[Option[A]],function:Try[Option[A]]): Unit =function match {
      case Success(value) => promise.success(value)
      case Failure(exception) => promise.failure(exception)
    }
  }

}