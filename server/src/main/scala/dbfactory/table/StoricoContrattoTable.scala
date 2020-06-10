package dbfactory.table
import java.sql.Date

import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import dbfactory.table.ContrattoTable.ContrattoTableRep
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Contratto, Persona, StoricoContratto, Turno}

object StoricoContrattoTable {
  class StoricoContrattoTableRep(tag: Tag) extends GenericTable[StoricoContratto](tag, "StoricoContrattoSets","IdStoricoContratto") {
    def dataInizio: Rep[Date] = column[Date]("DataInizio")
    def dataFine: Rep[Date] = column[Date]("DataFine")
    def personaId: Rep[Int] = column[Int]("Persone_Matricola")
    def contrattoId: Rep[Int] = column[Int]("Contrato_IdContratto")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    def turnoId1: Rep[Int] = column[Int]("Turno1_IdTurno")
    override def * : ProvenShape[StoricoContratto] = (dataInizio,dataFine.?,personaId.?,contrattoId,turnoId.?,turnoId1.?,id.?).mapTo[StoricoContratto]
    def persona: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("Persone_Matricola", personaId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
    def contratto: ForeignKeyQuery[ContrattoTableRep, Contratto] = foreignKey("Contrato_IdContratto", personaId, TableQuery[ContrattoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
    def turno2: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno1_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)

  }
}
