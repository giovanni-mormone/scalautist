package model.entity

import model.Model
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.StatusCodes
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword
import model.utils.ResponceCode

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

/**
 * PersonaModel extends [[model.Model]].
 * Interface for Persona Entity operation
 */
trait PersonaModel extends Model {

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
      val person = Promise[Option[Persona]]
      val credential = Login(user, password)
      val request = Post(getURI("loginpersona"), credential)
      dispatcher.serverRequest(request).onComplete{
        case Success(result) =>
          Unmarshal(result).to[Persona].onComplete(dbPerson => person.success(dbPerson.toOption) )
      }
      person.future
    }

    override def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Int] = {
      val result = Promise[Int]
      val newCredential = ChangePassword(user, oldPassword, newPassword)
      import jsonmessages.JsonMessageFormats._
      val request = Post(getURI("updatepassword"), newCredential) // cambiare request
      dispatcher.serverRequest(request).onComplete{
        case Success(t) => t.status match {
          case StatusCodes.Accepted => result.success(ResponceCode.Success)
          case StatusCodes.NotFound => result.success(ResponceCode.NotFound)
          case StatusCodes.BadRequest => result.success(ResponceCode.DbError)
        }
        case Failure(_) => result.success(ResponceCode.UnknownError)
      }
      result.future
    }
  }
}