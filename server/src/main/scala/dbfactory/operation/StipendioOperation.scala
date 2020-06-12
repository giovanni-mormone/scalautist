package dbfactory.operation
import java.sql.Date

import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.StipendioInformations
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceAssenza, InstancePresenza, InstanceStipendio, InstanceStraordinario}
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
   *         for the last day of the month.
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
   *         A [[caseclass.CaseClassHttpMessage.StipendioInformations]]
   */
  def getStipendioInformations(idStipendio: Int): Future[Option[StipendioInformations]]
}

object StipendioOperation extends StipendioOperation{
  import utils.DateConverter._
  private val PAGA_TURNO:Double = 32
  private val PAGA_NOTTE:Double = PAGA_TURNO * 1.3
  private val MUL_STRAORDINARIO:Double = 1.5
  implicit private val STANDARD_MUL:Double = 1

  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    for{
      stipendi <- InstanceStipendio.operation().selectFilter(filter => filter.data >= startMonthDate(date) && filter.data < nextMonthDate(date))
      endOfMonth <- InstancePresenza.operation().selectFilter(filter => filter.data === endOfMonth(date))
      result <- if (stipendi.isDefined) Future.successful(Some(StatusCodes.ERROR_CODE1)) else
        if (endOfMonth.isEmpty) Future.successful(Some(StatusCodes.ERROR_CODE2)) else insertStipendiInDB(date)
    }yield result
  }

  private def insertStipendiInDB (date: Date):Future[Some[Int]] = {
    for{
      createStipendi <- createStipendi(date)
      insertStipendi <- if (createStipendi.isDefined) insertAll(createStipendi.head) else Future.successful(None)
    }yield if(insertStipendi.isDefined) Some(StatusCodes.SUCCES_CODE) else Some(StatusCodes.ERROR_CODE3)
  }

  override def getstipendiForPersona(idPersona: Int): Future[Option[List[Stipendio]]] = {
    InstanceStipendio.operation().selectFilter(f => f.personaId === idPersona)
  }

  override def getStipendioInformations(idStipendio: Int): Future[Option[StipendioInformations]] =
    select(idStipendio).flatMap{
      case None => Future.successful(None)
      case Some(stipendio) =>
        for{
          assenze <- InstanceAssenza.operation().selectFilter(f => f.personaId === stipendio.personaId && f.dataInizio >= stipendio.data && f.dataFine < nextMonthDate(stipendio.data))
          presenze <- InstancePresenza.operation().selectFilter(f => f.personeId === stipendio.personaId && f.data >= stipendio.data && f.data < nextMonthDate(stipendio.data))
          straordinari <- InstanceStraordinario.operation().selectFilter(f => f.personaId === stipendio.personaId && f.data >= stipendio.data && f.data < nextMonthDate(stipendio.data))
        }yield presenze.map(x => StipendioInformations(assenze,x,straordinari,stipendio))
    }

  private def createStipendi(date: Date): Future[Option[List[Stipendio]]] = {
    for{
      turni <- TurnoOperation.selectAll
      straordinari <-InstanceStraordinario.operation().execQueryFilter(c => (c.personaId,c.turnoId),f => f.data >= startMonthDate(date) && f.data < nextMonthDate(date))
      presenze <- InstancePresenza.operation()execQueryFilter (c => (c.personeId,c.turnoId),f => f.data >= startMonthDate(date) && f.data < nextMonthDate(date))
      stipendi <- calculateMoney(presenze,straordinari,turni,date)
    }yield stipendi
  }

  private def calculateMoney(presenzeList: Option[List[(Int,Int)]],straordinariList: Option[List[(Int,Int)]],turni:Option[List[Turno]],date:Date): Future[Option[List[Stipendio]]] = {
    var stip:Map[Int,Double] = Map()
    presenzeList.foreach(_.foreach(x => stip =  updateMoneyMap(stip,turni,x)))
    straordinariList.foreach(_.foreach(x => stip = updateMoneyMap(stip,turni,x)(MUL_STRAORDINARIO)))
    Future.successful(stipendi(Some(stip),date))
  }

  private def updateMoneyMap(stip:Map[Int,Double],turni:Option[List[Turno]],presenza:(Int,Int))(implicit mul:Double): Map[Int,Double] = {
    stip.updated(presenza._1,stip.getOrElse(presenza._1,0.0) + turnoNotturno(turni)(presenza._2) * mul)
  }

  private val turnoNotturno: Option[List[Turno]] => Int => Double = turni => idTurno=>
    if(turni.exists(_.exists(turno => turno.id.contains(idTurno) && turno.notturno)))  PAGA_NOTTE else PAGA_TURNO

  private def stipendi(soldi:Option[Map[Int,Double]],date:Date):Option[List[Stipendio]] = {
    soldi.map(_.map(x => Stipendio(x._1,x._2,date)).toList)
  }
}