package view.fxview.component.driver.utils

import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.TableArgument

class DisponibilitaTable(giornop: String, disponibilitap: Boolean) extends TableArgument{
  var giorno = new SimpleStringProperty(giornop)
  var disponibilita = new SimpleStringProperty(disponibilitap.toString)

  def getGiorno: String = giorno.get
  def getDisponibilita: String = disponibilita.get

  def setGiorno(v: String): Unit = giorno.set(v)
  def setDisponibilita(v: Boolean): Unit = disponibilita.set(v.toString)
}

/**
 * @author Fabian Aspee
 *
 * Object contains Implicit conversion from Disponibilita->[[caseclass.CaseClassDB.Disponibilita]] to
 * DisponibilitaTable thant contains only shift that the driver have in more in the week
 */
object DisponibilitaTable {

  implicit def StipendioToStipendio(disponibilita: (String,Boolean)): DisponibilitaTable =
    getDisponibilita(disponibilita)

  implicit def ListFerieToListFerieTable(disponibilita: List[(String,Boolean)]): List[DisponibilitaTable] =
    disponibilita.map(salary =>getDisponibilita(salary))

  private def getDisponibilita(disponibilita:(String,Boolean))=new DisponibilitaTable(disponibilita._1,disponibilita._2)

}
