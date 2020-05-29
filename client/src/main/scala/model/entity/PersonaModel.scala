package model.entity

import model.Model
import model.ModelDispatcher
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword

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
  def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Unit]

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

    override def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Unit] = {
      val result = Promise[Unit]
      val newCredential = ChangePassword(user, oldPassword, newPassword)
      import jsonmessages.JsonMessageFormats._
      val request = Post(getURI("updatepassword"), newCredential) // cambiare request
      dispatcher.serverRequest(request).onComplete(_ => result.success() )
      result.future
    }
  }
}