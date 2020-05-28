package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.{Assenza, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.DummyDB

import scala.util.Success //TODO

object PersonaRoute {

  def dummy() =
    onComplete(DummyDB.dummyReq()) {
      case t => complete(StatusCodes.Accepted,t)
    }

  def methodDummy(): Route =
    post {
      dummy()
    }

  def getPersona(id: Int): Route =
    get {
      dummy()
      /*onComplete(PersonaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        case Success(None) => complete(StatusCodes.NotFound)
      }*/
    }

  def getAllPersona: Route =
    post {
      dummy()
      /*dummy()
      onComplete(PersonaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }*/
    }

  def createPersona(): Route =
    post {
      dummy()
      /*entity(as[Persona]) { order =>
        onComplete(PersonaOperation.insert(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }*/
    }

  def createAllPersona(): Route =
    post {
      dummy()
      /*entity(as[List[Persona]]) { order =>
        onComplete(PersonaOperation.insertAll(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }*/
    }

  def deletePersona(): Route =
    post {
      dummy()
      /*entity(as[Persona]) { order =>
        onComplete(PersonaOperation.delete(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }*/
    }

  def deleteAllPersona(): Route =
    post {
      dummy()
      /*entity(as[List[Persona]]) { order =>
        onComplete(PersonaOperation.deleteAll(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }*/
    }

  def updatePersona(): Route =
    post {
      dummy()
      /*entity(as[Persona]) { persona =>
        onComplete(PersonaOperation.update(persona)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }*/
    }

  def loginPersona(): Route =
    post {
      dummy()
      /*entity(as[Login]) { login =>
        onComplete(PersonaOperation.login(login)) {
          case Success(t)  =>  complete(StatusCodes.Created,t)
        }
      }*/
    }

  def updatePassword: Route =
    post {
      dummy()
      /*entity(as[ChangePassword]) {
        change => onComplete(PersonaOperation.changePassword(change)){
          case Success(credential) => complete(StatusCodes.Created, credential)
        }
      }*/
    }

  def addAbsence: Route =
    post {
      dummy()
      /*entity(as[Assenza]){
        absence => onComplete(PersonaOperation.addAbsence(absence)){
          case Success(_) => complete(StatusCodes.Created)
        }
      }*/
    }
}
