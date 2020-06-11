package servermodel.routes.subroute

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{as, complete, entity, post, _}
import caseclass.CaseClassDB.Zona
import caseclass.CaseClassHttpMessage.{Request, Response}
import jsonmessages.JsonFormats._
import servermodel.routes.exception.RouteException
import dbfactory.operation.ZonaOperation
import servermodel.routes.exception.SuccessAndFailure.anotherSuccessAndFailure

import scala.util.Success
import utils.{StatusCodes=>statusCodes}

object ZonaRoute {
  private val badHttpRequest: Response[Int] =Response[Int](statusCodes.BAD_REQUEST)
  def getAllZona: Route =
    post {
      onComplete(ZonaOperation.selectAll) {
        case Success(Some(zone)) =>  complete(Response(statusCodes.SUCCES_CODE,Some(zone)))
        case t => anotherSuccessAndFailure(t)
      }
    }

  def createZona(): Route =
    post {
      entity(as[Request[Zona]]) {
        case Request(Some(zona))=> onComplete(ZonaOperation.insert(zona)) {
          case Success(Some(id)) =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,Some(Zona(zona.zones,Some(id)))))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def createAllZona(): Route =
    post {
      entity(as[Request[List[Zona]]]) {
        case Request(Some(zona))=>onComplete(ZonaOperation.insertAll(zona)) {
          case Success(Some(id)) =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def deleteZona(): Route =
    post {
      entity(as[Request[Int]]) {
        case Request(Some(id))=> onComplete(ZonaOperation.delete(id)) {
          case Success(Some(statusCodes.SUCCES_CODE)) =>  complete(Response[Int](statusCodes.SUCCES_CODE))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def deleteAllZona(): Route =
    post {
      entity(as[Request[List[Int]]]) {
        case Request(Some(id))=> onComplete(ZonaOperation.deleteAll(id)) {
          case Success(Some(result)) =>  complete(Response(statusCodes.SUCCES_CODE,Some(result)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }

  def updateZona(): Route =
    post {
      entity(as[Request[Zona]]) {
        case Request(Some(zona))=>onComplete(ZonaOperation.update(zona)) {
          case Success(None) =>complete(Response[Int](statusCodes.SUCCES_CODE))
          case Success(Some(id)) =>  complete(StatusCodes.Created,Response(statusCodes.SUCCES_CODE,Some(id)))
          case t => anotherSuccessAndFailure(t)
        }
        case _ => complete(StatusCodes.BadRequest,badHttpRequest)
      }
    }
}
