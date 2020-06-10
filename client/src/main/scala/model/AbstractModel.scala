package model

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.annotation.nowarn
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

abstract class AbstractModel extends Model{
  private val dispatcher: ModelDispatcher = ModelDispatcher()
  protected implicit val system: ActorSystem = dispatcher.system
  override def getURI(request: String): String = dispatcher.address + "/" + request

  override def success[A,B](function:Try[Option[A]],promise:Promise[Option[A]]): Unit =function match {
    case Success(Some(List())) => promise.success(None)
    case Success(Some(value)) => promise.success(Some(value))
    case Success(_) => promise.success(None)
    case t => failure(t.failed,promise)
  }

  @nowarn
  override def failure[A](function:Try[Throwable], promise:Promise[Option[A]]): Unit = function match {
    case Failure(_) => promise.success(None)
  }
  private val found: PartialFunction[HttpResponse, Option[HttpResponse]] = new PartialFunction[HttpResponse, Option[HttpResponse]] {
    override def isDefinedAt(x: HttpResponse): Boolean = x.status!=StatusCodes.NotFound
    override def apply(v1: HttpResponse): Option[HttpResponse] = Some(v1)
  }
  private val notFound: PartialFunction[HttpResponse, Option[HttpResponse]] = new PartialFunction[HttpResponse, Option[HttpResponse]] {
    override def isDefinedAt(x: HttpResponse): Boolean = x.status==StatusCodes.NotFound || x.status==StatusCodes.InternalServerError
    override def apply(v1: HttpResponse): Option[HttpResponse] = None
  }
  def callHtpp(request: HttpRequest):Future[Option[HttpResponse]] =
    doHttp(request).collect{found orElse notFound}

  override def doHttp(request: HttpRequest): Future[HttpResponse] = dispatcher.serverRequest(request)

  override def shutdownActorSystem(): Future[Terminated] =system.terminate()
}
