package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Giorno
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object GiornoRoute {
  def getGiorno(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GiornoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllGiorno: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GiornoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createGiorno(): Route =
    post {
      entity(as[Giorno]) { giorno =>
        onComplete(DummyDB.dummyReq()) {
        //onComplete(GiornoOperation.insert(giorno)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
