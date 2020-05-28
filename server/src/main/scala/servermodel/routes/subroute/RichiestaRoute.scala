package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Richiesta
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB //TODO

import scala.util.Success

object RichiestaRoute {
  def getZona(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(RichiestaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllZona: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(RichiestaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createZona(): Route =
    post {
      entity(as[Richiesta]) { richiesta =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(RichiestaOperation.insert(richiesta)) {
          case Success(t)=>  complete(StatusCodes.Created)
        }
      }
    }
}
