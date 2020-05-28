package dbfactory.table
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Parametro
object ParametroTable {
  class ParametroTableRep(tag: Tag) extends GenericTable[Parametro](tag, "ParametriSets","IdParametri") {
    def treSabato: Rep[Byte] = column[Byte]("TreSabato")
    def regola: Rep[String] = column[String]("Regola")
    override def * : ProvenShape[Parametro] = (treSabato,regola,id.?).mapTo[Parametro]

  }
}
