package dbfactory.operation

import caseclass.CaseClassDB.StoricoContratto
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceStoricoContratto
import dbfactory.implicitOperation.OperationCrud
import promise.PromiseFactory
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

/**
 * @author Giovanni Mormone
 * Trait which allows to perform operations on the StoricoContratto table.
 *
 */
trait StoricoContrattoOperation extends OperationCrud[StoricoContratto]{
  /**
   * Deletes all StoricoContratto associate to a specific Persona.
   *
   * @param idPerson
   *                 The id of the Persona's StoricoContratto group to delete.
   * @return
   *         The status of the operation.
   */
  def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]]

  /**
   * Deletes all StoricoContratto associate to a List of Persone.
   * @param idList
   *               The ids of the StoriciContratti's Personas to delete
   * @return
   *         The status of the operation.
   */
  def deleteAllStoricoForPersonList(idList: List[Int]): Future[Option[Int]]

}
object StoricoContrattoOperation extends StoricoContrattoOperation {

  override def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]] = {
    val promise = PromiseFactory.intPromise()
    InstanceStoricoContratto.operation().selectFilter(f => f.personaId === idPerson).onComplete(t => checkOption(t,promise))
    promise.future
  }

  override def deleteAllStoricoForPersonList(idList: List[Int]): Future[Option[Int]] = {
    val promise = PromiseFactory.intPromise()
    InstanceStoricoContratto.operation().selectFilter(f => f.personaId.inSet(idList)).onComplete(t => checkOption(t,promise))
      promise.future
    }

  private def checkOption(list:Try[Option[List[StoricoContratto]]],promise: Promise[Option[Int]]):Unit = list match{
    case Success(Some(List())) => promise.success(None)
    case Success(Some(t))  =>
      StoricoContrattoOperation.deleteAll(t.flatMap(storico => storico.idStoricoContratto)).onComplete{
        case Success(value) => promise.success(value)
        case Failure(exception) => promise.failure(exception)
      }
    case Failure(exception) => promise.failure(exception)
  }
}