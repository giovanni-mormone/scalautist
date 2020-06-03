package dbfactory.operation

import caseclass.CaseClassDB.StoricoContratto
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceStoricoContratto
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
trait StoricoContrattoOperation extends OperationCrud[StoricoContratto]{
  def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]]
}
object StoricoContrattoOperation extends StoricoContrattoOperation {
  override def deleteAllStoricoForPerson(idPerson: Int): Future[Option[Int]] = {
    val promise:  Promise[Option[Int]] = Promise[Option[Int]]
    InstanceStoricoContratto.operation().selectFilter(f => f.personaId === idPerson).onComplete{
      case Success(value) if value.nonEmpty =>
        StoricoContrattoOperation.deleteAll(value.head.flatMap(storico => storico.idStoricoContratto)).onComplete{
          case Success(value) => promise.success(value)
          case Failure(exception) => promise.failure(exception)
        }
      case Failure(exception) => promise.failure(exception)
    }
    promise.future
  }

}