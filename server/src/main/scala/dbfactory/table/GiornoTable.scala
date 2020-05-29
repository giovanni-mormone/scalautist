package dbfactory.table
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Giorno

object GiornoTable{
  class GiornoTableRep(tag: Tag) extends GenericTable[Giorno](tag, "GiornoSets","IdGiorno"){
    def quantita: Rep[Int] = column[Int]("Quantita")
    def nomeGiorno: Rep[String] = column[String]("NomeGiorno")
    // Every table needs a * projection with the same type as the table's type parameter
    override def * : ProvenShape[Giorno] = ( quantita,nomeGiorno,id.?).mapTo[Giorno]
  }
}
