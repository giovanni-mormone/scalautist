package model.entity

import java.sql.Date

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{Dates, InfoHome, InfoShift, Response, StipendioInformations}
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.Future


/**
 * DriverModel extends [[model.Model]].
 * Interface for driver's operation on data
 */
trait DriverModel {

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
}



object DriverModel {

  def apply(): DriverModel = new DriverResourceHttp()

  private class DriverResourceHttp extends AbstractModel with DriverModel{

    override def getSalary(id: Int): Future[Response[List[Stipendio]]] = {
      val request = Post(getURI("getstipendio"),transform(id))
      callServerSalary(request)
    }
    private def callServerSalary(request: HttpRequest)=
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[List[Stipendio]]])

    override def getInfoForSalary(id: Int):Future[Response[StipendioInformations]] = {
      val request = Post(getURI("getinfostipendio"),transform(id))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[StipendioInformations]])
    }

    override def getTurniInDay(userId: Int): Future[Response[InfoHome]] = {
      val request = Post(getURI("getturniinday"),transform(userId,Dates(new Date(System.currentTimeMillis()))))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[InfoHome]])
    }

    override def getTurniSettimanali(userId: Int): Future[Response[InfoShift]] = {
      val request = Post(getURI("getturniinweek"),transform(userId,Dates(new Date(System.currentTimeMillis()))))
      callHtpp(request).flatMap(resultRequest => Unmarshal(resultRequest).to[Response[InfoShift]])
    }
  }

}
