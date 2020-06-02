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
  def getURI(request: String):String
  def shutdownActorSystem():Future[Terminated]
  def doHttp(request:HttpRequest):Future[HttpResponse]
  def success[A,B](function:Try[Option[A]])(implicit promise:Promise[Option[A]]): Unit
  def failure[A](function:Try[Throwable])(implicit promise:Promise[Option[A]]): Unit
  //protected def composePostRequest(request: String)
}
