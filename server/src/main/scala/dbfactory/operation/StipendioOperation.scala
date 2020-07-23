package dbfactory.operation
import java.sql.Date

import caseclass.CaseClassDB.{Assenza, Presenza, Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{InfoAssenza, InfoPresenza, InfoValorePresenza, StipendioInformations}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePresenza, InstanceStipendio, InstanceTurno}
import dbfactory.implicitOperation.OperationCrud
import messagecodes.StatusCodes
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/** @author Giovanni Mormone
 *  Trait which allows to perform operations on the stipendio table.
 */
trait StipendioOperation extends OperationCrud[Stipendio]{

  /**
   * Method to compute all stipendi of all conducenti in the month of the date given as input.
   * It can only run if there is at least a [[caseclass.CaseClassDB.Presenza]] for the last day of the month.
   * @param date
   *             The date used to compute the stipendi.
   * @return
   *         A [[messagecodes.StatusCodes.SUCCES_CODE]] if the stipendi are computed with succes or code messages:
   *         [[messagecodes.StatusCodes.ERROR_CODE1]] if stipendi have been already computed for the month provided in input
   *         [[messagecodes.StatusCodes.ERROR_CODE2]] if the month cannot be calculated, i.e. there is not a [[caseclass.CaseClassDB.Presenza]]
   *                                                  for the last day of the month.
   *         [[messagecodes.StatusCodes.ERROR_CODE3]] if it cannot calculate the stipendi
   *
   *
   */
  def calculateStipendi(date: Date): Future[Option[Int]]

  /**
   * Gets all stipendi for a specific persona, None if not found.
   * @param idPersona
   *                  The persona to find.
   * @return
   */
  def getstipendiForPersona(idPersona: Int): Future[Option[List[Stipendio]]]

  /**
   * Gets the informations of the stipendio with the given id.
   * @param idStipendio
   *                    The id of the stipendio.
   * @return
   *         A [[caseclass.CaseClassHttpMessage.StipendioInformations]] or None if there is not that Stipendio
   */
  def getStipendioInformations(idStipendio: Int): Future[Option[StipendioInformations]]
}

object StipendioOperation extends StipendioOperation {
  import utils.DateConverter._

