package dbfactory.operation
import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.{Persona, Presenza, Stipendio, Straordinario, Turno}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstancePresenza, InstanceStipendio, InstanceStraordinario}
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
   * Method to compute all stipendi of all conducenti in the month before the date given as input.
   * @param date
   *             The date used to compute the stipendi. It is a date of the month next to the month that is computed
   * @return
   *         A [[StatusCodes.SUCCES_CODE]] if the stipendi are computed with succes or code messages:
   *         [[StatusCodes.ERROR_CODE1]] if
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
}

object StipendioOperation extends StipendioOperation{
  private val PAGA_TURNO:Double = 32
  private val PAGA_NOTTE:Double = PAGA_TURNO * 1.3
  private val MUL_STRAORDINARIO:Double = 1.5
  implicit private val STANDARD_MUL:Double = 1

  private val startMonthDate: Date =>  Date = x  => {
    val calendar = Calendar.getInstance()
    calendar.setTime(x)
    calendar.set(Calendar.DAY_OF_MONTH,1)
    new Date(calendar.getTimeInMillis)
  }
  private val nextMonthDate: Date =>  Date = x  => {
    val calendar = Calendar.getInstance()
    calendar.setTime(x)
    calendar.add(Calendar.MONTH,1)
    calendar.set(Calendar.DAY_OF_MONTH,1)
    new Date(calendar.getTimeInMillis)
  }

  private def calculateStipenditest(date: Date): Future[Option[Int]] = {
    for{
      stipendi <- InstanceStipendio.operation().selectFilter(filter => filter.data >= startMonthDate(date) && filter.data < nextMonthDate(date))
      result <- if (stipendi.isDefined) Future.successful(Some(StatusCodes.ERROR_CODE1)) else Future.successful(Some(2))
    }yield result

  }

  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    createStipendi(date).flatMap(stipendi => {
      insertAll(stipendi.getOrElse(List())).collect{
        case Some(x) => Some(x.length)
        case _ => None
      }
    })
  }

  override def getstipendiForPersona(idPersona: Int): Future[Option[List[Stipendio]]] = {
    InstanceStipendio.operation().selectFilter(f => f.personaId === idPersona)
  }

  private def createStipendi(date: Date): Future[Option[List[Stipendio]]] = {
    for{
      turni <- TurnoOperation.selectAll
      straordinari <-InstanceStraordinario.operation().execQueryFilter(c => (c.personaId,c.turnoId),f => f.data < date /*&& f.data >= startMonthDate(date)*/)
      presenze <- InstancePresenza.operation()execQueryFilter (c => (c.personeId,c.turnoId),f => f.data < date /*&& f.data >= startMonthDate(date)*/ )
      stipendi <- calculateMoney(presenze,straordinari,turni,date)
    }yield stipendi
  }

  private def calculateMoney(presenzeList: Option[List[(Int,Int)]],straordinariList: Option[List[(Int,Int)]],turni:Option[List[Turno]],date:Date): Future[Option[List[Stipendio]]] = {
    var stip:Map[Int,Double] = Map()
    presenzeList.foreach(_.foreach(x => stip =  updateMoneyMap(stip,turni,x)))
    straordinariList.foreach(_.foreach(x => stip = updateMoneyMap(stip,turni,x)(MUL_STRAORDINARIO)))
    Future.successful(stipendi(Some(stip),date))
  }

  def updateMoneyMap(stip:Map[Int,Double],turni:Option[List[Turno]],presenza:(Int,Int))(implicit mul:Double): Map[Int,Double] = {
    stip.updated(presenza._1,stip.getOrElse(presenza._1,0.0) + turnoNotturno(turni)(presenza._2) * mul)
  }

  private val turnoNotturno: Option[List[Turno]] => Int => Double = turni => idTurno=>
    if(turni.exists(_.exists(turno => turno.id.contains(idTurno) && turno.notturno)))  PAGA_NOTTE else PAGA_TURNO

  private def stipendi(soldi:Option[Map[Int,Double]],date:Date):Option[List[Stipendio]] = {
    soldi.map(_.map(x => Stipendio(x._1,x._2,date)).toList)
  }
}
