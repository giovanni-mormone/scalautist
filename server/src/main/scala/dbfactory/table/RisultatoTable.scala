package dbfactory.table
import java.sql.Date
import dbfactory.setting.GenericTable
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.table.TurnoTable.TurnoTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Persona, Risultato, Turno}
object RisultatoTable {
  class RisultatoTableRep(tag: Tag) extends GenericTable[Risultato](tag, "RisultatoSets","IdRisultato") {
    def data: Rep[Date] = column[Date]("Data")
    def personeId: Rep[Int] = column[Int]("Persone_Matricola")
    def turnoId: Rep[Int] = column[Int]("Turno_IdTurno")
    override def * : ProvenShape[Risultato] = (data,personeId,turnoId,id.?).mapTo[Risultato]
    def persone: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("Persone_Matricola", personeId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
    def turno: ForeignKeyQuery[TurnoTableRep, Turno] = foreignKey("Turno_IdTurno", turnoId, TableQuery[TurnoTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}