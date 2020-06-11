package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Contratto
import caseclass.CaseClassHttpMessage.{ Request, Response}
import jsonmessages.JsonFormats._
import utils.{StatusCodes=>statusCodes}
import scala.util.Success
import dbfactory.operation.ContrattoOperation
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

object ContrattoRoute {

  def getContratto: Route =
    post {
      entity(as[Request[Int]]){
        case Request(Some(id))=> onComplete(ContrattoOperation.select(id)) {
          case Success(Some(contract)) =>    complete(Response(StatusCodes.Found.intValue,Some(contract)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }

    }

  def getAllContratto: Route =
    post {
      onComplete(ContrattoOperation.selectAll) {
        case Success(Some(contracts)) =>  complete(Response(StatusCodes.Found.intValue,Some(contracts)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createContratto(): Route =
    post {
      entity(as[Request[Contratto]]) {
        case Request(Some(contratto))=>onComplete(ContrattoOperation.insert(contratto)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(Contratto(contratto.tipoContratto,contratto.turnoFisso,contratto.ruolo,Some(id)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
  def updateContratto(): Route =
    post {
      entity(as[Request[Contratto]]) {
        case Request(Some(contratto)) => onComplete(ContrattoOperation.update(contratto)) {
          case Success(None) =>  complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case Success(Some(id)) if id>0 =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)

        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
}
