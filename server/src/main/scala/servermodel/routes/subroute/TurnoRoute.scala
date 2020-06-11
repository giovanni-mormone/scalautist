package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.Turno
import utils.{StatusCodes=>statusCodes}
import caseclass.CaseClassHttpMessage.{ Request, Response}
import servermodel.routes.exception.RouteException
import dbfactory.operation.TurnoOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object TurnoRoute {

  def getTurno: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TurnoOperation.select(id)) {
          case Success(Some(turno)) => complete(Response(StatusCodes.Found.intValue, Some(turno)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def getAllTurno: Route =
    post {
      onComplete(TurnoOperation.selectAll) {
        case Success(Some(turni)) =>  complete(Response(StatusCodes.Found.intValue,Some(turni)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createTurno(): Route =
    post {
      entity(as[Request[Turno]]) {
        case Request(Some(turno))=> onComplete(TurnoOperation.insert(turno)) {
          case Success(Some(idTurno))  =>  complete(Response(StatusCodes.Created.intValue,Some(Turno(turno.nomeTurno,turno.fasciaOraria,turno.notturno,Some(idTurno)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def createAllTurno(): Route =
    post {
      entity(as[Request[List[Turno]]]) {
        case Request(Some(turno))=>onComplete(TurnoOperation.insertAll(turno)) {
          case Success(Some(id))  =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteTurno(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TurnoOperation.delete(id)) {
          case Success(Some(statusCodes.SUCCES_CODE)) =>  complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteAllTurno(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(TurnoOperation.deleteAll(id)) {
          case Success(Some(result)) =>  complete(Response(StatusCodes.OK.intValue,Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def updateTurno(): Route =
    post {
      entity(as[Request[Turno]]) {
        case Request(Some(turno))=>onComplete(TurnoOperation.update(turno)) {
          case Success(None) =>complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
}
