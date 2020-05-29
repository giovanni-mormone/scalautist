package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, post, _}
import caseclass.CaseClassDB.{Assenza, Login, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword
import jsonmessages.JsonFormats._


import servermodel.routes.exception.RouteException
import dbfactory.DummyDB
import dbfactory.operation.PersonaOperation

import scala.util.{Success, Failure}

object PersonaRoute {

  def methodDummy(): Route =
    post {
      onComplete(DummyDB.dummyReq()) {
        case t => complete(StatusCodes.Accepted,t)
      }
    }

  def getPersona(id: Int): Route =
    get {
      onComplete(PersonaOperation.select(id)) {
        case Success(t) =>    complete((StatusCodes.Found,t))
        case Success(None) => complete(StatusCodes.NotFound)
      }
    }

  def getAllPersona: Route =
    post {
      onComplete(PersonaOperation.selectAll) {
        case Success(t) =>  complete((StatusCodes.Found,t))
      }
    }

  def createPersona(): Route =
    post {
      entity(as[Persona]) { order =>
        onComplete(PersonaOperation.insert(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }
    }

  def createAllPersona(): Route =
    post {
      entity(as[List[Persona]]) { order =>
        onComplete(PersonaOperation.insertAll(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }
    }

  def deletePersona(): Route =
    post {
      entity(as[Persona]) { order =>
        onComplete(PersonaOperation.delete(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }
    }

  def deleteAllPersona(): Route =
    post {
      entity(as[List[Persona]]) { order =>
        onComplete(PersonaOperation.deleteAll(order)) {
          case Success(t) if t==1 =>  complete(StatusCodes.Created)
        }
      }
    }

  def updatePersona(): Route =
    post {
      entity(as[Persona]) { persona =>
        onComplete(PersonaOperation.update(persona)) {
          case Success(t)  =>  complete(StatusCodes.Created)
        }
      }
    }

  def loginPersona(): Route =
    post {
      entity(as[Login]) { login =>
        onComplete(PersonaOperation.login(login)) {
          case Success(Some(t))  =>  complete((StatusCodes.Created,t))
          case Success(None) => complete(StatusCodes.NotFound)
          case Failure(_) => complete(StatusCodes.BadRequest)
        }
      }
    }

  def updatePassword(): Route =
    post {
      import jsonmessages.JsonMessageFormats._
      entity(as[ChangePassword]) {
        change => onComplete(PersonaOperation.changePassword(change)){
          case Success(Some(1))  =>  complete(StatusCodes.Accepted)
          case Success(Some(_))  =>  complete(StatusCodes.NotFound)
          case Failure(_)        =>  complete(StatusCodes.BadRequest)
        }
      }
    }

  def addAbsence(): Route =
    post {
      entity(as[Assenza]){
        absence => onComplete(dbfactory.DummyDB.dummyReq()){     //TODO new request
          case Success(_) => complete(StatusCodes.Created)
        }
      }
    }

  /*def getNewPassword(): Route =
    post{
      //TODO call dbmethod PersonaOperation
    }*/
}
