package servermodel.routes.subroute


import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.{Assenza, Login, Persona}
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword, Dates, Id, Response}
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
      entity(as[Id]) { id =>
        onComplete(PersonaOperation.select(id.id)) {
          case Success(Some(person)) => complete(Response(StatusCodes.OK.intValue, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
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
      entity(as[Assumi]) { assumi =>
        onComplete(PersonaOperation.assumi(assumi)) {
          case Success(Some(t)) =>  complete(Response(StatusCodes.Created.intValue, Some(t)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deletePersona(): Route =
    post {
      entity(as[Id]) { order =>
        onComplete(PersonaOperation.delete(order.id)) {
          case Success(Some(1)) =>  complete(Response(StatusCodes.OK.intValue, Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[List[Id]]) { order =>
        onComplete(PersonaOperation.deleteAll(order.map(_.id))) {
          case Success(Some(_)) =>  complete(Response(StatusCodes.OK.intValue, Some(1)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Persona]) { persona =>
        onComplete(PersonaOperation.update(persona)) {
          case Success(Some(t)) =>  complete(Response(StatusCodes.Created.intValue, Some(Id(t))))
          case Success(None) =>complete(Response(StatusCodes.OK.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Login]) { login =>
        onComplete(PersonaOperation.login(login)) {
          case Success(Some(person))  =>  complete(Response(StatusCodes.OK.intValue, Some(person)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def recoveryPassword(): Route =
    post {
      entity(as[Id]) {
        idUser => onComplete(PersonaOperation.recoveryPassword(idUser.id)){
          case Success(login)  =>  complete(Response(StatusCodes.OK.intValue, Some(login)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def changePassword(): Route =
    post {
      entity(as[ChangePassword]) {
        change => onComplete(PersonaOperation.changePassword(change)){
          case Success(Some(1))  =>  complete(Response(StatusCodes.OK.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getStipendio: Route =
    post{
      entity(as[Id]) {
        id => onComplete(StipendioOperation.getstipendiForPersona(id.id)){
          case Success(Some(salary))  =>  complete(Response(StatusCodes.OK.intValue, Some(salary)))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def salaryCalculus(): Route =
    post{
      entity(as[Dates]) {
        date => onComplete(StipendioOperation.calculateStipendi(date.date)){
          case Success(Some(1))  =>  complete(Response(StatusCodes.Created.intValue, Some(Id(1))))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def addAbsence(): Route =
    post {
      entity(as[Assenza]){
        absence => onComplete(AssenzaOperation.insert(absence)){
          case Success(Some(1)) => complete(Response(StatusCodes.Created.intValue, Some(Id(1))))
          case t =>anotherSuccessAndFailure(t)
        }
      }
    }
}
