package model

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.annotation.nowarn
import scala.concurrent.{ Future, Promise}
import scala.util.{Failure, Success, Try}

abstract class AbstractModel extends Model{
  private val dispatcher: ModelDispatcher = ModelDispatcher()
  protected implicit val system: ActorSystem = dispatcher.system
  override def getURI(request: String): String = dispatcher.address + "/" + request

  override def success[A,B](function:Try[Option[A]],promise:Promise[Option[A]]): Unit =function match {
    case Success(Some(List())) => promise.success(None)
    case Success(Some(value)) => promise.success(Some(value))
    case t => failure(t.failed,promise)
  }

  @nowarn
  override def failure[A](function:Try[Throwable], promise:Promise[Option[A]]): Unit = function match {
    case Failure(exception) => promise.failure(exception)
  }

  override def doHttp(request: HttpRequest): Future[HttpResponse] = dispatcher.serverRequest(request)

  override def shutdownActorSystem(): Future[Terminated] =system.terminate()
}
