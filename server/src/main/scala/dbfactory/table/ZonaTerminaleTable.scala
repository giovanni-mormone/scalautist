package dbfactory.table

import caseclass.CaseClassDB.{Parametro, ZonaTerminale}
import dbfactory.setting.GenericTable
import dbfactory.table.ParametroTable.ParametroTableRep
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.{ForeignKeyQuery, ProvenShape}
object ZonaTerminaleTable {
  class ZonaTerminaleTableRep(tag: Tag) extends GenericTable[ZonaTerminale](tag, "ZonaTerminaleSets","IdZonaTerminale") {
    def zona: Rep[Int] = column[Int]("Zona")
    def terminale:Rep[Int]=column[Int]("Terminale")
    def parametriId:Rep[Int]=column[Int]("ParametriSetIdParametri")
    // Every table needs a * projection with the same type as the table's type parameter
    override def * : ProvenShape[ZonaTerminale] = (zona,terminale,parametriId.?,id.?).mapTo[ZonaTerminale]
    def parametri: ForeignKeyQuery[ParametroTableRep, Parametro] = foreignKey("ParametriSetIdParametri", parametriId, TableQuery[ParametroTableRep])(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

  }
}