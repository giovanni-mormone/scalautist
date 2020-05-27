package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.StoricoContratto
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB  //TODO

import scala.util.Success

object StoricoContrattoRoute  {

  def getStoricoContratto(id: Int): Route =
    get {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(StoricoContrattoOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        //case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllStoricoContratto: Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        //onComplete(StoricoContrattoOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createStoricoContratto(): Route =
    post {
      entity(as[StoricoContratto]) { storicoContratto =>
        onComplete(DummyDB.dummyReq()) {
          //onComplete(StoricoContrattoOperation.insert(storicoContratto)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }
}
