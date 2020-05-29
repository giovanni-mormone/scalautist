package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Contratto
import jsonmessages.JsonFormats._

import scala.util.Success
import dbfactory.operation.ContrattoOperation
import servermodel.routes.exception.RouteException

object ContrattoRoute {

  def getContratto(id: Int): Route =
    get {
      onComplete(ContrattoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllContratto: Route =
    post {
      onComplete(ContrattoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createContratto(): Route =
    post {
      entity(as[Contratto]) { contratto =>
        onComplete(ContrattoOperation.insert(contratto)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
