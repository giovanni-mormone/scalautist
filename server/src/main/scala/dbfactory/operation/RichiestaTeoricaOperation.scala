package dbfactory.operation

import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceRichiesta, InstanceRichiestaTeorica}
import caseclass.CaseClassDB.{Richiesta, RichiestaTeorica}
import caseclass.CaseClassHttpMessage.RequestGiorno
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceGiorno, InstanceTerminale, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.util.Helper._
import messagecodes.StatusCodes._
import dbfactory.setting.Table.{GiornoTableQuery, RichiestaTableQuery}
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._
import utils.DateConverter._

import scala.annotation.nowarn
import scala.concurrent.Future

/**
 * @author Giovanni Mormone, Fabian Aspee, Francesco Cassano
 *         Allows to perform operation on RichiestaTeoricaSet table
 */
trait RichiestaTeoricaOperation extends OperationCrud[RichiestaTeorica] {

  /**
   * Control that the information passed are good for the query
   *
   * @param requests List of [[caseclass.CaseClassDB.RichiestaTeorica]]
   * @param days     List of [[caseclass.CaseClassHttpMessage.RequestGiorno]]
   * @return Operation result code:
   *         [[ERROR_CODE10]]: Error in [[caseclass.CaseClassDB.RichiestaTeorica]] set
   *         [[ERROR_CODE11]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some days in the set don't exist
   *         [[ERROR_CODE12]]: Error in [[caseclass.CaseClassHttpMessage.RequestGiorno]], some shifts in the set don't exist
   */
  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]]

  /**
   * Save info about a new theoretical request. It takes a list of [[caseclass.CaseClassDB.RichiestaTeorica]], representing a group of
   * requests that shares the same dates but for different terminals, and a List of [[caseclass.CaseClassHttpMessage.RequestGiorno]], representing
   * the quantity of driver for each day and shift associated to each of the RichiestaTeorica.
   * For each [[caseclass.CaseClassDB.RichiestaTeorica]] it inserts the data if there is not a request for the same terminal in the same dates.
   * If there is some date that overlaps it takes the old request and changes the date of start or end if they overlap only for a period of time;
   * if the old request is included in the new, it is deleted and if the old request has the same dates of the new, all the [[caseclass.CaseClassDB.Richiesta]]
   * are updated. There is a limit on the date that are provided to each [[caseclass.CaseClassDB.RichiestaTeorica]]: The start date must the first day
   * of a month and the end date must be the last day of a month; the end date must be after the start; additionally, every request to insert must be
   * associated to the same period.
   *
   * @param requests List of [[caseclass.CaseClassDB.RichiestaTeorica]]
   * @param days     List of [[caseclass.CaseClassHttpMessage.RequestGiorno]]
   * @return
   * Future of Int that represent status of operation:
   * [[messagecodes.StatusCodes.SUCCES_CODE]] if the operation is successful
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the conditions on the dates are not respected
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the RichiesteTeoriche to insert are None
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if it fails the insert of [[caseclass.CaseClassDB.Richiesta]]
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the id of richiesta teorica are None
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the days to insert are None
   * [[messagecodes.StatusCodes.ERROR_CODE6]] if it's been asked to update some RichiestaTeorica but it not finds the old RichiestaTeorica in the db
   * [[messagecodes.StatusCodes.ERROR_CODE7]] if it's been asked to update some RichiestaTeorica but it not finds the Richiesta associated to it
   * [[messagecodes.StatusCodes.ERROR_CODE8]] if it's been asked to update some RichiestaTeorica but it not finds the Giorno to associate to it
   * [[messagecodes.StatusCodes.ERROR_CODE9]] if there is some terminaleID duplicated in the insert request
   *
   */
  def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]]

  /**
   * method that allow select Giorno with quantity if this exist in another case this case class allow insert a new giorno in database
   * @param day case class that contains info for request to database in table giorno
   * @return id of giorno
   */
  def selectGiornoId(day: RequestGiorno): Future[Option[Int]]
}

object RichiestaTeoricaOperation extends RichiestaTeoricaOperation {

  private val WEEK: List[String] = List("Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica")
  private val MIN_IN_WEEK: Int = 1
  private val MAX_IN_WEEK: Int = 7

