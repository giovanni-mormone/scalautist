package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.StoricoContratto
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.StoricoContrattoOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object StoricoContrattoRoute  {

  def getStoricoContratto: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(StoricoContrattoOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllStoricoContratto: Route =
    post {
      onComplete(StoricoContrattoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createStoricoContratto(): Route =
    post {
      entity(as[StoricoContratto]) { storicoContratto =>
        onComplete(StoricoContrattoOperation.insert(storicoContratto)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
