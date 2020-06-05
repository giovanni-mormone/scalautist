package dbfactory.operation
import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.{Presenza, Stipendio, Straordinario}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstancePresenza, InstanceStraordinario}
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
}

object StipendioOperation extends StipendioOperation{
  private val PAGA_TURNO = 32
  private val PAGA_NOTTE = PAGA_TURNO * 1.3
  private val MUL_STRAORDINARIO = 1.5

  private val startMonthDate: Date =>  Date = x  => {
    val calendar = Calendar.getInstance()
    calendar.setTime(x)
    calendar.set(Calendar.DAY_OF_MONTH,1)
    new Date(calendar.getTimeInMillis)
  }

  def fcalculateStipendi(date: Date): Future[(Map[Int,Double],Map[Int,Double])] = {
    for{
      r <-InstanceStraordinario.operation().selectFilter(f => f.data < date)
      tt <- calculateMoneyStraordinari(r)
      z <- InstancePresenza.operation().selectFilter(f => f.data < date)
      ttt <- calculateMoney(z)
    }yield (ttt,tt)
  }

  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    fcalculateStipendi(date).flatMap(x => {
      val stipList = stipendi(x._1, x._2,date)
      insertAll(stipList).collect {
        case Some(List()) => None
        case Some(x) => Some(x.length)
      }
    })
  }

  private def calculateMoneyStraordinari(straordinaris: Option[List[Straordinario]]): Future[Map[Int,Double]] = Future{
    println("STRAOOOOO     " +straordinaris)
    straordinaris match {
      case Some(List()) => Map()
      case Some(list) =>
        list.map(s => (s.personaId,s.turnoId)).groupBy(_._1).map(x => x._1 -> money(x._2))
    }
  }


  private def calculateMoney(presenze: Option[List[Presenza]]): Future[Map[Int,Double]] = Future{
    println("PRESEEEEE     " +presenze)
    presenze match {
      case Some(List()) => Map()
      case Some(list) =>
        list.map(s => (s.personaId,s.turnoId)).groupBy(_._1).map(x => x._1 -> money(x._2))
    }
  }


  private def stipendi(presenze:Map[Int,Double], straordinari:Map[Int,Double], date:Date):List[Stipendio] ={
    println("LISTTTT PREEEEE" + presenze)
    println("LISTTTT STRAOOOO" + straordinari)
    val s = presenze.map(p => Stipendio(p._1,p._2 + straordinari.getOrElse(p._1,0.0),date))
    s.toList
  }


  private def money(value: List[(Int, Int)]):Double = {
    println("MONEEEEEEE " + value)
    value.map(t => t._2).map(_ => PAGA_TURNO * MUL_STRAORDINARIO).sum
  }

}
