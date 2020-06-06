package dbfactory.operation
import java.sql.Date
import java.util.Calendar

import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstanceAssenza
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{AssenzaTableQuery, PersonaTableQuery}
import dbfactory.table.AssenzaTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AssenzaOperation extends OperationCrud[Assenza]{
  def getAllFerie(data: Int):Unit

}

object AssenzaOperation extends AssenzaOperation{

  private val dateFromYear: Int => Date = year => {
    var calendar = Calendar.getInstance()
    calendar.set(year,0,1)
    new Date(calendar.getTimeInMillis)
  }

  override def getAllFerie(data: Int): Unit ={
    val currentYear = dateFromYear(data)
    val nextYear = dateFromYear(data+1)
    val filterJoin = for{
      assenza <- AssenzaTableQuery.tableQuery()
      persona <- PersonaTableQuery.tableQuery()
      if assenza.personaId === persona.id && assenza.dataInizio < nextYear && assenza.dataFine < nextYear && !assenza.malattia
    }yield (assenza.dataFine,assenza.dataInizio,persona.id,persona.nome,persona.cognome)

    InstanceAssenza.operation().execJoin(filterJoin)
      .collect{
        case None => println("GEGUUUUUU");None
        case Some(value) => println("GESUUUUUUUU" + value)
      }

  }

  private def constructFerie(assenze: List[(Date,Date,Int,String,String)]): List[Ferie] = {
    assenze(9).
    List()
  }
}
