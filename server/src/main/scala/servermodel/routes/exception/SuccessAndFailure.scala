package servermodel.routes.exception

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import caseclass.CaseClassHttpMessage.{Id, Response}
import jsonmessages.JsonFormats._

import scala.annotation.nowarn
import scala.util.{Failure, Success, Try}

object SuccessAndFailure {
  case class Error(int:Int) extends Throwable
  val ERRORE_GENERIC = 1
  def anotherSuccessAndFailure[A](result:Try[A]): StandardRoute =result match {
    case Success(None) => complete(Response[Option[Int]](StatusCodes.NotFound.intValue,None))
    case Success(Some(List())) =>    complete(Response[Option[Int]](StatusCodes.NotFound.intValue,None))
    case Success(Some(0)) =>    complete(StatusCodes.NotFound)
    case Success(Some(-1)) => complete((StatusCodes.BadRequest,Id(-1)))
    case Success(Some(-2)) => complete((StatusCodes.BadRequest,Id(-2)))
    case Success(Some(-3)) => complete((StatusCodes.BadRequest,Id(-3)))
    case t => failure(t.failed)
  }

  @nowarn
  private def failure(result:Try[Throwable]): StandardRoute =result match {
    case Failure(Error(ERRORE_GENERIC)) => complete(StatusCodes.InternalServerError)
  }
}
