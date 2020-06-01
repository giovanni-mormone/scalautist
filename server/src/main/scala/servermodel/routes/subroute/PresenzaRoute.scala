package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Presenza
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.PresenzaOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object PresenzaRoute {

  def getPresenza: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(PresenzaOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getAllPresenza: Route =
    get {
      onComplete(PresenzaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createPresenza(): Route =
    post {
      entity(as[Presenza]) { presenza =>
        onComplete(PresenzaOperation.insert(presenza)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
