package dbfactory.table

import dbfactory.setting.GenericTable
import dbfactory.table.GiornoTable.GiornoTableRep
import dbfactory.table.RichiestaTeoricaTable.RichiestaTeoricaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Giorno, Richiesta, RichiestaTeorica, Turno}
object RichiestaTable {
  class RichiestaTableRep(tag: Tag) extends GenericTable[Richiesta](tag, "RichiestaSets","IdRichiesta") {
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    def giornoId: Rep[Int] = column[Int]("Giorno_IdGiorno")
    def richiestaTeoricaId: Rep[Int] = column[Int]("RichiestaTeorica_IdRichiestaTeorica")
    override def * : ProvenShape[Richiesta] = (turnoId,giornoId,richiestaTeoricaId,id.?).mapTo[Richiesta]
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def giorno: ForeignKeyQuery[GiornoTableRep, Giorno] = foreignKey("Giorno_IdGiorno", giornoId, TableQuery[GiornoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def richiestaTeorica: ForeignKeyQuery[RichiestaTeoricaTableRep, RichiestaTeorica] = foreignKey("RichiestaTeorica_IdRichiestaTeorica", richiestaTeoricaId, TableQuery[RichiestaTeoricaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}