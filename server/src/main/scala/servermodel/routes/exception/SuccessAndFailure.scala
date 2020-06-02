package servermodel.routes.exception

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute

import scala.annotation.nowarn
import scala.util.{Failure, Success, Try}

object SuccessAndFailure {
  case object Error extends Throwable
  def anotherSuccessAndFailure[A](result:Try[A]): StandardRoute =result match {
    case Success(None) => complete(StatusCodes.NotFound)
    case Success(_) =>    complete(StatusCodes.NotFound)
    case t => failure(t)
  }
  @nowarn
  private def failure[A](result:Try[A]): StandardRoute =result match {
    case Failure(Error) => complete(StatusCodes.InternalServerError)
  }
}
