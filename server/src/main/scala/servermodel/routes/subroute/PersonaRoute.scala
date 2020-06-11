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
import utils.{StatusCodes=>statusCodes}
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
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
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
        case Request(Some(assumi)) => onComplete(PersonaOperation.assumi(assumi)) {
          case Success(Some(idPerson)) =>onComplete(PersonaOperation.recoveryPassword(idPerson)) {
            case Success(value) => complete(Response(StatusCodes.Created.intValue, value))
            case t => anotherSuccessAndFailure(t)
          }
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }

    }

  def deletePersona(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.delete(value)) {
          case Success(Some(statusCodes.SUCCES_CODE)) => complete(Response(StatusCodes.OK.intValue, Some(statusCodes.SUCCES_CODE)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.deleteAll(value)) {
          case Success(Some(result)) =>  complete(Response(StatusCodes.OK.intValue, Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Request[Persona]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.update(value)) {
          case Success(None) =>complete(Response(StatusCodes.OK.intValue, Some(statusCodes.SUCCES_CODE)))
          case Success(Some(id)) if id>0 =>  complete(Response(StatusCodes.Created.intValue, Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Request[Login]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.login(value)) {
          case Success(Some(person))  =>  complete(Response(StatusCodes.OK.intValue, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
  def recoveryPassword(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.recoveryPassword(value)){
          case Success(Some(login))  =>  complete(Response(StatusCodes.OK.intValue, Some(login)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def changePassword(): Route =
    post {
      entity(as[Request[ChangePassword]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.changePassword(value)){
          case Success(Some(statusCodes.SUCCES_CODE))  =>  complete(Response(StatusCodes.OK.intValue, Some(statusCodes.SUCCES_CODE)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def getStipendio: Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.getstipendiForPersona(value)){
          case Success(Some(salary))  =>  complete(Response(StatusCodes.OK.intValue, Some(salary)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
  def salaryCalculus(): Route =
    post{
      entity(as[Request[Dates]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.calculateStipendi(value.date)){
          case Success(Some(id)) if id>0 =>  complete(Response(StatusCodes.Created.intValue, Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

}
