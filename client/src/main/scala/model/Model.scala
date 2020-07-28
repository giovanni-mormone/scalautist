package model

import akka.actor.Terminated
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.{Future, Promise}
import scala.util.Try

/**
 * @author Francesco Cassano
 * Generic definition of model
 */
trait Model {
  /**
   * method that encapsulate url for the request
   * @param request endPoint of url
   * @return url where the call will be made
   */
  def getURI(request: String):String

  /**
   * method that encapsulate HttRequest for request to server
   * @param request HttRequest that contains info for the call that will be made
   * @return Future of HttResponse with response of the server
   */
  def doHttp(request:HttpRequest):Future[HttpResponse]
}
