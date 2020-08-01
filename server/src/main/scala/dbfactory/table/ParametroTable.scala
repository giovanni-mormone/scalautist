package dbfactory.table
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Parametro
object ParametroTable {
  class ParametroTableRep(tag: Tag) extends GenericTable[Parametro](tag, "ParametriSets","IdParametri") {
    def treSabato: Rep[Boolean] = column[Boolean]("TreSabato")
    def nome: Rep[String] = column[String]("NomeParametro")
    override def * : ProvenShape[Parametro] = (treSabato,nome,id.?).mapTo[Parametro]

  }
}
