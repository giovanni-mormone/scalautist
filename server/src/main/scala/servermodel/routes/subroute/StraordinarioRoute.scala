package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Straordinario
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.StraordinarioOperation

import scala.util.Success

object StraordinarioRoute  {

  def getStraordinario(id: Int): Route =
    get {
      onComplete(StraordinarioOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllStraordinario: Route =
    post {
      onComplete(StraordinarioOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createStraordinario(): Route =
    post {
      entity(as[Straordinario]) { straordinario =>
        onComplete(StraordinarioOperation.insert(straordinario)) {
          case Success(t) =>  complete(StatusCodes.Created,Straordinario(
                                                        straordinario.data,
                                                        straordinario.personaId,
                                                        straordinario.turnoId,
                                                        Some(2/*t*/)))//TODO
        }
      }
    }
}
