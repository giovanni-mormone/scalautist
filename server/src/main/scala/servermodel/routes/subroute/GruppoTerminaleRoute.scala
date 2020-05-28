package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.GruppoTerminale
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object GruppoTerminaleRoute {
  def getGruppoTerminale(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GruppoTerminaleOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllGruppoTerminale: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(GruppoTerminaleOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createGruppoTerminale(): Route =
    post {
      entity(as[GruppoTerminale]) { gruppoTerminale =>
        onComplete(DummyDB.dummyReq()) {
        //onComplete(GruppoTerminaleOperation.insert(gruppoTerminale)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }
}
