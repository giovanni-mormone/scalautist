package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Contratto
import caseclass.CaseClassHttpMessage.{Id, Request, Response}
import jsonmessages.JsonFormats._

import scala.util.Success
import dbfactory.operation.ContrattoOperation
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

object ContrattoRoute {

  def getContratto: Route =
    post {
      entity(as[Request[Int]]){
        case Request(Some(id))=> onComplete(ContrattoOperation.select(id)) {
          case Success(contract) =>    complete(Response(StatusCodes.Found.intValue,contract))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }

    }

  def getAllContratto: Route =
    post {
      onComplete(ContrattoOperation.selectAll) {
        case Success(contracts) =>  complete(Response(StatusCodes.Found.intValue,contracts))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createContratto(): Route =
    post {
      entity(as[Request[Contratto]]) {
        case Request(Some(contratto))=>onComplete(ContrattoOperation.insert(contratto)) {
          case Success(id) =>  complete(Response(StatusCodes.Created.intValue,id))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def updateContratto(): Route =
    post {
      entity(as[Request[Contratto]]) {
        case Request(Some(contratto)) => onComplete(ContrattoOperation.update(contratto)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case Success(None) =>  complete(Response(StatusCodes.OK.intValue,Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
}
