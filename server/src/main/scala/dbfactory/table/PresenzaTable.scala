package dbfactory.table
import java.sql.Date
import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Presenza, Persona, Turno}
object PresenzaTable {
  class PresenzaTableRep(tag: Tag) extends GenericTable[Presenza](tag, "PresenzaSets","IdPresenza") {
    def data: Rep[Date] = column[Date]("Data")
    def personeId: Rep[Int] = column[Int]("Persone_Matricola")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    def isStraordinario: Rep[Boolean] = column[Boolean]("IsStraordinario")
    override def * : ProvenShape[Presenza] = (data,personeId,turnoId,isStraordinario,id.?).mapTo[Presenza]
    def persone: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("Persone_Matricola", personeId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}
