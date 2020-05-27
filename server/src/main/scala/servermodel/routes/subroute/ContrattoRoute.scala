package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Contratto
import jsonmessages.JsonFormats._

import scala.util.Success
import dbfactory.DummyDB
import servermodel.routes.exception.RouteException
//import db   //TODO

object ContrattoRoute {

  def getContratto(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(ContrattoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllContratto: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
      //onComplete(ContrattoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createContratto(): Route =
    post {
      entity(as[Contratto]) { contratto =>
        onComplete(DummyDB.dummyReq()) {
        //onComplete(ContrattoOperation.insert(contratto)) {
          case Success(t) =>  complete(StatusCodes.Created)
        }
      }
    }
}
