package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.pattern.FutureRef
import caseclass.CaseClassDB.{Persona, Risultato}
import caseclass.CaseClassHttpMessage.{Dates, Id, InfoVacantShift, Response}
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.Future

/**
 * @author Francesco Cassano
 * ManagerModel extends [[model.Model]].
 * Interface for System Manager's operation on data
 */
trait ManagerModel {

  /**
   * Returns a list that contains all absent people in the date of today
   *
   * @return
   *         Future of List of absent [[caseclass.CaseClassDB.Persona]]
   */
  def absentPeople(): Future[Response[List[Persona]]]

  /**
   * Returns the list of work shifts for the chosen employee
   *
   * @param idPersona
   *                  Chosen employee id
   * @return
   *         Future of List of shifts in [[caseclass.CaseClassDB.Risultato]] instance
   */
  def vacantJobShift(idPersona: Int): Future[Response[List[Risultato]]]

  /**
   * Returns the list of employees available to replace the absent
   *
   * @param idRisultato
   *                    Id of work shift information to search for available employee
   * @return
   */
  def extraAvailability(idRisultato: Int): Future[Response[List[Persona]]]
}

/**
 * Companion object for [[ManagerModel]]
 */
object ManagerModel {

  def apply() = new ManagerModelHttp()

  /**
   * HTTP implementation for [[ManagerModel]]
   */
  private class ManagerModelHttp extends AbstractModel with ManagerModel {

    private val today: Date = Date.valueOf(LocalDate.now())

    override def absentPeople(): Future[Response[List[Persona]]] = {
      val request = Post(getURI("allabsences"), transform(Dates(today)))
      callRequest[Persona](request)
    }

    override def vacantJobShift(idPersona: Int): Future[Response[List[Risultato]]] = {
      val request = Post(getURI("allvacantshift"), transform(InfoVacantShift(idPersona, today.toString)))
      callRequest[Risultato](request)
    }

    override def extraAvailability(idRisultato: Int): Future[Response[List[Persona]]] = {
      val request = Post(getURI("extraavailability"), transform(Id(idRisultato)))
      callRequest[Persona](request)
    }

    private def callRequest[A](request: HttpRequest): Future[Response[List[A]]] =
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[A]]])

  }
}