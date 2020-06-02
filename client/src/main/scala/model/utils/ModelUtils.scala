package model.utils

import caseclass.CaseClassDB.{Contratto, Login, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Id

import scala.concurrent.Promise

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {

  val promiseZona: Promise[Option[Zona]] = Promise[Option[Zona]]
  val promiseTurn: Promise[Option[List[Turno]]] = Promise[Option[List[Turno]]]
  val promiseCont: Promise[Option[List[Contratto]]] = Promise[Option[List[Contratto]]]
  val promiseTer: Promise[Option[List[Terminale]]] = Promise[Option[List[Terminale]]]
  implicit val result: Promise[Unit] = Promise[Unit]
  val list: Promise[Option[List[Persona]]] = Promise[Option[List[Persona]]]
  implicit val promise: Promise[Option[Login]] = Promise[Option[Login]]
  implicit def id(id:Int):Id = Id(id)

}
