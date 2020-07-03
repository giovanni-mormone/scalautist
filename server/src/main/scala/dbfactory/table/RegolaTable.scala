package dbfactory.table
import caseclass.CaseClassDB.Regola
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape

object RegolaTable {
  class RegolaTableRep(tag: Tag) extends GenericTable[Regola](tag, "RegolaSets","IdRegola") {
    def nomeRegola: Rep[String] = column[String]("NomeRegola")
    override def * : ProvenShape[Regola] = (nomeRegola,id.?).mapTo[Regola]
  }
}
