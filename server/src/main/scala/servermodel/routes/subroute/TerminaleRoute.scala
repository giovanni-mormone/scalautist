package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Terminale
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB
import dbfactory.operation.TerminaleOperation

import scala.util.Success

object TerminaleRoute  {

  def methodDummy(): Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        case t => complete(StatusCodes.Accepted,t)
      }
    }

  def getTerminale(id: Int): Route =
    get {
      onComplete(TerminaleOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllTerminale: Route =
    post {
      onComplete(TerminaleOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.insert(terminale)) {
          case Success(t) =>  complete(StatusCodes.Created,
                                      Terminale(terminale.nomeTerminale,terminale.idZona,Some(t)))
        }
      }
    }

  def createAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(TerminaleOperation.insertAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }

  def deleteTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.delete(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def deleteAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(TerminaleOperation.deleteAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def updateTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(TerminaleOperation.update(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }
}
