package dbfactory.util
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.Persona
import dbfactory.table.PersonaTable.PersonaTableRep

import scala.concurrent.{ExecutionContext, Future}

import ExecutionContext.Implicits.global
object Helper {
  val EMPTY_STRING: String =""
  type tuplePerson = (String, String, String,Option[String], Int,Boolean,String,Option[Int], Option[Int], Option[Int])
  def convertTupleToPerson(persona:Option[tuplePerson]):Option[Persona] = persona.map(value =>Persona.apply _ tupled value)
  val personaSelect:PersonaTableRep => (Rep[String], Rep[String], Rep[String], Option[String], Rep[Int],Rep[Boolean],Rep[String], Rep[Option[Int]], Rep[Option[Int]],Rep[Option[Int]]) =  f => ( f.nome, f.cognome, f.numTelefono, Option[String](EMPTY_STRING), f.ruolo,f.isNew,f.userName, f.terminaleId, f.disponibilitaId,f.id.?)

  implicit class ConvertToOption[A](f:Future[Seq[A]]){
    def result(): Future[Option[List[A]]] =  f.collect({
      case Seq() => None
      case value => Some(value.toList)
    })
  }
  implicit class ConvertToOptionOneElement[A](f:Future[A]){
    def result(): Future[Option[A]] =  f.collect({
      case 0 => None
      case value => Some(value)
    })
  }
}
