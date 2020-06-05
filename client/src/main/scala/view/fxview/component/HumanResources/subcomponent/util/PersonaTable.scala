package view.fxview.component.HumanResources.subcomponent.util

import caseclass.CaseClassDB.Persona
import javafx.beans.property.SimpleStringProperty

/**
 * @author Francesco Cassano
 *
 * Class to draw people in table
 * @param idp
 *            String id
 * @param namep
 *              String name
 * @param surnamep
 *                 String surname
 */
class PersonaTable(idp: String, namep: String, surnamep: String) extends TableArgument {

  var id = new SimpleStringProperty(idp)
  var name = new SimpleStringProperty(namep)
  var surname = new SimpleStringProperty(surnamep)
  //var contract = new SimpleStringProperty(contractp)

  def getId: String = id.get
  def getName: String = name.get
  def getSurname: String = surname.get
  //def getContract: String = contract.get

  def setId(v: String): Unit = id.set(v)
  def setName(v: String): Unit = name.set(v)
  def setSurname(v: String): Unit = surname.set(v)
  //def setContract(v: String) = contract.set(v)
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
