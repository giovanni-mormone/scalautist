package view.fxview.component.HumanResources.subcomponent.util

import caseclass.CaseClassDB.Persona

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
  extends PersonaTable(idp, namep, surnamep) with SelectionTableField with TableArgument {

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