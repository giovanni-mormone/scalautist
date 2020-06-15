package caseclass

import java.sql.Date

import caseclass.CaseClassDB.{Assenza, Disponibilita, Persona, Presenza, Stipendio, StoricoContratto, Turno}

/**
 * @author Fabian Aspee Encina, Giovanni Mormone
 * Object that encapsulates case class diverse of encapsulates in [[caseclass.CaseClassDB]],
 * this is util because [[spray.json]] required this for make serialization of the objects
 */
object CaseClassHttpMessage {
  /**
   * case class that represent the possibility of change password in the system
   * @param id represent the user id, this is for search user into database and
   * and be able to change password
   * @param oldPassword represent the old password of the user in the system
   * @param newPassword represent the new password of the user in the system
   */
  case class ChangePassword(id: Int, oldPassword: String, newPassword: String)

  /**
   * case class which enable send id and use post for receive
   * @param id id that represent identifies of a element into database for operation
   *           select or delete.
   */
  final case class Id(id:Int)

  /**
   * case class used has a wrapper for server response
   * @param payload object that represent the case class send of the server
   * @tparam A
   */
  final case class Request[A](payload:Option[A])
  /**
   * case class used has a wrapper for server response
   * @param statusCode status of the operation
   * @param payload object that represent the case class send of the server
   * @tparam A
   */
  final case class Response[A](statusCode:Int,payload:Option[A]=None)

  /**
   * case class which enable send id and use post for receive
   * @param date date for request into rest api for obtain salary for a person an another operation
   */
  final case class Dates(date:Date)

  /**
   * Case class which enable create a driver, Human resource or Manager operation in the system
   * @param persona          Persona is a case class that represent a instance of table into database that contains
   *                         all information necessary for a persona, a persona can be a driver, human resources or
   *                         manager operation
   * @param storicoContratto StoricoContratto is a case class that represent a instance of table into database that contains all
   *                         work contract
   * @param disponibilita     Disponibilita is a case class that represent a instance of table into database that contains
   *                         all over time that driver make in the week, where week can be also saturday
   */
  final case class Assumi(persona:Persona,storicoContratto: StoricoContratto,disponibilita:Option[Disponibilita]=None)

  /**
   * case class that represent the day that one person to have of holiday
   * @param idPersona represent the user id, this is for search user into database and
   *                  and be able to change password
   * @param nomeCognome name and surname of the person
   * @param giorniVacanza
   *                      Remaining day of holidays for the person
   */
  final case class Ferie(idPersona:Int,nomeCognome:String,giorniVacanza:Int=0)

  /**
   * case class that represents the informations of a Presenza for a Stipendio
   * @param valoreTurno
   *                    The money value of the turno
   * @param durataTurno
   *                    A representation of the duration of the turno
   * @param nomeTurno
   *                  The name of the turno
   * @param data
   *             The Date of the turno
   * @param straordinario
   *                      Wheter or not is a straordinario
   */
  final case class InfoPresenza(valoreTurno:Double,durataTurno:String, nomeTurno:String,data:Date, straordinario: Boolean)

  /**
   * case class that represents the informations of presenze for a Stipendio
   * @param giorniLavorati
   *                       Total days worked in the month of the stipendio
   * @param valoreTotaleTurni
   *                          Money earned for normal turni
   * @param valoreTotaleStraordinari
   *                                 Money earned for turni straordinari
   */
  final case class InfoValorePresenza(giorniLavorati: Int, valoreTotaleTurni: Double,valoreTotaleStraordinari:Double)

  /**
   * case class that represents the informations of Assenze for a Stipendio
   * @param assenzePerFerie
   *                        Total days of ferie in the month of the stipendio
   * @param assenzePerMalattia
   *                           Total day of malattie in the month of the stipendio
   */
  final case class InfoAssenza(assenzePerFerie: Int, assenzePerMalattia: Int)

  /**
   * case class that represents the informations of a given stipendio
   * @param turni
   *              The List of turni worked in the month of the stipendio, of type [[caseclass.CaseClassHttpMessage.InfoPresenza]]
   * @param infoValore
   *                   The [[caseclass.CaseClassHttpMessage.InfoValorePresenza]] for the stipendio
   * @param infoAssenza
   *                    The [[caseclass.CaseClassHttpMessage.InfoAssenza]] for the stipendio
   */
  final case class StipendioInformations(turni:List[InfoPresenza], infoValore: InfoValorePresenza, infoAssenza: InfoAssenza)
}
