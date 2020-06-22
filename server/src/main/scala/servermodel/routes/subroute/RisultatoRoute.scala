package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Risultato
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.RisultatoOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

/**
 * @author Francesco Cassano
 * RisultatoRoute is an object that manage methods that act on the persona entity
 */
object RisultatoRoute {

  def getRisultato: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(RisultatoOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllRisultato: Route =
    post {
      onComplete(RisultatoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createRisultato(): Route =
    post {
      entity(as[Risultato]) { risultato =>
        onComplete(RisultatoOperation.insert(risultato)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

}
