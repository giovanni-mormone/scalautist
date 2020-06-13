package dbfactory.table
import java.sql.Date

import caseclass.CaseClassDB.{Persona, Stipendio}
import dbfactory.setting.GenericTable
import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}

object StipendioTable {

  class StipendioTableRep(tag:Tag) extends GenericTable[Stipendio](tag,"StipendioSets","IdStipendio"){
    def personaId: Rep[Int] = column[Int]("PersoneSet_Matricola")
    def data: Rep[Date] = column[Date]("Data")
    def valore: Rep[Double] = column[Double]("Valore")
    override def * : ProvenShape[Stipendio] =  (personaId,valore,data,id.?).mapTo[Stipendio]
    def persona: ForeignKeyQuery[PersonaTableRep, Persona] = foreignKey("PersoneSet_Matricola",personaId, TableQuery[PersonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.SetNull)
  }

}
