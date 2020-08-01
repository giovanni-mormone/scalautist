package dbfactory.table
import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import dbfactory.table.ZonaTable.ZonaTableRep
import slick.lifted.{ForeignKeyQuery, ProvenShape}
import caseclass.CaseClassDB.{Terminale, Zona}

object TerminaleTable{
  class TerminaleTableRep(tag: Tag) extends GenericTable[Terminale](tag, "TerminalSets","IdTerminale") {
    def zonaId: Rep[Int] = column[Int]("ZonaIdZona")
    def nomeTerminale: Rep[String] = column[String]("NomeTerminale")
    override def * : ProvenShape[Terminale] = (nomeTerminale,zonaId,id.?).mapTo[Terminale]
    def zona: ForeignKeyQuery[ZonaTableRep, Zona] = foreignKey("ZonaIdZona", zonaId, TableQuery[ZonaTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}
