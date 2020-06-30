package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{Giorno, Richiesta, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.RequestGiorno
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceGiorno
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.{Failure, Success}

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

  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno])={

  }

  override def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] =
    for{
      allInsert<-insertAll(requests)
      insertDay<-insertDay(days,allInsert)
    }yield insertDay

  private def selectGiornoId(day:RequestGiorno): Future[Option[Int]] =
    InstanceGiorno.operation().execQueryFilter(giorno=>giorno.id,
      giorno=>giorno.idGiornoSettimana === day.day.idGiornoSettimana && giorno.quantita===day.day.quantita)
      .flatMap {
        case Some(value) =>selectId(value)
        case None =>GiornoOperation.insert(day.day)
      }

  private def selectId(value:List[Int]): Future[Option[Int]] =
    Future.successful(value match {
      case List(result) => Some(result)
      case Nil => None
    })

  private def insertDay(days: List[RequestGiorno], idRT:Option[List[Int]]): Future[Option[Int]] =
    days.map(day =>
      for {
        giorno <- selectGiornoId(day)
        richiesta <-   richiestaOperation(giorno,idRT,day)
      } yield richiesta
    ).foldLeft(Future.successful(Option(0))){
      case (defaulFuture,future)=>defaulFuture.zip(future).map {
        case (option, option1) =>
          (option1.toList :: option.toList :: List.empty).flatten match {
            case List(first,_)=>Some(first)
            case Nil =>None
          }
      }
    }.collect {
      case Some(value) => Some(value)
      case None =>None
    }

  private def richiestaOperation(giorno: Option[Int], idRT: Option[List[Int]], day: RequestGiorno,idRichiesta:List[Int]=Nil):Future[Option[Int]] = (idRT,giorno) match {
    case (Some(idRichiestaTeorica::tail),Some(idGiorno)) => RichiestaOperation.insert(Richiesta(day.shift, idGiorno, idRichiestaTeorica))
      .flatMap{
        case Some(value) => richiestaOperation(giorno,Some(tail),day,value::idRichiesta)
        case None =>RichiestaOperation.deleteAll(idRichiesta).collect{
          case Some(value) if value == idRichiesta.length=> Some(StatusCodes.ERROR_CODE1)
          case Some(value) if value < idRichiesta.length=> Some(StatusCodes.ERROR_CODE2)
          case None =>Some(StatusCodes.ERROR_CODE3)
        }
      }
    case (Some(List()),_) => Future.successful(Some(StatusCodes.SUCCES_CODE))
    case (None,_) => Future.successful(Some(StatusCodes.ERROR_CODE4))
    case (_,None) => Future.successful(Some(StatusCodes.ERROR_CODE5))
  }
}
