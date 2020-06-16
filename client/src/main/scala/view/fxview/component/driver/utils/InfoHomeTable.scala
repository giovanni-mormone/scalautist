package view.fxview.component.driver.utils

import caseclass.CaseClassDB.Turno
import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

class InfoHomeTable(orarioAux: String, turnoAux: String) extends TableArgument{
  var orario = new SimpleStringProperty(orarioAux)
  var turno = new SimpleStringProperty(turnoAux)

  def getOrario: String = orario.get
  def getTurno: String = turno.get

  def setOrario(v: String): Unit = orario.set(v)
  def setTurno(v: String): Unit = turno.set(v)
}

/**
 * @author Fabian Aspee
 *
 * Object contains Implicit conversion from InfoHome->[[caseclass.CaseClassHttpMessage.InfoHome]] to
 * InfoHomeTable thant contains only shift in the day
 */
object InfoHomeTable {

  implicit def StipendioToStipendio(info: Turno): InfoHomeTable =
    getInfo(info)

  implicit def ListFerieToListFerieTable(info: List[Turno]): List[InfoHomeTable] =
    info.map(salary =>getInfo(salary))

  private def getInfo(turno:Turno)=new InfoHomeTable(turno.fasciaOraria,turno.nomeTurno)

}
