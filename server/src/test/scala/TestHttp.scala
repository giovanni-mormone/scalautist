import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.SystemMaterializer
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import servermodel.StartServer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait HttpRequest {

  protected var uri = "http://localhost:8080/"
  protected var personaDummyRequest = "dummyPerson"
  protected var terminalDummyRequest = "dummyTerminal"
  protected var zoneDummyRequest = "dummyZona"
  protected var shiftDummyRequest = "dummyTurno"

  implicit val system = ActorSystem("test")
  implicit val materializer = SystemMaterializer(system)
  implicit val ex = system.dispatchers

  StartServer.startServer()
}

class TestHttp extends  AsyncFlatSpec with BeforeAndAfterEach with HttpRequest {


  behavior of "dummyRequest"
  it should "return some String from MasterRoutePersona" in {
    import akka.http.scaladsl.client.RequestBuilding.Post
    val f: HttpResponse =  Await.result(Http().singleRequest(Post(uri + personaDummyRequest)), Duration.Inf)
    val string = Unmarshal(f).to[String]
    string map (s => assert(s.equals("ACAB: All Cannelloni Are Buoni")))
  }

  it should "return some String from MasterRouteTerminale" in {
    import akka.http.scaladsl.client.RequestBuilding.Post
    val f: HttpResponse =  Await.result(Http().singleRequest(Post(uri + terminalDummyRequest)), Duration.Inf)
    val string = Unmarshal(f).to[String]
    string map (s => assert(s.equals("ACAB: All Cannelloni Are Buoni")))
  }

  it should "return some String from MasterRouteTurni" in {
    import akka.http.scaladsl.client.RequestBuilding.Post
    val f: HttpResponse =  Await.result(Http().singleRequest(Post(uri + shiftDummyRequest)), Duration.Inf)
    val string = Unmarshal(f).to[String]
    string map (s => assert(s.equals("ACAB: All Cannelloni Are Buoni")))
  }

  it should "return some String from MasterRouteZona" in {
    import akka.http.scaladsl.client.RequestBuilding.Post
    val f: HttpResponse =  Await.result(Http().singleRequest(Post(uri + zoneDummyRequest)), Duration.Inf)
    val string = Unmarshal(f).to[String]
    string map (s => assert(s.equals("ACAB: All Cannelloni Are Buoni")))
  }

}
