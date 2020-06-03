package model.utils


import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import caseclass.CaseClassDB.{Contratto, Login, Persona, Stipendio, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Id

import scala.concurrent.Promise

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {
  val promiseStipendio: Promise[Option[List[Stipendio]]] = Promise[Option[List[Stipendio]]]
  val promiseZona: Promise[Option[List[Zona]]] = Promise[Option[List[Zona]]]
  val promiseTurn: Promise[Option[List[Turno]]] = Promise[Option[List[Turno]]]
  val promiseCont: Promise[Option[List[Contratto]]] = Promise[Option[List[Contratto]]]
  val promiseTer: Promise[Option[List[Terminale]]] = Promise[Option[List[Terminale]]]
  implicit val result: Promise[Unit] = Promise[Unit]
  val list: Promise[Option[List[Persona]]] = Promise[Option[List[Persona]]]
  implicit val promise: Promise[Option[Login]] = Promise[Option[Login]]
  implicit def id(id:Int):Id = Id(id)

}