  private val DEFAULT_PAGA: Double = 8
  private val MUL_STRAORDINARIO: Double = 1.5
  implicit private val STANDARD_MUL: Double = 1

  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    for {
      stipendi <- InstanceStipendio.operation().selectFilter(filter => filter.data >= startMonthDate(date) && filter.data < nextMonthDate(date))
      endOfMonth <- InstancePresenza.operation().selectFilter(filter => filter.data === endOfMonth(date))
      result <- if (stipendi.isDefined) Future.successful(Some(StatusCodes.ERROR_CODE1)) else if (endOfMonth.isEmpty) Future.successful(Some(StatusCodes.ERROR_CODE2)) else insertStipendiInDB(date)
    } yield result
  }

  private def insertStipendiInDB(date: Date): Future[Option[Int]] =
    createStipendi(date).flatMap {
      case None => Future.successful(Some(StatusCodes.ERROR_CODE3))
      case Some(stipendi) => insertAllBatch(stipendi).collect {
        case None => Some(StatusCodes.ERROR_CODE3)
        case _ => Some(StatusCodes.SUCCES_CODE)
      }
    }

  override def getstipendiForPersona(idPersona: Int): Future[Option[List[Stipendio]]] = {
    InstanceStipendio.operation().selectFilter(f => f.personaId === idPersona)
  }

  override def getStipendioInformations(idStipendio: Int): Future[Option[StipendioInformations]] ={
    select(idStipendio).flatMap{
      case None => Future.successful(None)
      case Some(stipendio) =>
        for{
          assenza <- InstanceAssenza.operation().selectFilter(f => f.personaId === stipendio.personaId && f.dataInizio >= startMonthDate(stipendio.data) && f.dataFine < nextMonthDate(endOfMonth(stipendio.data)))
          presenze <- InstancePresenza.operation().selectFilter(f => f.personeId === stipendio.personaId && f.data >= startMonthDate(stipendio.data) && f.data < nextMonthDate(endOfMonth(stipendio.data)))
          turniPresenze <- InstanceTurno.operation().selectFilter(f => f.id.inSet(presenze.toList.flatMap(_.map(_.turnoId))))
          infoPresenze <- getInfoPresenze(presenze,turniPresenze)
          infoValore <- getInfoValore(presenze,turniPresenze)
          infoAssenza <- getInfoAssenza(assenza)
        }yield {
          Some(StipendioInformations(infoPresenze,infoValore,infoAssenza))
        }
    }
  }

  private def getInfoPresenze(presenze:Option[List[Presenza]], turni:Option[List[Turno]]):Future[List[InfoPresenza]]={
    Future.successful(presenze.toList.flatMap(_.flatMap(x => turni.flatMap(_.find(_.id.contains(x.turnoId))
      .map(turno => InfoPresenza(turno.paga * multiplier(x.isStraordinario),turno.fasciaOraria,turno.nomeTurno,x.data,x.isStraordinario))))))
  }

  private def getInfoValore(presenze:Option[List[Presenza]], turni:Option[List[Turno]]):Future[InfoValorePresenza]={
    val moneyMap = presenze.toList.flatten.groupBy(_.isStraordinario)
      .map(t => t._1 -> t._2.flatMap(x => turni.flatMap(_.find(_.id.contains(x.turnoId))
        .map(t => t.paga * multiplier(x.isStraordinario)))).sum)
    val isStraordinario = true
    Future.successful(InfoValorePresenza(presenze.toList.flatten.groupBy(_.data).keys.size,
                                          moneyMap.getOrElse(isStraordinario,0.0),moneyMap.getOrElse(!isStraordinario,0.0)))
  }

  private def getInfoAssenza(assenze:Option[List[Assenza]]):Future[InfoAssenza]={
    val totalDays: (Int,Assenza) => Int = (x,ass) => x + computeDaysBetweenDates(ass.dataInizio,ass.dataFine)
    val daysMap = assenze.toList.flatten.groupBy(malattia => malattia.malattia).map(t => t._1 -> t._2.foldLeft(0)(totalDays))
    val malattia = true
    Future.successful(InfoAssenza(daysMap.getOrElse(!malattia,0),daysMap.getOrElse(malattia,0)))
  }

  private def createStipendi(date: Date): Future[Option[List[Stipendio]]] = {
    for{
      turni <- TurnoOperation.selectAll
      presenze <- InstancePresenza.operation().execQueryFilter(c => (c.personeId,c.turnoId,c.isStraordinario),
                                                                f => f.data >= startMonthDate(date) && f.data < nextMonthDate(date))
      stipendi <- calculateMoney(presenze,turni,date)
    }yield stipendi
  }

  private def calculateMoney(presenzeList: Option[List[(Int,Int,Boolean)]],turni:Option[List[Turno]],date:Date): Future[Option[List[Stipendio]]] = {
    val sumStipendi:(Map[Int,Double],(Int,Double)) => Map[Int,Double] = (map,couple) => map.updated(couple._1,couple._2 + map.getOrElse(couple._1,0.0))
    Future.successful(stipendi(presenzeList.map(_.map(x => x._1 -> pagaTurno(turni,x._2,x._3)).foldLeft(Map[Int,Double]())(sumStipendi)),date))
  }

  private val pagaTurno: (Option[List[Turno]],Int,Boolean) => Double = (turni,idTurno,straordinario) =>
    turni.flatMap(_.find(_.id.contains(idTurno))).map(x => x.paga * multiplier(straordinario)).getOrElse(DEFAULT_PAGA)

  private val multiplier: Boolean => Double = mul => if (mul) MUL_STRAORDINARIO else STANDARD_MUL

  private def stipendi(soldi:Option[Map[Int,Double]],date:Date):Option[List[Stipendio]] = {
    soldi.map(_.map(x => Stipendio(x._1,x._2,date)).toList)
  }
}