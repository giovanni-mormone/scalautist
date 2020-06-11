package model


import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import utils.Execution

import scala.annotation.nowarn
import scala.concurrent.{ExecutionContextExecutor, Future, Promise}
import scala.util.{Failure, Success, Try}

abstract class AbstractModel extends Model{
  private val dispatcher: ModelDispatcher = ModelDispatcher()
  protected implicit val system: ActorSystem = dispatcher.system
  override def getURI(request: String): String = dispatcher.address + "/" + request
  protected def transform[A](value:A): Request[A] =Request(Some(value))
  implicit protected val execution: ExecutionContextExecutor = Execution.executionContext


  //=================================ATTENZIONEE==========================================================||
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
  //=================================ATTENZIONEE==========================================================||


  def unMarshall(v1: Option[HttpResponse]): Future[Response[Int]] = Unmarshal(v1).to[Response[Int]]


  private val found: PartialFunction[HttpResponse, Option[HttpResponse]] = new PartialFunction[HttpResponse, Option[HttpResponse]] {
    override def isDefinedAt(x: HttpResponse): Boolean = x.status==StatusCodes.OK || x.status==StatusCodes.Created || x.status==StatusCodes.NotFound || x.status==StatusCodes.BadRequest
    override def apply(response: HttpResponse): Option[HttpResponse] = Some(response)
  }

  def callHtpp(request: HttpRequest):Future[Option[HttpResponse]] =
    doHttp(request).collect(found)

  override def doHttp(request: HttpRequest): Future[HttpResponse] = dispatcher.serverRequest(request)

  override def shutdownActorSystem(): Future[Terminated] =system.terminate()
}