  def controlInfo(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {
    if(requests.isEmpty) return Future.successful(Some(ERROR_CODE10))
    if(days.isEmpty) return Future.successful(Some(ERROR_CODE11))

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

  @nowarn
  private def verify(daysOk: Option[Boolean], requestOk: Option[Boolean], shiftOk: Option[Boolean]): Option[Int] = (daysOk, requestOk, shiftOk) match {
    case (Some(true), Some(true), Some(true)) => Some(SUCCES_CODE)
    case (Some(false), _, _) => Some(ERROR_CODE11)
    case (_, Some(false), _) => Some(ERROR_CODE10)
    case (_, _, Some(false)) => Some(ERROR_CODE12)
  }

  override def saveRichiestaTeorica(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {
    if(requests.forall(request => startMonthDate(request.dataInizio).compareTo(request.dataInizio) == 0
      && endOfMonth(request.dataFine).compareTo(request.dataFine) == 0
      && request.dataInizio.compareTo(request.dataFine) < 0)
      && requests.forall(x => requests.headOption.exists(m => m.dataInizio == x.dataInizio && m.dataFine == x.dataFine))){
      if(requests.forall(x => requests.count(_.terminaleId == x.terminaleId) == 1)) {
        checkOverlappingDates(requests).flatMap{
          case None => Future.successful(Some(StatusCodes.ERROR_CODE2))
          case Some((::(head,tail),List())) => insertRequests(::(head,tail),days)
          case Some((List(),x)) => updateRequest(x,days)
          case Some(x) =>
            insertRequests(x._1,days)
            updateRequest(x._2,days)
        }
      } else Future.successful(Some(StatusCodes.ERROR_CODE9))

    } else Future.successful(Some(StatusCodes.ERROR_CODE1))
  }

  private def updateRequest(requests: List[RichiestaTeorica], days: List[RequestGiorno]): Future[Option[Int]] = {

    def _updateRequest(requests: List[RichiestaTeorica], days: List[RequestGiorno], result: Future[Option[Int]] = Future.successful(None)): Future[Option[Int]] = requests match {
      case ::(head, next) => _updateDays(head,days).flatMap{
        case None => _updateRequest(next,days, result)
        case x => Future.successful(x)
      }
      case Nil => result.flatMap{
        case None => Future.successful(Some(StatusCodes.SUCCES_CODE))
        case x => Future.successful(x)
      }
    }

    def _updateDays(richiesta: RichiestaTeorica, days: List[RequestGiorno], result: Future[Option[Int]] = Future.successful(None)): Future[Option[Int]] = days match {
      case ::(head, next) => updateGiorno(richiesta,head).flatMap{
        case None => _updateDays(richiesta,next,result)
        case x => Future.successful(x)
      }
      case Nil => result
    }

    _updateRequest(requests,days)
  }

  private def updateGiorno(teorica: RichiestaTeorica, giorno: RequestGiorno):Future[Option[Int]] = {

    InstanceRichiestaTeorica.operation().execQueryFilter(field => field.id,
      filter=> filter.dataInizio === teorica.dataInizio && filter.dataFine === teorica.dataFine && filter.terminaleId === teorica.terminaleId).flatMap {
      case Some(List(idRichiestaTeorica)) =>
        val filterJoin = for{
          richiesta <- RichiestaTableQuery.tableQuery()
          giorn <- GiornoTableQuery.tableQuery()
          if richiesta.richiestaTeoricaId === idRichiestaTeorica && richiesta.turnoId === giorno.shift && richiesta.giornoId === giorn.id && giorn.idGiornoSettimana === giorno.day.idGiornoSettimana
        }yield richiesta.id
        InstanceRichiesta.operation().execJoin(filterJoin).flatMap{
          case Some(List(idRichiesta)) => selectGiornoId(giorno).flatMap{
            case Some(giornoId) => RichiestaOperation.update(Richiesta(giorno.shift,giornoId,idRichiestaTeorica,Some(idRichiesta)))
            case _ => Future.successful(Some(StatusCodes.ERROR_CODE8))
          }
          case _ => Future.successful(Some(StatusCodes.ERROR_CODE7))
        }
      case _ => Future.successful(Some(StatusCodes.ERROR_CODE6))
    }
  }


  private def checkOverlappingDates(richiesta: List[RichiestaTeorica]):Future[Option[(List[RichiestaTeorica],List[RichiestaTeorica])]] =
    richiesta match {
      case ::(head, _) => InstanceRichiestaTeorica.operation().selectFilter(filter => filter.dataInizio <= head.dataFine &&
        filter.dataFine >= head.dataInizio && filter.terminaleId.inSet(richiesta.map(_.terminaleId))).flatMap {
        case Some(value) => Future.successful(checkOverlappingRequests(value,richiesta))
        case None => Future.successful(Some(richiesta,List()))
      }
      case Nil =>Future.successful(None)
    }


  private def insertRequests(requests: List[RichiestaTeorica], days: List[RequestGiorno]):Future[Option[Int]] =
    for{
      allInsert<-insertAll(requests)
      insertDay<-insertDay(days,allInsert)
    }yield if (!insertDay.contains(StatusCodes.SUCCES_CODE))  {allInsert.foreach(deleteAll); insertDay} else insertDay

  @nowarn//at this point req is always defined
  private def checkOverlappingRequests(overlappingRequests: List[RichiestaTeorica], toCompareList: List[RichiestaTeorica]):Option[(List[RichiestaTeorica],List[RichiestaTeorica])] = {
    @scala.annotation.tailrec
    def _checkOverlappingRequests(_overlappingRequests: List[RichiestaTeorica], toCompare: RichiestaTeorica, result: (List[RichiestaTeorica],List[RichiestaTeorica])=(List.empty,List.empty)): Option[(List[RichiestaTeorica],List[RichiestaTeorica])]= _overlappingRequests match {
      case ::(req, next) if req.dataInizio.compareTo(toCompare.dataInizio) < 0 =>
        update(RichiestaTeorica(req.dataInizio, endOfMonth(previousMonthDate(toCompare.dataInizio)), req.terminaleId, req.idRichiestaTeorica))
        _checkOverlappingRequests(next,toCompare,(toCompare.copy(terminaleId = req.terminaleId) :: result._1,result._2))
      case ::(req, next) if req.dataFine.compareTo(toCompare.dataFine) > 0  =>
        update(RichiestaTeorica(nextMonthDate(toCompare.dataFine), req.dataFine, req.terminaleId, req.idRichiestaTeorica))
        _checkOverlappingRequests(next,toCompare,(toCompare.copy(terminaleId = req.terminaleId) :: result._1,result._2))
      case ::(req, next) if req.dataInizio.compareTo(toCompare.dataInizio) ==0 && req.dataFine.compareTo(toCompare.dataFine) ==0 =>  _checkOverlappingRequests(next,toCompare, (result._1,toCompare.copy(terminaleId = req.terminaleId) :: result._2))
      case ::(req, next) if req.dataInizio.compareTo(toCompare.dataFine) <=0 && req.dataFine.compareTo(toCompare.dataInizio) >=0 =>
        delete(req.idRichiestaTeorica match{case Some(x) => x}) //il warn è riferito a me
        _checkOverlappingRequests(next,toCompare, (toCompare.copy(terminaleId = req.terminaleId) :: result._1,result._2))
      case ::(req,next) => _checkOverlappingRequests(next,toCompare, (toCompare.copy(terminaleId = req.terminaleId) :: result._1,result._2))
      case Nil =>
        Some(toCompareList.filter(x => !overlappingRequests.exists(_.terminaleId==x.terminaleId)) ::: result._1.distinct,result._2)
    }
    _checkOverlappingRequests(overlappingRequests,toCompareList.head)
  }

  private def insertDay(days: List[RequestGiorno], idRT:Option[List[Int]]): Future[Option[Int]] = days match{
    case List(head) => selectGiornoId(head).flatMap(id => richiestaOperation(id,idRT,head))
    case head::tail => selectGiornoId(head).flatMap(id => richiestaOperation(id,idRT,head)).flatMap(_ => insertDay(tail,idRT))
  }

  override def selectGiornoId(day:RequestGiorno): Future[Option[Int]] =
    InstanceGiorno.operation().execQueryFilter(giorno=>giorno.id,
      giorno=>giorno.idGiornoSettimana === day.day.idGiornoSettimana && giorno.quantita===day.day.quantita)
      .flatMap {
        case Some(value) =>selectId(value)
        case None =>GiornoOperation.insert(day.day)
      }

  private def richiestaOperation(giorno: Option[Int], idRT: Option[List[Int]], day: RequestGiorno):Future[Option[Int]] = {

    @scala.annotation.tailrec
    def _richiestaOperation(idRT: Option[List[Int]], day: RequestGiorno, result: Future[Option[Int]] = Future.successful(None)): Future[Option[Int]] = (idRT,giorno) match {
      case (Some(idRichiestaTeorica::tail),Some(idGiorno)) => _richiestaOperation(Some(tail),day,RichiestaOperation.insert(Richiesta(day.shift, idGiorno, idRichiestaTeorica))
        .collect{
          case Some(_) => Some(StatusCodes.SUCCES_CODE)
          case None => Some(StatusCodes.ERROR_CODE3)
        })
      case (Some(List()),_) => result
      case (None,_) => Future.successful(Some(StatusCodes.ERROR_CODE4))
      case (_,None) => Future.successful(Some(StatusCodes.ERROR_CODE5))
    }
    _richiestaOperation(idRT,day)
  }

  private def selectId(value:List[Int]): Future[Option[Int]] =
    Future.successful(value match {
      case List(result) => Some(result)
      case Nil => None
    })
}
