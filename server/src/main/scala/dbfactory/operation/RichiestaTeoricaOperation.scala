package dbfactory.operation

import java.sql.Date
import java.time.LocalDate

import caseclass.CaseClassDB.{Giorno, Richiesta, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.RequestGiorno
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceGiorno, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.util.Helper._
import messagecodes.StatusCodes._
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
  private val MIN_IN_WEEK: Int = 1
  private val MAX_IN_WEEK: Int = 7

  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {
    if(requests.isEmpty) return Future.successful(Some(ERROR_CODE4))
    if(days.isEmpty) return Future.successful(Some(ERROR_CODE5))

    for {
      daysOk <- verifyDays(days)
      requestOk <- verifyRequest(daysOk, requests)
      shiftOk <- verifyShift(requestOk, days)
    } yield verify(daysOk, requestOk, shiftOk)

  }

  private def verifyDays(days: List[RequestGiorno]): Future[Option[Boolean]] = Future(
      Option(days.map(day => (day.day.nomeGiorno,
              if (day.day.idGiornoSettimana >= MIN_IN_WEEK && day.day.idGiornoSettimana <= MAX_IN_WEEK) Some(WEEK(day.day.idGiornoSettimana - 1)) else None) match {
        case (valName, Some(nameDay)) if valName.equals(nameDay) => Some(SUCCES_CODE)
        case _ => None
      }).forall(_.isDefined))
    )

  private def verifyRequest(daysOk: Option[Boolean], requests: List[RichiestaTeorica]): Future[Option[Boolean]] =
    daysOk.filter(_ != false).map(_ =>InstanceTerminale.operation().execQueryFilter(_.id, filter => filter.id.inSet(requests.map(_.terminaleId)))
      .collect{
        case Some(list) if list.size == requests.size => Some(true)
        case _ => Some(false)
      }).convert()

  private def verifyShift(requestOk: Option[Boolean], days: List[RequestGiorno]): Future[Option[Boolean]] =
    requestOk.filter(_ != false).map(_ => InstanceTurno.operation().execQueryFilter(_.id, filter => filter.id.inSet(days.map(_.shift)))
      .collect{
        case Some(list) if days.map(_.shift).forall(shift => list.contains(shift)) => Some(true)
        case _ => Some(false)
      }).convert()

  private def verify(daysOk: Option[Boolean], requestOk: Option[Boolean], shiftOk: Option[Boolean]): Option[Int] = (daysOk, requestOk, shiftOk) match {
    case (Some(true), Some(true), Some(true)) => Some(SUCCES_CODE)
    case (Some(false), _, _) => Some(ERROR_CODE5)
    case (_, Some(false), _) => Some(ERROR_CODE4)
    case (_, _, Some(false)) => Some(ERROR_CODE6)
  }

  override def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] =
    for{
      allInsert<-insertAll(requests)
      insertDay<-insertDay(days,allInsert)
    }yield insertDay

  private def selectGiornoId(day:RequestGiorno): Future[Option[Int]] =
    InstanceGiorno.operation().execQueryFilter(_.id,
      giorno => giorno.idGiornoSettimana === day.day.idGiornoSettimana && giorno.quantita===day.day.quantita)
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


  val t = Option(List.empty)
  RichiestaTeoricaOperation.controlInfo(requests,days) onComplete{
    case Success(value)=>println("//////////////////////////" + value)
    case Failure(exception) => println(exception)
  }
  while(true){}
}