package model.entity
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.{ChangePassword, Request, Response}
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.Future

/**
 * @author Francesco Cassano
 * PersonaModel extends [[model.Model]].
 * Interface for Persona Entity operation
 */
trait PersonaModel{

  /**
   * Check credential on the database, if the result is positive it returns personal data, else empty.
   *
   * @param user
   * string to identify user
   * @param password
   * user password string
   * @return
   * future of istance of persona or [[messagecodes.StatusCodes.NOT_FOUND]] if the person's informations are wrong.
   */
  def login(user: String, password: String): Future[Response[Persona]]

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
   *         Response of the status of the operation: if the operation doesn't go well returns
   *         a [[messagecodes.StatusCodes.NOT_FOUND]]
   */
  def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Response[Int]]

}

/**
* Companin object of [[model.entity.PersonaModel]]. [Singleton]
* Contains the implementation of its interface methods with http requests.
*/
object PersonaModel {

  private val instance = new PersonaModelHttp()

  def apply(): PersonaModel = instance

  private class PersonaModelHttp extends AbstractModel with PersonaModel{


    override def login(user: String, password: String): Future[Response[Persona]] = {
      val credential = Request(Some(Login(user, password)))
      val request = Post(getURI("loginpersona"), credential)
      tranformMarshal(request)
    }

    private def tranformMarshal(request: HttpRequest):Future[Response[Persona]]={
      callHttp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[Persona]])
    }
    override def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Response[Int]] = {
      val newCredential = ChangePassword(user, oldPassword, newPassword)
      val request = Post(getURI("changepassword"), transform(newCredential))
      callHttp(request).flatMap(unMarshall)
    }
  }

}