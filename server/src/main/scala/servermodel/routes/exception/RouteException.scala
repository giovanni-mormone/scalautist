package servermodel.routes.exception

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server._
import Directives._

/**
 * Exception Handler
 */
object RouteException {

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case _: ArithmeticException =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(InternalServerError, entity = "Bad numbers, bad result!!!"))
        }
      case _: IllegalArgumentException =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(BadGateway, entity = "Bad Gateway Error"))
        }
      case _: IllegalAccessError =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(BadGateway, entity = "Bad Gateway Error"))
        }
      case cause: Exception =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(BadRequest, entity = s"Exception thrown from LeaderboardPost: ${cause.getMessage}"))
        }
    }
}
