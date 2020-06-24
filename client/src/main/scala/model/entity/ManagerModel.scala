package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.Risultato
import caseclass.CaseClassHttpMessage._
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
   *         Future of List of absent [[caseclass.CaseClassHttpMessage.InfoAbsenceOnDay]]
   */
  def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]]

  /**
   * Returns the list of employees available to replace the absent
   *
   * @param idTerminale
   *                    Id of terminal with empty shift
   * @param idTurno
   *                Id of shift to replace
   * @param idRisultato
   *                    Id of work shift information to search for available employee
   * @return
   *         Future of list of [[caseclass.CaseClassHttpMessage.InfoReplacement]]
   */
  def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]]

  /**
   * Reassign chosen shift to another employee
   *
   * @param idRisultato
   *                    Id of shift to reassign
   * @param idNewDriver
   *                    Id of new driver to assign to shift
   * @return
   *         result of operation
   */
  def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]]
}

/**
 * Companion object for [[ManagerModel]]
 */
object ManagerModel {

  def apply(): ManagerModel = new ManagerModelHttp()

  /**
   * HTTP implementation for [[ManagerModel]]
   */
  private class ManagerModelHttp extends AbstractModel with ManagerModel {

    private val today: Date = Date.valueOf(LocalDate.now())

    override def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]] = {
      val request = Post(getURI("allabsences"), transform(Dates(today)))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoAbsenceOnDay]]])
    }

    override def extraAvailability(idTerminale: Int, idTurno: Int, idRisultato: Int): Future[Response[List[InfoReplacement]]] = {
      val request = Post(getURI("extraavailability"), transform((idTerminale, idTurno, idRisultato)))
      callHtpp(request).flatMap(response => Unmarshal(response).to[Response[List[InfoReplacement]]])
    }

    override def replaceShift(idRisultato: Int, idNewDriver: Int): Future[Response[Int]] = {
      val request = Post(getURI("replaceshift"), transform((idRisultato, idNewDriver)))
      callHtpp(request).flatMap(unMarshall)
    }
  }
}