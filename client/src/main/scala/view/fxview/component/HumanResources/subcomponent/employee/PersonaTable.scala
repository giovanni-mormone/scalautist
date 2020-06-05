package view.fxview.component.HumanResources.subcomponent.employee

import caseclass.CaseClassDB.Persona
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.CheckBox

/**
 * @author Francesco Cassano
 *
 * Class to draw peoplr in table
 *
 * @param idp
 *            String id
 * @param namep
 *              String name
 * @param surnamep
 *                 String surname
 */
class PersonaTable(idp: String, namep: String, surnamep: String){

  var id = new SimpleStringProperty(idp)
  var name = new SimpleStringProperty(namep)
  var surname = new SimpleStringProperty(surnamep)
  //var contract = new SimpleStringProperty(contractp)
  var selected: CheckBox = new CheckBox()

  def getId: String = id.get
  def getName: String = name.get
  def getSurname: String = surname.get
  //def getContract: String = contract.get
  def getSelected: CheckBox = selected

  def setId(v: String) = id.set(v)
  def setName(v: String) = name.set(v)
  def setSurname(v: String) = surname.set(v)
  //def setContract(v: String) = contract.set(v)
  def setSelected(v: CheckBox) = selected = v

  def isSelected: Boolean = selected.isSelected

}

/**
 * @author Francesco Cassano
 *
 * Object contains Implicit conversion from persona to persona table
 */
object PersonaTable {

  implicit def PersonaToPersonaTable(person: Persona): PersonaTable =
    new PersonaTable(person.matricola.head.toString, person.nome, person.cognome)

  implicit def ListPersonaToListPersonaTable(peopleList: List[Persona]) =
    peopleList.map(person => new PersonaTable(person.matricola.head.toString, person.nome, person.cognome))
}