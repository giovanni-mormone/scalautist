package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Giorno
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.GiornoOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object GiornoRoute {
  def getGiorno: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(GiornoOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getAllGiorno: Route =
    post {
      onComplete(GiornoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createGiorno(): Route =
    post {
      entity(as[Giorno]) { giorno =>
        onComplete(GiornoOperation.insert(giorno)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
