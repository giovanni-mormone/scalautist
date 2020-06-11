package servermodel.routes.subroute


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import akka.http.scaladsl.server.Route
import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage._
import dbfactory.operation.{AssenzaOperation, PersonaOperation, StipendioOperation}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import servermodel.routes.exception.SuccessAndFailure._
import messagecodes.{StatusCodes => statusCodes}

import scala.util.Success

/**
 * @author Francesco Cassano
 * PersonaRoute is an object that manage methods that act on the persona entity
 */
object PersonaRoute{
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def getPersona: Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.select(value)) {
          case Success(Some(person)) => complete(Response(statusCodes.SUCCES_CODE, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
  def getAllPersona: Route =
    post {
      onComplete(PersonaOperation.selectAll) {
        case Success(persons) =>  complete(Response(statusCodes.SUCCES_CODE, Some(persons)))
        case t => anotherSuccessAndFailure(t)
      }
    }
  def hirePerson: Route =
    post {
      entity(as[Request[Assumi]]) {
        case Request(Some(assumi)) => onComplete(PersonaOperation.assumi(assumi)) {
          case Success(Some(idPerson)) =>onComplete(PersonaOperation.recoveryPassword(idPerson)) {
            case Success(Some(login)) => complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(login)))
            case t => anotherSuccessAndFailure(t)
          }
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }

    }

  def deletePersona(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.delete(value)) {
          case Success(Some(statusCodes.SUCCES_CODE)) => complete(Response[Int](statusCodes.SUCCES_CODE))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.deleteAll(value)) {
          case Success(Some(result)) =>  complete(Response(statusCodes.SUCCES_CODE, Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Request[Persona]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.update(value)) {
          case Success(None) =>complete(Response[Int](statusCodes.SUCCES_CODE))
          case Success(Some(id)) if id>0 =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Request[Login]]) {
        case Request(Some(value))=> onComplete(PersonaOperation.login(value)) {
          case Success(Some(person)) => complete(Response(statusCodes.SUCCES_CODE, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
  def recoveryPassword(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.recoveryPassword(value)){
          case Success(Some(login))  =>  complete(Response(statusCodes.SUCCES_CODE, Some(login)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def changePassword(): Route =
    post {
      entity(as[Request[ChangePassword]]) {
        case Request(Some(value)) => onComplete(PersonaOperation.changePassword(value)){
          case Success(Some(statusCodes.SUCCES_CODE))  =>  complete(Response[Int](statusCodes.SUCCES_CODE))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def getStipendio: Route =
    post{
      entity(as[Request[Int]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.getstipendiForPersona(value)){
          case Success(Some(salary))  =>  complete(Response(statusCodes.SUCCES_CODE, Some(salary)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
  def salaryCalculus(): Route =
    post{
      entity(as[Request[Dates]]) {
        case Request(Some(value)) => onComplete(StipendioOperation.calculateStipendi(value.date)){
          case Success(Some(id)) if id>0 =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE, Some(id)))
          case t => anotherSuccessAndFailure(t)
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
}
