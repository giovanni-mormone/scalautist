package dbfactory.operation

import caseclass.CaseClassDB.{Richiesta, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.RequestGiorno
import dbfactory.implicitOperation.OperationCrud

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

/**
 * @author Giovanni Mormone, Fabian Aspee, Francesco Cassano
 * Allows to perform operation on RichiestaTeoricaSet table
 */
trait RichiestaTeoricaOperation extends OperationCrud[RichiestaTeorica]{

  /**
   * Save info about a new theoretical request
   *
   * @param requests List of [[caseclass.CaseClassDB.RichiestaTeorica]]
   * @param days List of [[caseclass.CaseClassHttpMessage.RequestGiorno]]
   * @return Code of something
   */
  def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]]
}

object RichiestaTeoricaOperation extends RichiestaTeoricaOperation {
  override def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {
    /*insertAll(requests).flatMap(idRT => {
      days.map(day => GiornoOperation.insert(day.day) flatMap(idG => idRT.toList.flatten.map(idrt => RichiestaOperation.insert(Richiesta(day.shift, idG.get, idrt))
        .collect{
          case None => Some(-1)
          case _ => Some(0)
      }).map(_.flatMap{
        case Some(result) => result
      })))
    })*/
    
    Future.successful(None)
  }
}