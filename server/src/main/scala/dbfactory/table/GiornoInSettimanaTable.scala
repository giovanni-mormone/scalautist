package dbfactory.table
import caseclass.CaseClassDB.{Giorno, GiornoInSettimana, Parametro, Regola, Turno}
import dbfactory.setting.GenericTable
import dbfactory.table.GiornoTable.GiornoTableRep
import dbfactory.table.ParametroTable.ParametroTableRep
import dbfactory.table.RegolaTable.RegolaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

object GiornoInSettimanaTable {
  class GiornoInSettimanaTableRep(tag: Tag) extends GenericTable[GiornoInSettimana](tag, "GiornoInSettimanaSets","IdSettimana") {
    def giornoId: Rep[Int] = column[Int]("GiornoIdGiorno")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    def parametriId: Rep[Int] = column[Int]("ParametriSetIdParametri")
    def regolaId: Rep[Int] = column[Int]("RegolaSetIdRegola")
    override def * : ProvenShape[GiornoInSettimana] = (giornoId,turnoId,regolaId,parametriId.?,id.?).mapTo[GiornoInSettimana]
    def giorno: ForeignKeyQuery[GiornoTableRep, Giorno] = foreignKey("GiornoIdGiorno", giornoId, TableQuery[GiornoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def parametri: ForeignKeyQuery[ParametroTableRep, Parametro] = foreignKey("ParametriSetIdParametri", parametriId, TableQuery[ParametroTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def regola: ForeignKeyQuery[RegolaTableRep, Regola] = foreignKey("RegolaSetIdRegola", regolaId, TableQuery[RegolaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}
