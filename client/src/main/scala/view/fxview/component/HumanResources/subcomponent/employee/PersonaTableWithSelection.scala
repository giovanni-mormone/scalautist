package view.fxview.component.HumanResources.subcomponent.employee

import caseclass.CaseClassDB.Persona
import javafx.scene.control.CheckBox

/**
 * @author Francesco Cassano
 *
 * Class to draw people in table with checkBox
 *
 * @param idp
 *            String id
 * @param namep
 *              String name
 * @param surnamep
 *                 String surname
 */
class PersonaTableWithSelection(idp: String, namep: String, surnamep: String)
  extends PersonaTable(idp, namep, surnamep){

  var selected: CheckBox = new CheckBox()

  def getSelected: CheckBox = selected
  def setSelected(v: CheckBox): Unit = selected = v
  def isSelected: Boolean = selected.isSelected
}

/**
 * @author Francesco Cassano
 *
 * Object contains Implicit conversion from persona to persona table
 */
object PersonaTableWithSelection {

  implicit def PersonaToPersonaTable(person: Persona): PersonaTableWithSelection =
    new PersonaTableWithSelection(person.matricola.head.toString, person.nome, person.cognome)

  implicit def ListPersonaToListPersonaTable(peopleList: List[Persona]) =
    peopleList.map(person => new PersonaTableWithSelection(person.matricola.head.toString, person.nome, person.cognome))
}