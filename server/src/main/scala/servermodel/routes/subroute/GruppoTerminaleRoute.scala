package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.GruppoTerminale
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.GruppoTerminaleOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object GruppoTerminaleRoute {
  def getGruppoTerminale: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(GruppoTerminaleOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getAllGruppoTerminale: Route =
    post {
      onComplete(GruppoTerminaleOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createGruppoTerminale(): Route =
    post {
      entity(as[GruppoTerminale]) { gruppoTerminale =>
        onComplete(GruppoTerminaleOperation.insert(gruppoTerminale)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
