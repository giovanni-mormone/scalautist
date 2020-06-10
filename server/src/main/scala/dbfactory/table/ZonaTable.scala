package dbfactory.table

import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Zona

object ZonaTable{
  class ZonaTableRep(tag: Tag) extends GenericTable[Zona](tag, "ZonaSets","IdZona") {
    def Zones: Rep[String] = column[String]("Zone")
    // Every table needs a * projection with the same type as the table's type parameter
    override def * : ProvenShape[Zona] = ( Zones,id.?).mapTo[Zona]
  }

}
