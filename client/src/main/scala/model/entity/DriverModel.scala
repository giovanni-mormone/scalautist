package model.entity

import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{Response, StipendioInformations}
import jsonmessages.JsonFormats._
import model.AbstractModel

import scala.concurrent.Future


/**
 * DriverModel extends [[model.Model]].
 * Interface for driver's operation on data
 */
trait DriverModel extends AbstractModel{
  /**
   * Return a set of one driver's turn
   * @param id
   * User id
   * @param startData
   * The start date of the workshift period to be shown
   * @param endData
   * The end date of the workshift period to be shown
   * @return
   * Future of list of Turno
   */
  def getWorkshift(id: Int, startData: String, endData: String): Future[List[Turno]]


  /**
   *  Method that obtains salary for a person
   * @param id id that represent  id of a persona
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getSalary(id:Int):Future[Response[List[Stipendio]]]

  /**
   *  Method that obtains information of salary for a person
   * @param id id that represent  id of salary
   * @return Option of List of Stipendio that represent all salary of a persona
   */
  def getInfoForSalary(id:Int):Future[Response[StipendioInformations]]

}

object DriverModel {

  def apply(): DriverModel = new DriverResourceHttp()

  private class DriverResourceHttp extends DriverModel {

    override def getWorkshift(id: Int, startData: String, endData: String): Future[List[Turno]] = ???

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
  }

}
