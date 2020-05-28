package model.entity

import model.Model
import model.ModelDispatcher
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import jsonmessages.JsonFormats._
import akka.http.scaladsl.client.RequestBuilding.Post
import caseclass.CaseClassDB.Zona

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Success


/**
 * ZonaModel extends [[model.Model]].
 * Interface for Zona Entity operation
 */
trait ZonaModel extends Model{
  /**
   * Return all the zones in database
   * @return
   *         future of list of zones
   */
  def getZone: Future[List[Zona]]

  /**
   * Add a zone in database
   * @param nome
   *             zone's name
   * @return
   *         future
   */
  def addZona(nome:String): Future[Unit]

  /**
   * Delete a set of zones
   * @param ids
   *            sequence of zones ids to delete
   * @return
   *         future
   */
  def deleteAllZona(ids: Set[Int]): Future[Unit]
}

/**
 * Companin object of [[model.entity.ZonaModel]]. [Singleton]
 * Contains the implementation of its interface methods with http requests.
 */
object ZonaModel {

  private val instance = new ZonaModelHttp()

  def apply(): ZonaModel = instance

  private class ZonaModelHttp extends ZonaModel {

    override def getZone: Future[List[Zona]] = {
      val zonaAll = Promise[List[Zona]]
      val zonaRequest = HttpRequest(
        uri = getURI("getallzona"),
        method = HttpMethods.POST
      )
      dispatcher.serverRequest(zonaRequest).onComplete {
        case Success(result) =>
          Unmarshal(result).to[List[Zona]].onComplete(t => zonaAll.success(t.get))
      }
      zonaAll.future
    }

    override def addZona(nome: String): Future[Unit] = {
      val result = Promise[Unit]
      val zonaToIns = Zona(nome)
      val request = Post(getURI("createzona"), zonaToIns)
      dispatcher.serverRequest(request).onComplete(_ => result.success())
      result.future
    }

    override def deleteAllZona(ids: Set[Int]): Future[Unit] = {
      val result = Promise[Unit]
      var list: List[Zona] = List()
      ids.foreach(x => list = Zona("",Some(x))::list)
      val request = Post(getURI("deleteallzona"), list)
      dispatcher.serverRequest(request).onComplete(_ => result.success())
      result.future
    }
  }

}
