package model.entity
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.{ChangePassword, Id, Request, Response}
import jsonmessages.JsonFormats._
import model.AbstractModel

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
   * future
   */
  def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Response[Int]]
  def getPersone(id:Request[Int]): Future[Response[Persona]]
}

/**
* Companin object of [[model.entity.PersonaModel]]. [Singleton]
* Contains the implementation of its interface methods with http requests.
*/
object PersonaModel {

  private val instance = new PersonaModelHttp()

  def apply(): PersonaModel = instance

  private class PersonaModelHttp extends PersonaModel{


    override def login(user: String, password: String): Future[Response[Persona]] = {
      val credential = Request(Some(Login(user, password)))
      val request = Post(getURI("loginpersona"), credential)
      tranformMarshal(request)
    }
    override def getPersone(id: Request[Int]): Future[Response[Persona]] = {
      val request = Post(getURI("getpersona"), id)
      tranformMarshal(request)
    }
    private def tranformMarshal(request: HttpRequest):Future[Response[Persona]]={
      callHtpp(request).flatMap(resultRequest=>Unmarshal(resultRequest).to[Response[Persona]])
    }
    override def changePassword(user: Int, oldPassword: String, newPassword: String): Future[Response[Int]] = {
      val result =Promise[Response[Int]]
      val newCredential = ChangePassword(user, oldPassword, newPassword)
      changePassword(result,newCredential)
      result.future
    }
    private def changePassword(result: Promise[Response[Int]],newCredential:ChangePassword): Unit ={
      val request = Post(getURI("changepassword"), Request(Some(newCredential))) // cambiare request
      doHttp(request).onComplete{
        case Success(t) => t.status match {
          case StatusCodes.OK => result.success(Response[Int](StatusCodes.OK.intValue,None))
          case StatusCodes.NotFound => result.success(Response[Int](StatusCodes.NotFound.intValue,None))
          case StatusCodes.InternalServerError => result.success(Response[Int](StatusCodes.InternalServerError.intValue,None))
        }
        case Failure(_) => result.success(Response[Int](StatusCodes.InternalServerError.intValue,None))
      }
    }
  }

}