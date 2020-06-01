package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.RichiestaTeorica
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.RichiestaTeoricaOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object RichiestaTeoricaRoute {

  def getRichiestaTeorica: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(RichiestaTeoricaOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllRichiestaTeorica: Route =
    post {
      onComplete(RichiestaTeoricaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createRichiestaTeorica(): Route =
    post {
      entity(as[RichiestaTeorica]) { richiestaTeorica =>
        onComplete(RichiestaTeoricaOperation.insert(richiestaTeorica)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}