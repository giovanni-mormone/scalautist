package model.entity

import model.AbstractModel
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import caseclass.CaseClassDB.{Assenza, Contratto, Login, Persona, Terminale, Turno, Zona}
import java.sql.Date

import akka.http.scaladsl.model.HttpRequest
import caseclass.CaseClassHttpMessage.Id

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
  def passwordRecovery(user: Int): Future[Option[Login]]
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


    override def recruit(assumi: Assumi): Future[Option[Login]] = {
      implicit val credential: Promise[Option[Login]] = Promise[Option[Login]]
      val request = Post(getURI("hirePerson"), assumi)
      loginCase(request)
      credential.future
    }
    private def loginCase(request:HttpRequest)(implicit credential: Promise[Option[Login]]): Unit={
      doHttp(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Option[Login]].onComplete{result=>success(result)}
        case t => failure(t.failed)
      }
    }
    override def fires(ids: Set[Int]): Future[Unit] = {
      val result = Promise[Unit]
      var list: List[Persona] = List()

      ids.foreach(x => list = Persona("","","",None,1,isNew = false,"",None,Some(x))::list)
      val request = Post(getURI("deleteallpersona"), list)
      doHttp(request).onComplete(_ => result.success())
      result.future
    }

    override def getAllPersone: Future[Option[List[Persona]]] = {
      implicit val list: Promise[Option[List[Persona]]] = Promise[Option[List[Persona]]]
      val request = Post(getURI("getallpersona"))
      doHttp(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Option[List[Persona]]].onComplete(result => success(result))
        case t => failure(t.failed)
      }
      list.future
    }

    override def illnessPeriod(idPersona: Int, startDate: Date, endDate: Date): Future[Unit] = {
      implicit val result: Promise[Unit] = Promise[Unit]
      val absence = Assenza(idPersona, startDate, endDate, malattia = true)
      val request = Post(getURI("addabsence"), absence)
      unitFuture(request)
      result.future
    }
    private def unitFuture(request:HttpRequest)(implicit result: Promise[Unit]):Unit ={
      doHttp(request).onComplete{
        case Success(_) => result.success()
        case Failure(exception) => result.failure(exception)
      }
    }
    override def holidays(idPersona: Int, startDate: Date, endDate: Date): Future[Unit] = {
      implicit val result: Promise[Unit] = Promise[Unit]
      val absence = Assenza(idPersona, startDate, endDate, malattia = false)
      val request = Post(getURI("addabsence"), absence)
      unitFuture(request)
      result.future
    }

    override def passwordRecovery(idUser: Int): Future[Option[Login]] = {
      implicit val promise: Promise[Option[Login]] = Promise[Option[Login]]
      val idPersona = Id(idUser)
      val request = Post(getURI("recoverypassword"),idPersona)
      loginCase(request)
      promise.future
    }

    override def getPersone(id: Int): Future[Option[Persona]] = {
      implicit val promise: Promise[Option[Persona]] = Promise[Option[Persona]]
      val idPersona = Id(id)
      val request = Post(getURI("getpersona"), idPersona)
      doHttp(request).onComplete{
        case Success(persona) =>
          Unmarshal(persona).to[Option[Persona]].onComplete{
            result=>success(result)
          }
        case t => failure(t.failed)
      }
      promise.future
    }

    override def getTerminalByZone(id: Int): Future[Option[List[Terminale]]] = {
      implicit val promise: Promise[Option[List[Terminale]]] = Promise[Option[List[Terminale]]]
      val idZona = Id(id)
      val request = Post(getURI("getterminalebyzona"), idZona)
      doHttp(request).onComplete{
        case Success(terminale) =>
          Unmarshal(terminale).to[Option[List[Terminale]]].onComplete{
            result=>success(result)
          }
        case t => failure(t.failed)
      }
      promise.future
    }

    override def getZone(id: Int): Future[Option[Zona]] = {
      implicit val promise: Promise[Option[Zona]] = Promise[Option[Zona]]
      val idZona = Id(id)
      val request = Post(getURI("getzona"), idZona)
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[Zona]].onComplete{
            result=>success(result)
          }
        case t => failure(t.failed)
      }
      promise.future

    }

    override def getAllContract: Future[Option[List[Contratto]]] = {
      implicit val promise: Promise[Option[List[Contratto]]] = Promise[Option[List[Contratto]]]
      val request = Post(getURI("getallcontratto"))
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Contratto]]].onComplete{
            result=>success(result)
          }
        case t => failure(t.failed)
      }
      promise.future

    }

    override def getAllShift: Future[Option[List[Turno]]] = {
      implicit val promise: Promise[Option[List[Turno]]] = Promise[Option[List[Turno]]]
      val request = Post(getURI("getallturno"))
      doHttp(request).onComplete{
        case Success(zona) =>
          Unmarshal(zona).to[Option[List[Turno]]].onComplete{
            result=>success(result)
          }
        case t => failure(t.failed)
      }
      promise.future

    }

  }

}