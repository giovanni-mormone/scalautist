package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.{AssignRichiestaTeorica, Id, Request, Response}
import dbfactory.operation.RichiestaTeoricaOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import messagecodes.{StatusCodes => statusCodes}

import scala.util.Success

/**
 * @author Francesco Cassano
 * RichiestaTeoricaRoute is an object that manage methods that act on the RichiestaTeorica entity
 */
object RichiestaTeoricaRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)

  def getRichiestaTeorica: Route =
    post {
      entity(as[Id]) { id =>
        onComplete(RichiestaTeoricaOperation.select(id.id)) {
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllRichiestaTeorica: Route =
    post {
      onComplete(RichiestaTeoricaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def saveRichiestaTeorica(): Route =
    post {
      entity(as[Request[AssignRichiestaTeorica]]) {
        case Request(Some(theoReq)) => complete(StatusCodes.BadRequest, badHttpRequest)
          /*onComplete(RichiestaTeoricaOperation.saveRichiestaTeorica(theoReq.request, theoReq.days)){
            case Success(Some(statusCodes.SUCCES_CODE)) => complete(Response[Int](statusCodes.SUCCES_CODE))
            case other => anotherSuccessAndFailure(other)
          }*/
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }
}