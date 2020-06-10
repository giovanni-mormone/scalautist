package servermodel.routes.subroute


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.{Assenza, Login, Persona}
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword, Dates, Id, Request, Response}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.{AssenzaOperation, PersonaOperation, StipendioOperation}
import servermodel.routes.exception.SuccessAndFailure._

import scala.util.Success

/**
 * @author Francesco Cassano
 * PersonaRoute is an object that manage methods that act on the persona entity
 */
object PersonaRoute{
  def getPersona: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.select(value)) {
          case Success(Some(person)) => complete(Response(StatusCodes.OK.intValue, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def getAllPersona: Route =
    post {
      onComplete(PersonaOperation.selectAll) {
        case Success(persons) =>  complete(Response(StatusCodes.OK.intValue, Some(persons)))
        case t => anotherSuccessAndFailure(t)
      }
    }
  def hirePerson: Route =
    post {
      entity(as[Request[Assumi]]) {
        case Request(Some(value)) =>
        onComplete(PersonaOperation.assumi(value)) {
          case Success(Some(login)) =>  complete(Response(StatusCodes.Created.intValue, Some(login)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def deletePersona(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.delete(value)) {
          case Success(Some(1)) => complete(Response(StatusCodes.OK.intValue, Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.deleteAll(value)) {
          case Success(Some(id>0)) =>  complete(Response(StatusCodes.OK.intValue, Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Request[Persona]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.update(value)) {
          case Success(Some(t)) =>  complete(Response(StatusCodes.Created.intValue, Some(Id(t))))
          case Success(None) =>complete(Response(StatusCodes.OK.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Request[Login]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.login(value)) {
          case Success(Some(person))  =>  complete(Response(StatusCodes.OK.intValue, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def recoveryPassword(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.recoveryPassword(value)){
          case Success(login)  =>  complete(Response(StatusCodes.OK.intValue, Some(login)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def changePassword(): Route =
    post {
      entity(as[Request[ChangePassword]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.changePassword(value)){
          case Success(Some(1))  =>  complete(Response(StatusCodes.OK.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }

  def getStipendio: Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.getstipendiForPersona(value)){
          case Success(Some(salary))  =>  complete(Response(StatusCodes.OK.intValue, Some(salary)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def salaryCalculus(): Route =
    post{
      entity(as[Request[Dates]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.calculateStipendi(value.date)){
          case Success(Some(1))  =>  complete(Response(StatusCodes.Created.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
  def addAbsence(): Route =
    post {
      entity(as[Request[Assenza]]){
        case Request(Some(value)) => onComplete(AssenzaOperation.insert(value)){
          case Success(Some(1)) => complete(Response(StatusCodes.Created.intValue, Some(Id(1))))
          case t =>anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(1)))
      }
    }
}
