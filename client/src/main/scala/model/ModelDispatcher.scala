package model

import akka.actor.ActorSystem
import akka.dispatch.Dispatchers
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.SystemMaterializer

import scala.concurrent.Future

/**
 * @author Francesco Cassano
 * Generic definition of model dispatcher. It contains the server's address and the actor system information.
 */
trait ModelDispatcher {
  private val port = 8080 //util
  val address: String = "http://localhost:" + port

  private val actorSystemName: String = "ClientSystem"

  implicit val system: ActorSystem = ActorSystem(actorSystemName)
  implicit val materializer: SystemMaterializer = SystemMaterializer(system)
  implicit val ex: Dispatchers = system.dispatchers
  /**
   * Http server request
   * @param request: Http request to send to server
   * @return future of server response
   */
  def serverRequest(request:HttpRequest):Future[HttpResponse]
}

/**
 * Companion object of [[model.ModelDispatcher]]. It contains Akka implementation.
 */
object ModelDispatcher {

  private val istance: ModelDispatcher = new ModelDispatcherAkka

  def apply(): ModelDispatcher = istance

  private class ModelDispatcherAkka extends ModelDispatcher{

    override def serverRequest(request: HttpRequest): Future[HttpResponse] =
      Http().singleRequest(request)
  }
}
