package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Straordinario
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.StraordinarioOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object StraordinarioRoute  {

  def getStraordinario: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(StraordinarioOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllStraordinario: Route =
    post {
      onComplete(StraordinarioOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
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
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
