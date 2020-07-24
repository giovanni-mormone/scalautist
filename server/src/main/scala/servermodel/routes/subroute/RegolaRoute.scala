package servermodel.routes.subroute

import akka.http.scaladsl.server.Directives.{complete, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassHttpMessage.Response
import dbfactory.operation.RegolaOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import messagecodes.StatusCodes
import jsonmessages.JsonFormats._
import scala.util.Success

object RegolaRoute {

  def getRuleForGroup: Route =
    post {
      onComplete(RegolaOperation.regolaGruppo()) {
        case Success(Some(regola)) =>  complete(Response(StatusCodes.SUCCES_CODE, Some(regola)))
        case t => anotherSuccessAndFailure(t)
      }
    }
  def getRuleForWeek: Route =
    post {
      onComplete(RegolaOperation.regolaSettimana()) {
        case Success(Some(regola)) =>  complete(Response(StatusCodes.SUCCES_CODE, Some(regola)))
        case t => anotherSuccessAndFailure(t)
      }
    }
}
