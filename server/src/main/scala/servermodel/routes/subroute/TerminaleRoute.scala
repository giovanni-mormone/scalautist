package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Terminale
import caseclass.CaseClassHttpMessage.{Id, Request, Response}
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
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
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
          case Success(t) =>  complete(Response(StatusCodes.Created.intValue,
                                      Some(Terminale(terminal.nomeTerminale,terminal.idZona,t))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def createAllTerminale(): Route =
    post {
      entity(as[Request[List[Terminale]]]) {
        case Request(Some(terminal))=>onComplete(TerminaleOperation.insertAll(terminal)) {
          case Success(Some(id))  =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def deleteTerminale(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=> onComplete(TerminaleOperation.delete(id)) {
          case Success(Some(1)) =>  complete(Response(StatusCodes.OK.intValue,Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def deleteAllTerminale(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(TerminaleOperation.deleteAll(id)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.OK.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def updateTerminale(): Route =
    post {
      entity(as[Request[Terminale]]) {
        case Request(Some(terminal))=> onComplete(TerminaleOperation.update(terminal)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case Success(None)  =>  complete(Response(StatusCodes.OK.intValue,Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def getTerminaleByZona: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=>onComplete(TerminaleOperation.getTermininaliInZona(id)) {
          case Success(Some(terminale)) =>  complete(Response(StatusCodes.Found.intValue,Some(terminale)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
}
