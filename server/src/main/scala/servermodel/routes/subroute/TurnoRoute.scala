package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Turno
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.TurnoOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object TurnoRoute {

  def getTurno: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(TurnoOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getAllTurno: Route =
    post {
      onComplete(TurnoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createTurno(): Route =
    post {
      entity(as[Turno]) { turno =>
        onComplete(TurnoOperation.insert(turno)) {
          //case Success(t)  =>  complete(StatusCodes.Created,Turno(turno.nomeTurno,turno.fasciaOraria,Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def createAllTurno(): Route =
    post {
      entity(as[List[Turno]]) { turno =>
        onComplete(TurnoOperation.insertAll(turno)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteTurno(): Route =
    post {
      entity(as[Id]) { turno =>
        onComplete(TurnoOperation.delete(turno.id)) {
          case Success(Some(1)) =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteAllTurno(): Route =
    post {
      entity(as[List[Id]]) { turno =>
        onComplete(TurnoOperation.deleteAll(turno.map(_.id))) {
          case Success(Some(_)) =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updateTurno(): Route =
    post {
      entity(as[Turno]) { turno =>
        onComplete(TurnoOperation.update(turno)) {
          case Success(Some(t)) =>  complete((StatusCodes.Created,Id(t)))
          case Success(None) =>complete(StatusCodes.OK)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
