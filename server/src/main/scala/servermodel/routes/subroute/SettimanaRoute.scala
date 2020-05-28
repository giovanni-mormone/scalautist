package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Settimana
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object SettimanaRoute  {

  def getSettimana(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(SettimanaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllSettimana: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(SettimanaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createSettimana(): Route =
    post {
      entity(as[Settimana]) { settimana =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(SettimanaOperation.insert(settimana)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
