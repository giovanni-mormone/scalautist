package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.Zona
import caseclass.CaseClassHttpMessage.{Id, Request, Response}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.ZonaOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success
import utils.{StatusCodes=>statusCodes}

object ZonaRoute {
  def getAllZona: Route =
    post {
      onComplete(ZonaOperation.selectAll) {
        case Success(Some(zone)) =>  complete(Response(StatusCodes.Found.intValue,Some(zone)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createZona(): Route =
    post {
      entity(as[Request[Zona]]) {
        case Request(Some(zona))=> onComplete(ZonaOperation.insert(zona)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(Zona(zona.zones,Some(id)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def createAllZona(): Route =
    post {
      entity(as[Request[List[Zona]]]) {
        case Request(Some(zona))=>onComplete(ZonaOperation.insertAll(zona)) {
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteZona(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=> onComplete(ZonaOperation.delete(id)) {
          case Success(Some(statusCodes.SUCCES_CODE)) =>  complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def deleteAllZona(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(ZonaOperation.deleteAll(id)) {
          case Success(Some(result)) =>  complete(Response(StatusCodes.OK.intValue,Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }

  def updateZona(): Route =
    post {
      entity(as[Request[Zona]]) {
        case Request(Some(zona))=>onComplete(ZonaOperation.update(zona)) {
          case Success(None) =>complete(Response(StatusCodes.OK.intValue,Some(statusCodes.SUCCES_CODE)))
          case Success(Some(id)) =>  complete(Response(StatusCodes.Created.intValue,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(Response(StatusCodes.BadRequest.intValue, Some(statusCodes.BAD_REQUEST)))
      }
    }
}
