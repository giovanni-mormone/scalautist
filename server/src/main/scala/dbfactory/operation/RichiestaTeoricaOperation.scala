package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{Giorno, GiornoInSettimana, Richiesta, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.RequestGiorno
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceGiorno, InstanceGiornoInSettimana, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes._
import slick.jdbc.SQLServerProfile.api._
import dbfactory.util.Helper._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
 * @author Giovanni Mormone, Fabian Aspee, Francesco Cassano
 * Allows to perform operation on RichiestaTeoricaSet table
 */
trait RichiestaTeoricaOperation extends OperationCrud[RichiestaTeorica]{

  /**
   * Control that the information passed are good for the query
   *
   * @param requests List of [[caseclass.CaseClassDB.RichiestaTeorica]]
   * @param days List of [[caseclass.CaseClassHttpMessage.RequestGiorno]]
   * @return Operation result code:
   *         [[ERROR_CODE4]]: Error in [[caseclass.CaseClassDB.RichiestaTeorica]] set
   *         [[ERROR_CODE5]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some days in the set don't exist
   *         [[ERROR_CODE6]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some shifts in the set don't exist
   */
  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]]

  /**
   * Save info about a new theoretical request
   *
   * @param requests List of [[caseclass.CaseClassDB.RichiestaTeorica]]
   * @param days List of [[caseclass.CaseClassHttpMessage.RequestGiorno]]
   * @return Operation result code
   */
  def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]]
}

object RichiestaTeoricaOperation extends RichiestaTeoricaOperation {

  private val WEEK: List[String] = List("Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica")

  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {
    if(requests.isEmpty) return Future.successful(Some(ERROR_CODE4))
    if(days.isEmpty) return Future.successful(Some(ERROR_CODE5))

    val daysOk: Boolean =  days.map(day => (day.day.nomeGiorno, Option(WEEK(day.day.idGiornoSettimana - 1))) match {
        case (valName, Some(nameDay)) if valName.equals(nameDay) => Some(true)
        case _ => Some(false)
      }).forall(result => result.get)

    val requestOk: Future[Boolean] = InstanceTerminale.operation().execQueryFilter(_.id, filter => filter.id.inSet(requests.map(_.terminaleId)))
      .flatMap{
        case Some(list) if list.size == requests.size => Future.successful(true)
        case _ => Future.successful(false)
      }

    val shiftOk: Future[Boolean] = InstanceTurno.operation().execQueryFilter(_.id, filter => filter.id.inSet(days.map(_.shift)))
      .flatMap{
        case Some(list) if days.map(_.shift).forall(shift => list.contains(shift)) => Future.successful(true)
        case _ => Future.successful(false)
      }

    verify(daysOk, Await.result(requestOk, Duration.Inf), Await.result(shiftOk, Duration.Inf))
  }


  private def verify(daysOk: Boolean, requestOk: Boolean, shiftOk: Boolean): Future[Option[Int]] = (daysOk, requestOk, shiftOk) match {
    case (true, true, true) => Future.successful(Some(SUCCES_CODE))
    case (false, _, _) => Future.successful(Some(ERROR_CODE5))
    case (_, false, _) => Future.successful(Some(ERROR_CODE4))
    case (_, _, false) => Future.successful(Some(ERROR_CODE6))
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


  private def richiestaOperation(giorno: Option[Int], idRT: Option[List[Int]], day: RequestGiorno,idRichiesta:List[Int]=Nil): Future[Option[Int]] = (idRT,giorno) match {
    case (Some(idRichiestaTeorica::tail),Some(idGiorno)) => RichiestaOperation.insert(Richiesta(day.shift, idGiorno, idRichiestaTeorica))
      .flatMap{
        case Some(value) => richiestaOperation(giorno,Some(tail),day,value::idRichiesta)
        case None =>RichiestaOperation.deleteAll(idRichiesta).collect{
          case Some(value) if value == idRichiesta.length=> Some(ERROR_CODE1)
          case Some(value) if value < idRichiesta.length=> Some(ERROR_CODE2)
          case None =>Some(ERROR_CODE3)
        }
      }
    case (Some(List()),_) => Future.successful(Some(SUCCES_CODE))
    case (None,_) => Future.successful(Some(ERROR_CODE4))
    case (_,None) => Future.successful(Some(ERROR_CODE5))
  }
}

object  e extends  App{
  val  requests: List[RichiestaTeorica]=List(RichiestaTeorica(Date.valueOf(LocalDate.of(2020,6,14)),
                                              Some(Date.valueOf(LocalDate.of(2020,12,14))),1))

  val  requests2: List[RichiestaTeorica]=List(RichiestaTeorica(Date.valueOf(LocalDate.of(2020,6,14)),
                                             Some(Date.valueOf(LocalDate.of(2020,12,14))),2))

  val  requests3: List[RichiestaTeorica]=List(RichiestaTeorica(Date.valueOf(LocalDate.of(2020,6,14)),
                                              Some(Date.valueOf(LocalDate.of(2020,12,14))),3))

