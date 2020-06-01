package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Terminale
import caseclass.CaseClassHttpMessage.Id
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.TerminaleOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object TerminaleRoute  {

  def getTerminale: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(TerminaleOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllTerminale: Route =
    post {
      onComplete(TerminaleOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.insert(terminale)) {
          case Success(t) =>  complete(StatusCodes.Created,
                                      Terminale(terminale.nomeTerminale,terminale.idZona,Some(t)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def createAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(TerminaleOperation.insertAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.delete(terminale)) {
          case Success(t)  =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(TerminaleOperation.deleteAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updateTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.update(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
}
