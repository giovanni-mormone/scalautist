package view.fxview.component.HumanResources.subcomponent.util

import caseclass.CaseClassHttpMessage.Ferie
import javafx.beans.property.SimpleStringProperty

/**
 * @author Fabian Aspee
 *
 * Class to draw people in table
 * @param idp
 *            String id
 * @param namepSurname
 *              String comncat name and surname
 * @param holidays
 *                holiday of a person
 */
class FerieTable(idp: Int, namepSurname: String,holidays:Int) extends TableArgument {

  var id = new SimpleStringProperty(idp.toString)
  var nameSurname = new SimpleStringProperty(namepSurname)
  var holiday = new SimpleStringProperty(holidays.toString)

  def getId: Int = id.get.toInt
  def getNameSurname: String = nameSurname.get
  def getHoliday: Int = holiday.get.toInt

  def setId(v: String): Unit = id.set(v)
  def setNameSurname(v: String): Unit = nameSurname.set(v)
  def setHoliday(v: String): Unit = holiday.set(v)
}

/**
 * @author Fabian Aspee
 *
 * Object contains Implicit conversion from holiday to holiday table
 */
object FerieTable {

  implicit def FerieToFerieTable(person: Ferie): FerieTable =
    new FerieTable(person.idPersona, person.nomeCognome, person.giorniVacanza)

  implicit def ListFerieToListFerieTable(peopleList: List[Ferie]): List[FerieTable] =
    peopleList.map(person => new FerieTable(person.idPersona, person.nomeCognome, person.giorniVacanza))
}
