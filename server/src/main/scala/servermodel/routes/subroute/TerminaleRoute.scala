package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Terminale
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object TerminaleRoute  {

  def dummy() =
    onComplete(DummyDB.dummyReq()) {
      case t => complete(StatusCodes.Accepted,t)
    }

  def methodDummy(): Route =
    post {
      dummy()
    }

  def getTerminale(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(TerminaleOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllTerminale: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(TerminaleOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(TerminaleOperation.insert(terminale)) {
          case Success(t) =>  complete(StatusCodes.Created,
                                      Terminale(terminale.nomeTerminale,terminale.idZona,Some(2/*t*/)))//TODO
        }
      }
    }

  def createAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(TerminaleOperation.insertAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }

  def deleteTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(TerminaleOperation.delete(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def deleteAllTerminale(): Route =
    post {
      entity(as[List[Terminale]]) { terminale =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(TerminaleOperation.deleteAll(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }

  def updateTerminale(): Route =
    post {
      entity(as[Terminale]) { terminale =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(TerminaleOperation.update(terminale)) {
          case Success(t)  =>  complete(StatusCodes.OK)
        }
      }
    }
}
