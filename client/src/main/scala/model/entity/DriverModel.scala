package model.entity

import java.sql.Date
import java.time.LocalDate
import java.util.Calendar

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Disponibilita, Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{Dates, Id, InfoHome, InfoShift, Response, StipendioInformations}
import jsonmessages.JsonFormats._
import model.AbstractModel
import persistence.ConfigReceiverPersistence

import scala.concurrent.Future


/**
 * @author Giovanni Mormone, Francesco Cassano
 * DriverModel extends [[model.Model]].
 * Interface for driver's operation on data
 */
trait DriverModel {
  def verifyExistedQueue(userId: Option[Int], notificationReceived: (String, Long) => Unit): Unit


  /**
   * Method that obtains salary for a person
   *
   * @param id id that represent  id of a persona
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getSalary(id: Int): Future[Response[List[Stipendio]]]

  /**
   * Method that obtains information of salary for a person
   *
   * @param id id that represent  id of salary
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getInfoForSalary(id: Int): Future[Response[StipendioInformations]]

  /**
   * Method that obtains the informations about the turni of a day for a driver
   * @param userId
   * @return
   */
  def getTurniInDay(userId: Int): Future[Response[InfoHome]]

  /**
   * Method that obtains the informations about the turni of a week for a driver
   *
   * @param userId
   * @return
   */
  def getTurniSettimanali(userId: Int):  Future[Response[InfoShift]]

  /**
   * Method that obtains the information about the disponibilita in a week
   *
   * @param userId
   *               id employee to control
   * @return
   *         List of day of possible extra
   */
  def getDisponibilita(userId: Int): Future[Response[List[String]]]

  /**
   * Method that set new availability to extra shift
   *
   * @param giorno1
   *                String of day one
   * @param giorno2
   *                String of day two
   */
  def setDisponibilita(giorno1: String, giorno2: String, user: Int): Future[Response[Int]]
}



object DriverModel {

  def apply(): DriverModel = new DriverResourceHttp()

  private class DriverResourceHttp extends AbstractModel with DriverModel{

    override def getSalary(id: Int): Future[Response[List[Stipendio]]] = {
      val request = Post(getURI("getstipendio"),transform(id))
      callServerSalary(request)
    }
    private def callServerSalary(request: HttpRequest)=
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Stipendio]]])

    override def getInfoForSalary(id: Int):Future[Response[StipendioInformations]] = {
      val request = Post(getURI("getinfostipendio"),transform(id))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[StipendioInformations]])
    }

    override def getTurniInDay(userId: Int): Future[Response[InfoHome]] = {
      val request = Post(getURI("getturniinday"),transform(userId,Dates(new Date(System.currentTimeMillis()))))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[InfoHome]])
    }

    override def getTurniSettimanali(userId: Int): Future[Response[InfoShift]] = {
      val request = Post(getURI("getturniinweek"),transform(userId,Dates(new Date(System.currentTimeMillis()))))
      callHttp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[InfoShift]])
    }

    override def getDisponibilita(userId: Int): Future[Response[List[String]]] = {
      val request = Post(getURI("getdisponibilitainweek"), transform(userId, Dates(new Date(System.currentTimeMillis()))))
      callHttp(request).flatMap(result => Unmarshal(result).to[Response[List[String]]])
    }

    override def setDisponibilita(giorno1: String, giorno2: String, user: Int): Future[Response[Int]] = {
      val c = Calendar.getInstance()
      c.setTime(new Date(System.currentTimeMillis()))
      val request = Post(getURI("setdisponibilita"), transform((Disponibilita(c.get(Calendar.WEEK_OF_YEAR), giorno1, giorno2), Id(user))))
      callHttp(request).flatMap(unMarshall)
    }

    override def verifyExistedQueue(userId: Option[Int],f:(String,Long)=>Unit): Unit = {
      userId.foreach(queue =>{
        val receiver = ConfigReceiverPersistence("driver"+queue.toString,queue.toString)
        receiver.start()
        receiver.receiveMessage(f)
      })
    }
  }

}
