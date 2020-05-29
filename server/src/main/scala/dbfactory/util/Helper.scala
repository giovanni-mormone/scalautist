package dbfactory.util
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.Persona
import dbfactory.table.PersonaTable.PersonaTableRep

object Helper {
  type tuplePerson = (String, String, String,Option[String], Int,Boolean,String,Option[Int], Option[Int])
  def convertTupleToPerson(persona:Option[tuplePerson]):Option[Persona] = persona.map(value =>Persona.apply _ tupled value)
  val personaSelect:PersonaTableRep => (Rep[String], Rep[String], Rep[String], Option[String], Rep[Int],Rep[Boolean],Rep[String], Rep[Option[Int]], Rep[Option[Int]]) =  f => ( f.nome, f.cognome, f.numTelefono, Option[String](""), f.ruolo,f.isNew,f.userName, f.terminaleId, f.id.?)

}
