package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Richiesta
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.RichiestaOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object RichiestaRoute {

  def getRichiesta: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(RichiestaOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllRichiesta: Route =
    post {
      onComplete(RichiestaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createRichiesta(): Route =
    post {
      entity(as[Richiesta]) { richiesta =>
        onComplete(RichiestaOperation.insert(richiesta)) {
          case Success(t)=>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
