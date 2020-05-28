package dbfactory.table
import dbfactory.setting.GenericTable
import dbfactory.table.GiornoTable.GiornoTableRep
import dbfactory.table.SettimanaTable.SettimanaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Giorno, GiornoInSettimana, Settimana, Turno}

object GiornoInSettimanaTable {
  class GiornoInSettimanaTableRep(tag: Tag) extends GenericTable[GiornoInSettimana](tag, "GiornoInSettimanaSets","IdSettimana") {
    def giornoId: Rep[Int] = column[Int]("GiornoIdGiorno")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    def parametriId: Rep[Int] = column[Int]("Parametri_IdParametri")
    def settimanaId: Rep[Int] = column[Int]("SettimaneIdSettimane")
    override def * : ProvenShape[GiornoInSettimana] = (giornoId,turnoId,parametriId,settimanaId,id.?).mapTo[GiornoInSettimana]
    def giorno: ForeignKeyQuery[GiornoTableRep, Giorno] = foreignKey("GiornoIdGiorno", giornoId, TableQuery[GiornoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def settimana: ForeignKeyQuery[SettimanaTableRep, Settimana] = foreignKey("SettimaneIdSettimane", settimanaId, TableQuery[SettimanaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}
