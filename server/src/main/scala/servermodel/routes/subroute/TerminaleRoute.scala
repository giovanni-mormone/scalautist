package servermodel.routes.subroute

import utils.{StatusCodes=>statusCodes}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.Terminale
import caseclass.CaseClassHttpMessage.{ Request, Response}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.TerminaleOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object TerminaleRoute  {

  def getTerminale: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=> onComplete(TerminaleOperation.select(id)) {
          case Success(Some(terminal)) => complete(Response(StatusCodes.Found.intValue, Some(terminal)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response[Terminale](StatusCodes.BadRequest.intValue, None))
      }
    }
  def getAllTerminale: Route =
    post {
      onComplete(TerminaleOperation.selectAll) {
        case Success(Some(terminals)) =>  complete(Response(StatusCodes.Found.intValue,Some(terminals)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createTerminale(): Route =
    post {
      entity(as[Request[Terminale]]) {
        case Request(Some(terminal))=>onComplete(TerminaleOperation.insert(terminal)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,
                                      Some(Terminale(terminal.nomeTerminale,terminal.idZona,Some(id)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def createAllTerminale(): Route =
    post {
      entity(as[Request[List[Terminale]]]) {
        case Request(Some(terminal))=>onComplete(TerminaleOperation.insertAll(terminal)) {
          case Success(Some(id))  =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteTerminale(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=> onComplete(TerminaleOperation.delete(id)) {
          case Success(Some(statusCodes.SUCCES_CODE)) =>  complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteAllTerminale(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(TerminaleOperation.deleteAll(id)) {
          case Success(Some(result)) =>  complete(Response(StatusCodes.OK.intValue,Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def updateTerminale(): Route =
    post {
      entity(as[Request[Terminale]]) {
        case Request(Some(terminal))=> onComplete(TerminaleOperation.update(terminal)) {
          case Success(None)  =>  complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
  def getTerminaleByZona: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TerminaleOperation.getTermininaliInZona(id)) {
          case Success(Some(terminale)) =>  complete(Response(StatusCodes.Found.intValue,Some(terminale)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ =>complete(Response[List[Terminale]](StatusCodes.BadRequest.intValue, None))
      }
    }
}