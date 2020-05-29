package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.RichiestaTeorica
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.RichiestaTeoricaOperation

import scala.util.Success

object RichiestaTeoricaRoute {

  def getZona(id: Int): Route =
    get {
      onComplete(RichiestaTeoricaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllZona: Route =
    post {
      onComplete(RichiestaTeoricaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createZona(): Route =
    post {
      entity(as[RichiestaTeorica]) { richiestaTeorica =>
        onComplete(RichiestaTeoricaOperation.insert(richiestaTeorica)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}