package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Turno
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB
import dbfactory.operation.TurnoOperation
import jsonmessages.JsonFormats._

import scala.util.Success

object TurnoRoute {

  def methodDummy(): Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        case t => complete(StatusCodes.Accepted,t)
      }
    }

  def getTurno(id: Int): Route =
    get {
      onComplete(TurnoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllTurno: Route =
    post {
      onComplete(TurnoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createTurno(): Route =
    post {
      entity(as[Turno]) { turno =>
        onComplete(TurnoOperation.insert(turno)) {
          case Success(t)  =>  complete(StatusCodes.Created,Turno(turno.nomeTurno,turno.fasciaOraria,Some(1)))
        }
      }
    }

  def createAllTurno(): Route =
    post {
      entity(as[List[Turno]]) { turno =>
        onComplete(TurnoOperation.insertAll(turno)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }

  def deleteTurno(): Route =
    post {
      entity(as[Turno]) { turno =>
        onComplete(TurnoOperation.delete(turno)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def deleteAllTurno(): Route =
    post {
      entity(as[List[Turno]]) { turno =>
        onComplete(TurnoOperation.deleteAll(turno)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def updateTurno(): Route =
    post {
      entity(as[Turno]) { turno =>
        onComplete(TurnoOperation.update(turno)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }
}
