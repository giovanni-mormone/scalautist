package servermodel.routes.exception

import akka.http.scaladsl.model.StatusCodes
import utils.{StatusCodes=>statusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import caseclass.CaseClassHttpMessage.Response
import jsonmessages.JsonFormats._

import scala.annotation.nowarn
import scala.util.{Failure, Success, Try}

object SuccessAndFailure {
  def anotherSuccessAndFailure[A](result:Try[A]): StandardRoute =result match {
    case Success(None) => complete(Response[Int](StatusCodes.NotFound.intValue,None))
    case Success(Some(List())) =>    complete(Response[Int](StatusCodes.NotFound.intValue,None))
    case Success(Some(statusCodes.NOT_FOUND)) =>    complete(Response[Int](StatusCodes.NotFound.intValue,None))
    case Success(Some(statusCodes.ERROR_CODE1)) => complete(Response(StatusCodes.BadRequest.intValue,Some(statusCodes.ERROR_CODE1)))
    case Success(Some(statusCodes.ERROR_CODE2)) => complete(Response(StatusCodes.BadRequest.intValue,Some(statusCodes.ERROR_CODE2)))
    case Success(Some(statusCodes.ERROR_CODE3)) => complete(Response(StatusCodes.BadRequest.intValue,Some(statusCodes.ERROR_CODE3)))
    case t => failure(t.failed)
  }

  @nowarn
  private def failure(result:Try[Throwable]): StandardRoute =result match {
    case Failure(_) => complete(Response[Int](StatusCodes.InternalServerError.intValue,None))
  }
}
