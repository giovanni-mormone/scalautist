package model.entity

import java.sql.Date
import java.time.LocalDate

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Giorno, RichiestaTeorica}
import caseclass.CaseClassHttpMessage._
import jsonmessages.JsonFormats._
import model.AbstractModel
import utils.TransferObject.InfoRichiesta

import scala.collection.immutable.HashMap
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

  /**
   * Define theoretical request for shifts
   * @param info
   *             information to build request body like an instance of [[InfoRichiesta]]
   * @return
   *         result of operation
   */
  def defineTheoreticalRequest(info: InfoRichiesta): Future[Response[Int]]

  /**
   * method that call server for execute algorithm for assigment free day and shift
   * @param info case class that contains all info for algorithm and yours execution
   */
  def esecuzioneAlgoritmo(info:AlgorithmExecute):Unit
}

/**
 * Companion object for [[ManagerModel]]
 */
object ManagerModel {

  def apply():ManagerModel = new ManagerModelHttp()

  /**
   * HTTP implementation for [[ManagerModel]]
   */
  private class ManagerModelHttp extends AbstractModel with ManagerModel {

    private val TODAY: Date = Date.valueOf(LocalDate.now())
    private val WEEK: HashMap[Int, String] = HashMap(1 -> "Lunedi", 2 -> "Martedi", 3 -> "Mercoledi", 4 -> "Giovedi",
                                                      5 -> "Venerdi", 6 -> "Sabato", 7 -> "Domenica")

    override def allAbsences(): Future[Response[List[InfoAbsenceOnDay]]] = {
      val request = Post(getURI("allabsences"), transform(Dates(TODAY)))
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

    override def defineTheoreticalRequest(info: InfoRichiesta): Future[Response[Int]] = {
      val theoreticalRequest: List[RichiestaTeorica] =
        info.idTerminal.map(terminal => RichiestaTeorica(info.date, Some(info.dateF), terminal))
      val dailyRequest: List[RequestGiorno] =
        info.info.flatMap(giorno => giorno._2.map(needed =>
          RequestGiorno(Giorno(needed._2, WEEK.getOrElse(giorno._1, "Vacanza"), giorno._1), needed._1)))
      val requestBody: AssignRichiestaTeorica = AssignRichiestaTeorica(theoreticalRequest, dailyRequest)
      println(requestBody)
      val request = Post(getURI("definedailyrequest"), transform(requestBody))
      callHtpp(request).flatMap(unMarshall)
    }

    override def esecuzioneAlgoritmo(info: AlgorithmExecute): Unit = {
      val request = Post(getURI("executealgorithm"), transform(info))
      callHtpp(request)
    }
  }
}

object t extends App{
  val first = AssignRichiestaTeorica(
    List(RichiestaTeorica(Date.valueOf("2020-06-10"),Some(Date.valueOf("2020-07-08")),2,None),
    RichiestaTeorica(Date.valueOf("2020-06-10"),Some(Date.valueOf("2020-07-08")),3,None),
    RichiestaTeorica(Date.valueOf("2020-06-10"),Some(Date.valueOf("2020-07-08")),4,None)),
    List(RequestGiorno(Giorno(1,"Lunedi",1,None),6), RequestGiorno(Giorno(1,"Lunedi",1,None),5),
      RequestGiorno(Giorno(1,"Lunedi",1,None),4), RequestGiorno(Giorno(1,"Lunedi",1,None),3),
      RequestGiorno(Giorno(1,"Lunedi",1,None),2), RequestGiorno(Giorno(1,"Lunedi",1,None),1),
      RequestGiorno(Giorno(2,"Martedi",2,None),6), RequestGiorno(Giorno(2,"Martedi",2,None),5),
      RequestGiorno(Giorno(2,"Martedi",2,None),4), RequestGiorno(Giorno(2,"Martedi",2,None),3),
      RequestGiorno(Giorno(2,"Martedi",2,None),2), RequestGiorno(Giorno(2,"Martedi",2,None),1),
      RequestGiorno(Giorno(3,"Mercoledi",3,None),6), RequestGiorno(Giorno(3,"Mercoledi",3,None),5),
      RequestGiorno(Giorno(3,"Mercoledi",3,None),4), RequestGiorno(Giorno(3,"Mercoledi",3,None),3),
      RequestGiorno(Giorno(3,"Mercoledi",3,None),2), RequestGiorno(Giorno(3,"Mercoledi",3,None),1),
      RequestGiorno(Giorno(4,"Giovedi",4,None),6), RequestGiorno(Giorno(4,"Giovedi",4,None),5),
      RequestGiorno(Giorno(4,"Giovedi",4,None),4), RequestGiorno(Giorno(4,"Giovedi",4,None),3),
      RequestGiorno(Giorno(4,"Giovedi",4,None),2), RequestGiorno(Giorno(4,"Giovedi",4,None),1),
      RequestGiorno(Giorno(5,"Venerdi",5,None),6), RequestGiorno(Giorno(5,"Venerdi",5,None),5),
      RequestGiorno(Giorno(5,"Venerdi",5,None),4), RequestGiorno(Giorno(5,"Venerdi",5,None),3),
      RequestGiorno(Giorno(5,"Venerdi",5,None),2), RequestGiorno(Giorno(5,"Venerdi",5,None),1),
      RequestGiorno(Giorno(6,"Sabato",6,None),6), RequestGiorno(Giorno(6,"Sabato",6,None),5),
      RequestGiorno(Giorno(6,"Sabato",6,None),4), RequestGiorno(Giorno(6,"Sabato",6,None),3),
      RequestGiorno(Giorno(6,"Sabato",6,None),2), RequestGiorno(Giorno(6,"Sabato",6,None),1),
      RequestGiorno(Giorno(7,"Domenica",7,None),6), RequestGiorno(Giorno(7,"Domenica",7,None),5),
      RequestGiorno(Giorno(7,"Domenica",7,None),4), RequestGiorno(Giorno(7,"Domenica",7,None),3),
      RequestGiorno(Giorno(7,"Domenica",7,None),2), RequestGiorno(Giorno(7,"Domenica",7,None),1)))

  val t = first.days.map(element => element).groupBy(_.day.idGiornoSettimana)
  println(t)
  println(first)
}