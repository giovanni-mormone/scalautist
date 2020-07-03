package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Parametro
import caseclass.CaseClassHttpMessage.{Dates, Id, InfoAlgorithm, Request, Response}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.ParametroOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import servermodel.routes.subroute.RisultatoRoute.badHttpRequest
import scala.concurrent.Future
import scala.util.Success
import messagecodes.{StatusCodes => statusCodes}

object ParametroRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def getParametro: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(ParametroOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def createParametro(): Route =
    post {
      entity(as[Parametro]) { parametro =>
        onComplete(ParametroOperation.insert(parametro)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getAllOldParameters: Route = // TODO
    post {
      onComplete(ParametroOperation.selectAll) {
        case Success(_) => complete(Response[Int](statusCodes.SUCCES_CODE))
        case other => anotherSuccessAndFailure(other)
      }
    }

  def getParametersById: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(idParameter)) => onComplete(ParametroOperation.getParameter(idParameter)) {
            case Success(Some(value)) => complete(Response[InfoAlgorithm](statusCodes.SUCCES_CODE,Some(value)))
            case other => anotherSuccessAndFailure(other)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }

  def saveParameters: Route =
    post {
      entity(as[Request[InfoAlgorithm]]) {
        case Request(Some(infoParameter)) => onComplete(ParametroOperation.saveInfoAlgorithm(infoParameter)) {
            case Success(_) => complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }
}
