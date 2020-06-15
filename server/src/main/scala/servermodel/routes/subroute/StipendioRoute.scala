package servermodel.routes.subroute

import java.sql.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.{Dates, Request, Response}
import dbfactory.operation.StipendioOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import messagecodes.{StatusCodes => statusCodes}

import scala.util.Success
import jsonmessages.JsonFormats._

object StipendioRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def getStipendio: Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.getstipendiForPersona(value)){
          case Success(Some(salary))  =>  complete(Response(statusCodes.SUCCES_CODE, Some(salary)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
  def salaryCalculus(): Route =
    post{
      entity(as[Request[Dates]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.calculateStipendi(value.date)){
          case Success(Some(id)) if id>0 =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def infoStipendio: Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.getStipendioInformations(value)){
          case Success(Some(stipendio))  =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(stipendio)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
}
