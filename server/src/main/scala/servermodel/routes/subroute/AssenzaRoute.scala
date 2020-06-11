package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.{Request, Response}
import dbfactory.operation.AssenzaOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import utils.{StatusCodes=>statusCodes}
import scala.util.Success

object AssenzaRoute {
  def addAbsence(): Route =
    post {
      entity(as[Request[Assenza]]){
        case Request(Some(value)) => onComplete(AssenzaOperation.insert(value)){
          case Success(Some(id)) if id!=0 && id>0=> complete(Response(StatusCodes.Created.intValue, Some(id)))
          case t =>anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
}
