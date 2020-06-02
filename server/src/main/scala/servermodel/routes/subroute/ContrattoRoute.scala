package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Contratto
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._

import scala.util.Success
import dbfactory.operation.ContrattoOperation
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

object ContrattoRoute {

  def getContratto: Route =
    post {
      entity(as[Id]){ id =>
        onComplete(ContrattoOperation.select(id.id)) {
          case Success(t) =>    complete((StatusCodes.Found,t))
          case t => anotherSuccessAndFailure(t)
        }
      }

    }

  def getAllContratto: Route =
    post {
      onComplete(ContrattoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createContratto(): Route =
    post {
      entity(as[Contratto]) { contratto =>
        onComplete(ContrattoOperation.insert(contratto)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
