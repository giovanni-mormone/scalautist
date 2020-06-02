package model

import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.annotation.nowarn
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success, Try}

abstract class AbstractModel extends Model{
  private val dispatcher: ModelDispatcher = ModelDispatcher()
  protected implicit val system: ActorSystem = dispatcher.system
  override def getURI(request: String): String = dispatcher.address + "/" + request

  override def success[A,B](function:Try[Option[A]])(implicit promise:Promise[Option[A]]): Unit =function match {
    case Success(value) => promise.success(value)
    case t => failure(t.failed)
  }

  @nowarn
  override def failure[A](function:Try[Throwable])(implicit promise:Promise[Option[A]]): Unit = function match {
    case Failure(exception) => promise.failure(exception)
  }

  override def doHttp(request: HttpRequest): Future[HttpResponse] = dispatcher.serverRequest(request)

  override def shutdownActorSystem(): Future[Terminated] =system.terminate()
}