  val days: List[RequestGiorno]=List(RequestGiorno(Giorno(10,"Lunedi",1),1),RequestGiorno(Giorno(10,"Martedi",2),1),
    RequestGiorno(Giorno(10,"Mercoledi",3),1),RequestGiorno(Giorno(10,"Giovedi",4),1),
    RequestGiorno(Giorno(10,"Venerdi",5),1),RequestGiorno(Giorno(10,"Sabato",6),1),RequestGiorno(Giorno(10,"Domenica",7),1),
    RequestGiorno(Giorno(10,"Lunedi",1),2),RequestGiorno(Giorno(10,"Martedi",2),2),
    RequestGiorno(Giorno(10,"Mercoledi",3),2),RequestGiorno(Giorno(10,"Giovedi",4),2),
    RequestGiorno(Giorno(10,"Venerdi",5),2),RequestGiorno(Giorno(10,"Sabato",6),2),RequestGiorno(Giorno(10,"Domenica",7),2),
    RequestGiorno(Giorno(10,"Lunedi",1),3),RequestGiorno(Giorno(10,"Martedi",2),3),
    RequestGiorno(Giorno(10,"Mercoledi",3),3),RequestGiorno(Giorno(10,"Giovedi",4),3),
    RequestGiorno(Giorno(10,"Venerdi",5),3),RequestGiorno(Giorno(10,"Sabato",6),3),RequestGiorno(Giorno(10,"Domenica",7),3),
    RequestGiorno(Giorno(10,"Lunedi",1),4),RequestGiorno(Giorno(10,"Martedi",2),4),
    RequestGiorno(Giorno(10,"Mercoledi",3),4),RequestGiorno(Giorno(10,"Giovedi",4),4),
    RequestGiorno(Giorno(10,"Venerdi",5),4),RequestGiorno(Giorno(10,"Sabato",6),4),RequestGiorno(Giorno(10,"Domenica",7),4),
    RequestGiorno(Giorno(10,"Lunedi",1),5),RequestGiorno(Giorno(10,"Martedi",2),5),
    RequestGiorno(Giorno(10,"Mercoledi",3),5),RequestGiorno(Giorno(10,"Giovedi",4),5),
    RequestGiorno(Giorno(10,"Venerdi",5),5),RequestGiorno(Giorno(10,"Sabato",6),5),RequestGiorno(Giorno(10,"Domenica",7),5),
    RequestGiorno(Giorno(10,"Lunedi",1),6),RequestGiorno(Giorno(10,"Martedi",2),6),
    RequestGiorno(Giorno(10,"Mercoledi",3),6),RequestGiorno(Giorno(10,"Giovedi",4),6),
    RequestGiorno(Giorno(10,"Venerdi",5),6),RequestGiorno(Giorno(10,"Sabato",6),6),RequestGiorno(Giorno(10,"Domenica",7),6))

  val days2: List[RequestGiorno]=List(RequestGiorno(Giorno(20,"Lunedi",1),1),RequestGiorno(Giorno(20,"Martedi",2),1),
    RequestGiorno(Giorno(20,"Mercoledi",3),1),RequestGiorno(Giorno(20,"Giovedi",4),1),
    RequestGiorno(Giorno(20,"Venerdi",5),1),RequestGiorno(Giorno(20,"Sabato",6),1),RequestGiorno(Giorno(20,"Domenica",7),1),
    RequestGiorno(Giorno(20,"Lunedi",1),2),RequestGiorno(Giorno(20,"Martedi",2),2),
    RequestGiorno(Giorno(20,"Mercoledi",3),2),RequestGiorno(Giorno(20,"Giovedi",4),2),
    RequestGiorno(Giorno(20,"Venerdi",5),2),RequestGiorno(Giorno(20,"Sabato",6),2),RequestGiorno(Giorno(20,"Domenica",7),2),
    RequestGiorno(Giorno(20,"Lunedi",1),3),RequestGiorno(Giorno(20,"Martedi",2),3),
    RequestGiorno(Giorno(20,"Mercoledi",3),3),RequestGiorno(Giorno(20,"Giovedi",4),3),
    RequestGiorno(Giorno(20,"Venerdi",5),3),RequestGiorno(Giorno(20,"Sabato",6),3),RequestGiorno(Giorno(20,"Domenica",7),3),
    RequestGiorno(Giorno(20,"Lunedi",1),4),RequestGiorno(Giorno(20,"Martedi",2),4),
    RequestGiorno(Giorno(20,"Mercoledi",3),4),RequestGiorno(Giorno(20,"Giovedi",4),4),
    RequestGiorno(Giorno(20,"Venerdi",5),4),RequestGiorno(Giorno(20,"Sabato",6),4),RequestGiorno(Giorno(20,"Domenica",7),4),
    RequestGiorno(Giorno(20,"Lunedi",1),5),RequestGiorno(Giorno(20,"Martedi",2),5),
    RequestGiorno(Giorno(20,"Mercoledi",3),5),RequestGiorno(Giorno(20,"Giovedi",4),5),
    RequestGiorno(Giorno(20,"Venerdi",5),5),RequestGiorno(Giorno(20,"Sabato",6),5),RequestGiorno(Giorno(20,"Domenica",7),5),
    RequestGiorno(Giorno(20,"Lunedi",1),6),RequestGiorno(Giorno(20,"Martedi",2),6),
    RequestGiorno(Giorno(20,"Mercoledi",3),6),RequestGiorno(Giorno(20,"Giovedi",4),6),
    RequestGiorno(Giorno(20,"Venerdi",5),6),RequestGiorno(Giorno(20,"Sabato",6),6),RequestGiorno(Giorno(20,"Domenica",7),6))
  val t = Option(List.empty)
  RichiestaTeoricaOperation.saveRichiestaTeorica(requests,days) onComplete{
    case Success(value)=>RichiestaTeoricaOperation.saveRichiestaTeorica(requests2,days) onComplete{
      case Success(value)=>RichiestaTeoricaOperation.saveRichiestaTeorica(requests3,days2) onComplete{
        case Success(value)=>println(value)
        case Failure(exception) => println(exception)
      }
      case Failure(exception) => println(exception)
    }
    case Failure(exception) => println(exception)
  }
  while(true){}
}