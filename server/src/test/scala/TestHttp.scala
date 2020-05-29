import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.SystemMaterializer
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import servermodel.StartServer
import akka.http.scaladsl.client.RequestBuilding.Post
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword
import jsonmessages.JsonFormats._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait HttpRequest {

  protected val uri = "http://localhost:8080/"
  protected val personaDummyRequest = "dummyPerson"
  protected val terminalDummyRequest = "dummyTerminal"
  protected val zoneDummyRequest = "dummyZona"
  protected val shiftDummyRequest = "dummyTurno"
  protected val dummyString = "ACAB: All Cannelloni Are Buoni"

  implicit val system = ActorSystem("test")
  implicit val materializer = SystemMaterializer(system)
  implicit val ex = system.dispatchers

  StartServer.startServer()
}

class TestHttpDummydb extends  AsyncFlatSpec with BeforeAndAfterEach with HttpRequest {

  behavior of "dummyRequest"
  it should "return some String from MasterRoutePersona" in {
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + personaDummyRequest)), Duration.Inf)
    val string = Unmarshal(request).to[String]
    string map (s => assert(s.equals(dummyString)))
  }

  it should "return some String from MasterRouteTerminale" in {
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + terminalDummyRequest)), Duration.Inf)
    val string = Unmarshal(request).to[String]
    string map (s => assert(s.equals(dummyString)))
  }

  it should "return some String from MasterRouteTurni" in {
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + shiftDummyRequest)), Duration.Inf)
    val string = Unmarshal(request).to[String]
    string map (s => assert(s.equals(dummyString)))
  }

  it should "return some String from MasterRouteZona" in {
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + zoneDummyRequest)), Duration.Inf)
    val string = Unmarshal(request).to[String]
    string map (s => assert(s.equals(dummyString)))
  }

}

class TestHttpOnlinedb extends  AsyncFlatSpec with BeforeAndAfterEach with HttpRequest {

  behavior of "Onlinedb Request for login and change password"
  it should "return Persona instance on login request" in {
    val user: String = "Francesco"
    val password: String = "admin2"
    val requestRoute: String = "loginpersona"
    val login: Login = Login(user, password)
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + requestRoute, login)), Duration.Inf)
    val persona = Unmarshal(request).to[Persona]
    persona map (res => assert(res.isInstanceOf[Persona]))
  }

  it should "return success on change password request" in {
    val user: Int = 2
    val oldPassword: String = "admin2"
    val newPassword: String = "admin2"
    val requestRoute: String = "updatepassword"
    val changePassword = ChangePassword(user, oldPassword, newPassword)
    import jsonmessages.JsonMessageFormats._
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + requestRoute, changePassword)), Duration.Inf)
    assert(request.status == StatusCodes.Accepted)
  }

  it should "return not found code on login if credential are wrong" in {
    val user: String = "aladin"
    val password: String = "admin2"
    val requestRoute: String = "loginpersona"
    val login: Login = Login(user, password)
    val request: HttpResponse =  Await.result(Http().singleRequest(Post(uri + requestRoute, login)), Duration.Inf)
    assert(request.status == StatusCodes.NotFound)
  }
}

