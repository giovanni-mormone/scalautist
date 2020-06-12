package dbfactory.table

import java.sql.Date

import caseclass.CaseClassDB.{Assenza, Persona}
import dbfactory.setting.GenericTable
import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

/**
 * @author Giovanni Mormone
 *
 * Encapsulate an instance of Assenza in the DB
 */
object AssenzaTable {

  class AssenzaTableRep(tag:Tag) extends GenericTable[Assenza](tag, "AssenzaSets","IdAssenza"){
    def dataInizio: Rep[Date] = column[Date]("DataInizio")
    def dataFine: Rep[Date] = column[Date]("DataFine")
    def malattia: Rep[Boolean] = column[Boolean]("IsMalattia")
    def personaId: Rep[Int] = column[Int]("PersoneSet_Matricola")
    override def * : ProvenShape[Assenza] = (personaId,dataInizio,dataFine,malattia,id.?).mapTo[Assenza]
    def persona: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("PersoneSet_Matricola", personaId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
  }
}
