package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Id
import dbfactory.operation.AssenzaOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure
import jsonmessages.JsonFormats._
import scala.util.Success

object AssenzaRoute {
  def addAbsence(): Route =  post{
    entity(as[Assenza]) {
      assenza => onComplete(AssenzaOperation.insert(assenza)){
        case Success(Some(value))  =>  complete((StatusCodes.Found,Id(value)))
        case t => anotherSuccessAndFailure(t)
      }
    }
  }
}
