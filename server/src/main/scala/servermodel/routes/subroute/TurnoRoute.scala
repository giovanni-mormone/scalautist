package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.Turno
import messagecodes.{StatusCodes=>statusCodes}
import caseclass.CaseClassHttpMessage.{ Request, Response}
import servermodel.routes.exception.RouteException
import dbfactory.operation.TurnoOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object TurnoRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def getTurno: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TurnoOperation.select(id)) {
          case Success(Some(turno)) => complete(Response(statusCodes.SUCCES_CODE, Some(turno)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def getAllTurno: Route =
    post {
      onComplete(TurnoOperation.selectAll) {
        case Success(Some(turni)) =>  complete(Response(statusCodes.SUCCES_CODE,Some(turni)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createTurno(): Route =
    post {
      entity(as[Request[Turno]]) {
        case Request(Some(turno))=> onComplete(TurnoOperation.insert(turno)) {
          case Success(Some(idTurno))  =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,
            Some(Turno(turno.nomeTurno,turno.fasciaOraria,turno.paga,Some(idTurno)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def createAllTurno(): Route =
    post {
      entity(as[Request[List[Turno]]]) {
        case Request(Some(turno))=>onComplete(TurnoOperation.insertAll(turno)) {
          case Success(Some(id))  =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def deleteTurno(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TurnoOperation.delete(id)) {
          case Success(Some(statusCodes.SUCCES_CODE)) =>  complete(Response[Int](statusCodes.SUCCES_CODE))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def deleteAllTurno(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(TurnoOperation.deleteAll(id)) {
          case Success(Some(result)) =>  complete(Response(statusCodes.SUCCES_CODE,Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def updateTurno(): Route =
    post {
      entity(as[Request[Turno]]) {
        case Request(Some(turno))=>onComplete(TurnoOperation.update(turno)) {
          case Success(None) =>complete(Response[Int](statusCodes.SUCCES_CODE))
          case Success(Some(id)) =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
}
