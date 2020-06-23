package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.{Dates, Request, Response}
import dbfactory.operation.DisponibilitaOperation
import jsonmessages.JsonFormats._
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object DisponibilitaRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)

  def getAvailability: Route =
    post {
      entity(as[Request[(Int, Dates)]]) {
        case Request(Some(available)) => onComplete(DisponibilitaOperation.getGiorniDisponibilita(available._1, available._2.date)){
          case Success(Some(days)) => complete((StatusCodes.OK, Response(statusCodes.SUCCES_CODE, Some(days))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }

  def getExtraAvailability: Route =
    post {
      entity(as[Request[(Int, Int, Int)]]) {
        case Request(Some(turnoInfo)) =>
          onComplete(DisponibilitaOperation.verifyIdRisultatoAndTerminalAndShift(turnoInfo._3, turnoInfo._1, turnoInfo._2)){
            case Success(Some(statusCodes.SUCCES_CODE)) => onComplete(DisponibilitaOperation.allDriverWithAvailabilityForADate(turnoInfo._3, turnoInfo._1, turnoInfo._2)){
              case Success(Some(info)) => complete(StatusCodes.OK, Response(statusCodes.SUCCES_CODE, Some(info)))
            }
            case t => anotherSuccessAndFailure(t)
          }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }

}
