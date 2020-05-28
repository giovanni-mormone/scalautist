package caseclass

import java.sql.Date

/**
 *
 */
object CaseClassDB{
   /**
   * Login is a case class that represent parameters for login
   * @param user
   *             user name in system
   * @param password
   *                 user password in system
   */
  final case class Login(user:String,password:String) 
  final case class Contratto(tipoContratto:String,turnoFisso:Byte,idContratto:Option[Int]=None)
  final case class Giorno(quantita:Int,nomeGiorno:String,idGiorno:Option[Int]=None)
  final case class GruppoTerminale(parametriId:Int,idGruppoTerminale:Option[Int]=None)
  final case class Parametro(treSabato:Byte,Regola:String,idParametri:Option[Int]=None)
  final case class Persona(nome:String,cognome:String,numTelefono:String,password:Option[String],ruolo:Int,isNew:Boolean,userName:String,idTerminale:Option[Int]=None,matricola:Option[Int]=None)
  final case class Presenza(data:Date,personaId:Int,turnoId:Int,idPresenza:Option[Int]=None)
  final case class Richiesta(turnoId:Int,giornoId:Int,richiestaTeoricaId:Int,idRichiesta:Option[Int]=None)
  final case class RichiestaTeorica(dataInizio:Date,dataFine:Option[Date],idRichiestaTeorica:Option[Int]=None)
  final case class Risultato(data:Date,personaId:Int,turnoId:Int,idRisultato:Option[Int]=None)
  final case class GiornoInSettimana(giornoId:Int,turnoId:Int,parametriId:Int,settimanaId:Int,idSettimana:Option[Int]=None)
  final case class StoricoContratto(dataInizio:Date,dataFine:Option[Date],personaId:Int,contrattoId:Int,turnoId:Option[Int],turnoId1:Option[Int],idStoricoContratto:Option[Int]=None)
  final case class Straordinario(data:Date,personaId:Int,turnoId:Int,idStraordinario:Option[Int]=None)
  final case class Terminale(nomeTerminale:String,idZona:Int,idTerminale:Option[Int]=None)
  final case class Turno(nomeTurno: String, fasciaOraria: String,id: Option[Int] = None)
  final case class Zona(zones:String,idZone:Option[Int]=None)
  final case class Settimana(parametriSetId:Int,idZone:Option[Int]=None)
  final case class Stipendio(personaId: Int, valore: Double, data: Date, idStipendio: Option[Int] = None)
  final case class Assenza(perosnaId: Int, dataInizio: Date, dataFine: Date, malattia: Boolean, idAssenza: Option[Int] = None)
}

