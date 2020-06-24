package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, post}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.{Dates, Request, Response}
import dbfactory.operation.AssenzaOperation
import jsonmessages.JsonFormats._
import messagecodes.{StatusCodes => statusCodes}
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success

object AssenzaRoute{
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
  def holidayByPerson(): Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(AssenzaOperation.getAllFerie(value)){
          case Success(Some(ferie))  =>  complete(StatusCodes.OK,Response(statusCodes.SUCCES_CODE, Some(ferie)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def absenceInYearForPerson(): Route =
    post{
      entity(as[Request[(Int,Int)]]) {
        case Request(Some(value)) =>onComplete(AssenzaOperation.getAssenzeInYearForPerson(value._2,value._1)){
          case Success(Some(ferie))  =>  complete(StatusCodes.OK,Response(statusCodes.SUCCES_CODE, Some(ferie)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def absencesOnDay(): Route =
    post{
      entity(as[Request[Dates]]) {
        case Request(Some(date)) => onComplete(AssenzaOperation.getAllAbsence(date.date)){
          case Success(Some(absents)) => complete(StatusCodes.OK, Response(statusCodes.SUCCES_CODE, Some(absents)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest, badHttpRequest)
      }
    }
}
