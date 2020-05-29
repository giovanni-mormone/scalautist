package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Risultato
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.RisultatoOperation

import scala.util.Success

object RisultatoRoute {

  def getRisultato(id: Int): Route =
    get {
      onComplete(RisultatoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllRisultato: Route =
    get {
      onComplete(RisultatoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createRisultato(): Route =
    post {
      entity(as[Risultato]) { risultato =>
        onComplete(RisultatoOperation.insert(risultato)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
