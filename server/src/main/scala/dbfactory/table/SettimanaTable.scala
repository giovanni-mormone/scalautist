package dbfactory.table
import dbfactory.table.ParametroTable.ParametroTableRep
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Settimana,Parametro}
object SettimanaTable {
  class SettimanaTableRep(tag: Tag) extends GenericTable[Settimana](tag, "SettimaneSet","IdSettimane") {
    def parametriSetId: Rep[Int] = column[Int]("ParametriSetIdParametri")
    override def * : ProvenShape[Settimana] = (parametriSetId,id.?).mapTo[Settimana]
    def parametriSet: ForeignKeyQuery[ParametroTableRep, Parametro] = foreignKey("ParametriSetIdParametri", parametriSetId, TableQuery[ParametroTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
}