package dbfactory.table
import slick.jdbc.SQLServerProfile.api._
import dbfactory.setting.GenericTable
import slick.lifted.ProvenShape
import caseclass.CaseClassDB.Turno

object TurnoTable{
  class TurnoTableRep(tag: Tag) extends GenericTable[Turno](tag, "TurnoSets","IdTurno") {
    def nomeTurno: Rep[String] = column[String]("NomeTurno")
    def fasciaOraria: Rep[String] = column[String]("FasciaOraria")
    def notturno: Rep[Boolean] = column[Boolean]("Notturno")
    override def * : ProvenShape[Turno] = (nomeTurno, fasciaOraria,notturno,id.?).mapTo[Turno]
  }
}
