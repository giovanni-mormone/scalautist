package dbfactory.table
import dbfactory.setting.GenericTable
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Contratto
object ContrattoTable {
  class ContrattoTableRep(tag: Tag) extends GenericTable[Contratto](tag, "ContratoeSets","IdContratto") {
    def tipoContratto: Rep[String] = column[String]("TipoContratto")
    def turnoFisso: Rep[Byte] = column[Byte]("TurnoFisso")
    override def * : ProvenShape[Contratto] = (tipoContratto,turnoFisso,id.?).mapTo[Contratto]

  }
}
