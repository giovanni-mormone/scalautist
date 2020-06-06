package dbfactory.operation
import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.{Persona, Presenza, Stipendio, Straordinario, Turno}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstancePresenza, InstanceStipendio, InstanceStraordinario}
import dbfactory.implicitOperation.OperationCrud
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/** @author Giovanni Mormone
 *  Trait which allows to perform operations on the stipendio table.
 */
trait StipendioOperation extends OperationCrud[Stipendio]{

  /**
   * Method to compute all stipendi of all conducenti in the month of the date given as input.
   * It computes the stipendi between the start of the month of the date provided and the date itself.
   * @param date
   *             The stop date of the stipendi to compute
   * @return
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

  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    createStipendi(date).flatMap(stipendi => {
      insertAll(stipendi.orElse(Some(List())).head).collect {
        case Some(List()) => None
        case Some(x) => Some(x.length)
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

  private def calculateMoney(presenzeList: Option[List[(Int,Int)]],straordinariList: Option[List[(Int,Int)]],turni:Option[List[Turno]],date:Date): Future[Option[List[Stipendio]]] = Future{
    var stip:Map[Int,Double] = Map()
    presenzeList.orElse(Some(List())).head.foreach(x => stip =  updateMoneyMap(stip,turni,x))
    straordinariList.orElse(Some(List())).head.foreach(x => stip = updateMoneyMap(stip,turni,x)( MUL_STRAORDINARIO))
    stipendi(Some(stip),date)
  }

  def updateMoneyMap(stip:Map[Int,Double],turni:Option[List[Turno]],presenza:(Int,Int))(implicit mul:Double): Map[Int,Double] = {
    stip.updated(presenza._1,stip.getOrElse(presenza._1,0.0) + turnoNotturno(turni)(presenza._2) * mul)
  }

  private val turnoNotturno: Option[List[Turno]] => Int => Double = turni => idTurno=>
    if(turni.orElse(Some(List())).head.exists(turno => turno.id.contains(idTurno) && turno.notturno)) PAGA_NOTTE else PAGA_TURNO

  private def stipendi(soldi:Option[Map[Int,Double]],date:Date):Option[List[Stipendio]] = {
    Option(soldi.orElse(Some(Map())).head.map(x => Stipendio(x._1,x._2,date)).toList)
  }
}
