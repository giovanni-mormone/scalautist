package dbfactory.operation
import java.sql.Date
import java.util.Calendar

import caseclass.CaseClassDB.Stipendio
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstancePresenza
import dbfactory.implicitOperation.OperationCrud
import promise.PromiseFactory
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


/** @author Giovanni Mormone
 *  Trait which allows to perform operations on the stipendio table.
 */
trait StipendioOperation extends OperationCrud[Stipendio]{

  def calculateStipendi(date: Date): Future[Option[Int]]
}

object StipendioOperation extends StipendioOperation{
  override def calculateStipendi(date: Date): Future[Option[Int]] = {
    val promise = PromiseFactory.intPromise()
    val calendarForDate = Calendar.getInstance()
    calendarForDate.setTime(date)
    calendarForDate.set(Calendar.DAY_OF_MONTH,1)
    val d:Date = new Date(calendarForDate.getTimeInMillis)

    InstancePresenza.operation().selectFilter(f => f.data > d && f.data<date).onComplete{
      case Success(value) => println("Successo?? ----- "+value)
      case Failure(exception) => println("FAllitooooo-----"+exception)
    }

    promise.future
  }
}
