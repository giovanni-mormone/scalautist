package testdboperation.persona

import caseclass.CaseClassDB.{Login, Persona}
import caseclass.CaseClassHttpMessage.ChangePassword

object LoginAndCrudValues {
    val persona: Persona = Persona("Fabian", "Aspee", "569918598", None, 1, isNew = true, "admin", None, None, Some(1))
    val personaSelect: Persona = Persona("Fabian", "Aspee", "569918598", Some(""), 1, isNew = true, "admin", None, None, Some(1))
    val persona2: Persona = Persona("Fabian", "Aspee", "569918598", None, 1, isNew = false, "admin", None, None, Some(1))
    val newPersona: Persona = Persona("Juanito", "Perez", "569918598", Some(""), 1, isNew = true, "adminF")
    val updatePersona: Persona = Persona("Fabian Andres", "Aspee Encina", "59613026", Some(""), 1, isNew = false, "admin", None, None, Some(1))
    val listNewPerson: List[Persona] = List(newPersona, Persona("Juanito", "Perez", "569918598", Some(""), 1, isNew = true, "adminF"))
    val login: Login = Login("admin", "admin")
    val changePassword: ChangePassword = ChangePassword(1, "admin", "admin")
}
