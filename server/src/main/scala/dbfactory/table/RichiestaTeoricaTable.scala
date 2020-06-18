package dbfactory.table
import java.sql.Date

import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{RichiestaTeorica, Terminale, Zona}
import dbfactory.table.TerminaleTable.TerminaleTableRep
import dbfactory.table.ZonaTable.ZonaTableRep
object RichiestaTeoricaTable {
  class RichiestaTeoricaTableRep(tag: Tag) extends GenericTable[RichiestaTeorica](tag, "RichiestaTeoricaSets","IdRichiestaTeorica") {
    def dataInizio: Rep[Date] = column[Date]("DataInizio")
    def dataFine: Rep[Date] = column[Date]("DataFine")
    def terminaleId: Rep[Int] = column[Int]("TerminalSetIdTerminale")
    override def * : ProvenShape[RichiestaTeorica] = (dataInizio,dataFine.?,terminaleId,id.?).mapTo[RichiestaTeorica]
    def terminal: ForeignKeyQuery[TerminaleTableRep, Terminale] = foreignKey("TerminalSetIdTerminale", terminaleId,
      TableQuery[TerminaleTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}
