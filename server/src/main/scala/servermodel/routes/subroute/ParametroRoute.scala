package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.Parametro
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object ParametroRoute {

  def getParametro(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(ParametroOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllParametro: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(ParametroOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createParametro(): Route =
    post {
      entity(as[Parametro]) { parametro =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(ParametroOperation.insert(parametro)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }
}
