package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.{Request, Response}
import dbfactory.operation.DisponibilitaOperation
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object DisponibilitaRoute {

  def getAvailability: Route =
    post {
      entity(as[Request[(Int, Int)]]) {
        case Request(Some(available)) => onComplete(DisponibilitaOperation.getDisponibilita(available._1, available._2)){ //todo metodo nuovo
          case Success(days) => complete(StatusCodes.OK, Response(statusCodes.SUCCES_CODE, Some(days)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest)
      }
    }

  def getExtraAvailability: Route =
    post {
      entity(as[Request[(Int, Int, Int)]]) {
        case Request(Some(turnoInfo)) =>
          onComplete(DisponibilitaOperation.verifyIdRisultatoAndTerminalAndShift(turnoInfo._3, turnoInfo._1, turnoInfo._2)){
            case Success(statusCodes.SUCCES_CODE) => DisponibilitaOperation.allDriverWithAvailabilityForADate(turnoInfo._3, turnoInfo._1, turnoInfo._2).onComplete{
              case Success(Some(info)) => complete(StatusCodes.OK, Response(statusCodes.SUCCES_CODE, Some(info)))
            }
            case t => anotherSuccessAndFailure(t)
          }
        case _ => complete(StatusCodes.BadRequest)
      }
    }

}
