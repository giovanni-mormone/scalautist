package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.{Request, Response}
import dbfactory.operation.AssenzaOperation
import jsonmessages.JsonFormats._
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import messagecodes.{StatusCodes=>statusCodes}

import scala.util.Success

object AssenzaRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def addAbsence(): Route =
    post {
      entity(as[Request[Assenza]]){
        case Request(Some(value)) => onComplete(AssenzaOperation.insert(value)){
          case Success(Some(id)) if id!=0 && id>0=> complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(id)))
          case t =>anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
}
