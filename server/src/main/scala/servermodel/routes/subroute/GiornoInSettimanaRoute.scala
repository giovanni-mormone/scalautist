package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.GiornoInSettimana
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.GiornoInSettimanaOperation
import servermodel.routes.exception.RouteException
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object GiornoInSettimanaRoute {
  def getGiornoInSettimana: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(GiornoInSettimanaOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllGiornoInSettimana: Route =
    post {
      onComplete(GiornoInSettimanaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createGiornoInSettimana(): Route =
    post {
      entity(as[GiornoInSettimana]) { giornoInSettimana =>
        onComplete(GiornoInSettimanaOperation.insert(giornoInSettimana)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
