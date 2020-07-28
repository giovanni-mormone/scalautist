package model


import java.sql.Date
import java.util.Calendar

import akka.actor.{ActorSystem, Terminated}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import utils.Execution

import scala.concurrent.{ExecutionContextExecutor, Future}

abstract class AbstractModel extends Model{

  private val dispatcher: ModelDispatcher =ModelDispatcher()
  protected implicit val system: ActorSystem = dispatcher.system
  override def getURI(request: String): String = dispatcher.address + "/" + request
  protected def transform[A](value:A): Request[A] =Request(Some(value))
  implicit protected val execution: ExecutionContextExecutor = Execution.executionContext


  protected def unMarshall(v1: Option[HttpResponse]): Future[Response[Int]] = Unmarshal(v1).to[Response[Int]]


  private val found: PartialFunction[HttpResponse, Option[HttpResponse]] = new PartialFunction[HttpResponse, Option[HttpResponse]] {
    override def isDefinedAt(x: HttpResponse): Boolean = (x.status==StatusCodes.OK || x.status==StatusCodes.Created || x.status==StatusCodes.NotFound || x.status==StatusCodes.BadRequest
    || x.status==StatusCodes.InternalServerError || x.status==StatusCodes.ServiceUnavailable || x.status==StatusCodes.EnhanceYourCalm)
    override def apply(response: HttpResponse): Option[HttpResponse] = Some(response)
  }

  protected def callHttp(request: HttpRequest):Future[Option[HttpResponse]] =
    doHttp(request).collect(found)

  override def doHttp(request: HttpRequest): Future[HttpResponse] = dispatcher.serverRequest(request)

  ///////////////GET YEAR DA MUOVERE?

  protected def getCalendar:Calendar={
    val year = Calendar.getInstance()
    year.setTime(new Date(System.currentTimeMillis()))
    year
  }

  protected def callRequest(request:HttpRequest):Future[Response[Int]] =
    callHttp(request).flatMap(unMarshall)
  protected def getYear:Int=getCalendar.get(Calendar.YEAR)
}
