package model.entity
import model.AbstractModel
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.{ChangePassword, Id}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * @author Francesco Cassano
 * PersonaModel extends [[model.Model]].
 * Interface for Persona Entity operation
 */
trait PersonaModel extends AbstractModel {

  /**
   * Check credential on the database, if the result is positive it returns personal data, else empty.
   *
   * @param user
   * string to identify user
   * @param password
   * user password string
   * @return
   * future of istance of persona
   */
  def login(user: String, password: String): Future[Option[Persona]]

  /**
   * If old password is correct, it update user password on the database
   *
   * @param user
   * string to identify user
   * @param oldPassword
   * old user password string
   * @param newPassword
   * new user password string
   * @return
   * future
   */
  def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Int]
  def getPersone(id:Int): Future[Option[Persona]]
}

/**
* Companin object of [[model.entity.PersonaModel]]. [Singleton]
* Contains the implementation of its interface methods with http requests.
*/
object PersonaModel {

  private val instance = new PersonaModelHttp()

  def apply(): PersonaModel = instance

  private class PersonaModelHttp extends PersonaModel{

    override def login(user: String, password: String): Future[Option[Persona]] = {
      implicit val person: Promise[Option[Persona]] = Promise[Option[Persona]]
      val credential = Login(user, password)
      val request = Post(getURI("loginpersona"), credential)
      callHtpp(request)
      person.future
    }

    override def getPersone(id: Int): Future[Option[Persona]] = {
      implicit val promise: Promise[Option[Persona]] = Promise[Option[Persona]]
      val idPersona = Id(id)
      val request = Post(getURI("getpersona"), idPersona)
      callHtpp(request)
      promise.future
    }

    private def callHtpp(request: HttpRequest)(implicit promise: Promise[Option[Persona]]): Unit ={
      doHttp(request).onComplete{
        case Success(persona) =>
          Unmarshal(persona).to[Option[Persona]].onComplete{
            result=>success(result,promise)
          }
        case t => failure(t.failed,promise)
      }
    }
    override def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Int] = {
      val result = Promise[Int]
      val newCredential = ChangePassword(user, oldPassword, newPassword)
      changePassword(result,newCredential)
      result.future
    }
    private def changePassword(result: Promise[Int],newCredential:ChangePassword): Unit ={
      val request = Post(getURI("changepassword"), newCredential) // cambiare request
      doHttp(request).onComplete{
        case Success(t) => t.status match {
          case StatusCodes.OK => result.success(StatusCodes.OK.intValue)
          case StatusCodes.NotFound => result.success(StatusCodes.NotFound.intValue)
          case StatusCodes.InternalServerError => result.success(StatusCodes.InternalServerError.intValue)
        }
        case Failure(_) => result.success(StatusCodes.InternalServerError.intValue)
      }
    }
  }

}