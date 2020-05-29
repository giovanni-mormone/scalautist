package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Zona
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB
import dbfactory.operation.ZonaOperation

import scala.util.Success

object ZonaRoute {

  def methodDummy(): Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        case t => complete(StatusCodes.Accepted,t)
      }
    }

  def getZona(id: Int): Route =
    get {
     onComplete(ZonaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllZona: Route =
    post {
      onComplete(ZonaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        //case Success(Nil) => complete(StatusCodes.NotFound)
      }
    }

  def createZona(): Route =
    post {
      entity(as[Zona]) { zona =>
        onComplete(ZonaOperation.insert(zona)) {
          case Success(t) =>  complete(StatusCodes.Created,Zona(zona.zones,Some(2/*t*/))) //TODO
        }
      }
    }

  def createAllZona(): Route =
    post {
      entity(as[List[Zona]]) { zona =>
        onComplete(ZonaOperation.insertAll(zona)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }

  def deleteZona(): Route =
    post {
      entity(as[Zona]) { zona =>
        onComplete(ZonaOperation.delete(zona)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def deleteAllZona(): Route =
    post {
      entity(as[List[Zona]]) { zona =>
        onComplete(ZonaOperation.deleteAll(zona)) {
          case Success(t) =>  complete(StatusCodes.OK)
        }
      }
    }

  def updateZona(): Route =
    post {
      entity(as[Zona]) { zona =>
        onComplete(ZonaOperation.update(zona)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
