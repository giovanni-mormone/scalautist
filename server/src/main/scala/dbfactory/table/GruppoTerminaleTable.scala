package dbfactory.table
import dbfactory.setting.GenericTable
import dbfactory.table.ParametroTable.ParametroTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{GruppoTerminale, Parametro}
object GruppoTerminaleTable {
  class GruppoTerminaleTableRep(tag: Tag) extends GenericTable[GruppoTerminale](tag, "GruppiTerminaliSets","IdGruppoTerminale") {
    def parametriId: Rep[Int] = column[Int]("ParametriIdParametri")
    override def * : ProvenShape[GruppoTerminale] = (parametriId,id.?).mapTo[GruppoTerminale]
    def parametri: ForeignKeyQuery[ParametroTableRep, Parametro] = foreignKey("ParametriIdParametri", parametriId, TableQuery[ParametroTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}