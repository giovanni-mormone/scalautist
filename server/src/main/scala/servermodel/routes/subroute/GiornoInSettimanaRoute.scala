package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.GiornoInSettimana
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB //TODO

import scala.util.Success

object GiornoInSettimanaRoute {
  def getGiornoInSettimana(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GiornoInSettimanaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllGiornoInSettimana: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GiornoInSettimanaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createGiornoInSettimana(): Route =
    post {
      //entity(as[GiornoInSettimana]) { giornoInSettimana =>
        onComplete(DummyDB.dummyReq()) {
        //onComplete(GiornoInSettimanaOperation.insert(giornoInSettimana)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      //}
    }
}
