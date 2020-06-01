package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.{Assenza, Login, Persona}
import caseclass.CaseClassHttpMessage.{ChangePassword, Id}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.PersonaOperation
import servermodel.routes.exception.SuccessAndFailure._

import scala.concurrent.Future
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
          case Success(t) => complete((StatusCodes.Found, t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  def getAllPersona: Route =
    post {
      onComplete(PersonaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createPersona(): Route =
    post {
      entity(as[Persona]) { order =>
        onComplete(PersonaOperation.insert(order)) {
          case Success(t) =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def createAllPersona(): Route =
    post {
      entity(as[List[Persona]]) { order =>
        onComplete(PersonaOperation.insertAll(order)) {
          case Success(t) if t.nonEmpty =>  complete(StatusCodes.Created)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deletePersona(): Route =
    post {
      entity(as[Id]) { order =>
        onComplete(PersonaOperation.delete(order.id)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[List[Id]]) { order =>
        onComplete(PersonaOperation.deleteAll(order.map(_.id))) {
          case Success(t) if t==1 =>  complete(StatusCodes.Gone)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Persona]) { persona =>
        onComplete(PersonaOperation.update(persona)) {
          case Success(t)  =>  complete(StatusCodes.OK)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Login]) { login =>
        onComplete(PersonaOperation.login(login)) {
          case Success(Some(t))  =>  complete((StatusCodes.Created,t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def updatePassword(): Route =
    post {
      entity(as[ChangePassword]) {
        change => onComplete(PersonaOperation.changePassword(change)){
          case Success(Some(1))  =>  complete(StatusCodes.Accepted)
          case t => anotherSuccessAndFailure(t)
        }
      }
    }

  def getStipendio(): Route =
    post{
      entity(as[Id]) {
        id => onComplete(stipendio(id.id)){
          case Success(t)  =>  complete((StatusCodes.Found,t))
          case t => anotherSuccessAndFailure(t)
        }
      }
    }
  import scala.concurrent.ExecutionContext.Implicits.global
  private def stipendio(id:Int) =Future{Id(id)}
 /* def addAbsence(): Route =
    post {
      entity(as[Assenza]){
        absence => onComplete(dbfactory.DummyDB.dummyReq()){     //TODO new request
          case Success(_) => complete(StatusCodes.Created)
        }
      }
    }*/
}
