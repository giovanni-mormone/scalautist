package dbfactory.table
import java.sql.Date
import dbfactory.setting.GenericTable
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Persona, Straordinario, Turno}
object StraordinarioTable {
  class StraordinarioTableRep(tag: Tag) extends GenericTable[Straordinario](tag, "StraordinariSets","IdStraordinari") {
    def data: Rep[Date] = column[Date]("Data")
    def personaId: Rep[Int] = column[Int]("Persone_Matricola")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    override def * : ProvenShape[Straordinario] = (data,personaId,turnoId,id.?).mapTo[Straordinario]
    def persona: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("Persone_Matricola", personaId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)

  }
}
