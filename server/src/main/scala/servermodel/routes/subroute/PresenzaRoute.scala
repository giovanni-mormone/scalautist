package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Presenza
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB //TODO


import scala.util.Success

object PresenzaRoute {
  def getPresenza(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(PresenzaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllPresenza: Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(PresenzaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createPresenza(): Route =
    post {
      entity(as[Presenza]) { presenza =>
        onComplete(DummyDB.dummyReq()) {
        //onComplete(PresenzaOperation.insert(presenza)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
