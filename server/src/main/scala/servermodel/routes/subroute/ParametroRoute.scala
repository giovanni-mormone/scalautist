package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Parametro
import caseclass.CaseClassHttpMessage.{InfoAlgorithm, Request, Response}
import dbfactory.operation.ParametroOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object ParametroRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)

  def getAllOldParameters: Route =
    post {
      onComplete(ParametroOperation.selectAll) {
        case Success(Some(parameter)) => complete(Response[List[Parametro]](statusCodes.SUCCES_CODE,Some(parameter)))
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
            case Success(Some(statusCodes.SUCCES_CODE))=> complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }
}
