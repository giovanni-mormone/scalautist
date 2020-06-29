package caseclass

import java.sql.Date

import caseclass.CaseClassDB._

/**
 * @author Fabian Aspee Encina, Giovanni Mormone, Francesco Cassano
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
   * @tparam A generic type of request
   */
  final case class Request[A](payload:Option[A])
  /**
   * case class used has a wrapper for server response
   * @param statusCode status of the operation
   * @param payload object that represent the case class send of the server
   * @tparam A generic type of response
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

  /**
   * Case class that represent informations of a given day
   * @param turno list lenght two that contains the shift that the person must make in the day
   * @param disponibilita availability that the person has indicated as
   *                      possible days that they can make extra shifts
   */
  final case class InfoHome(turno:List[Turno],disponibilita: Option[Disponibilita])

  /**
   * Case class that represent information for one shift in a day
   * @param idGiorno represent id for a day in week
   * @param orario represent working hours of a shift
   */
  final case class ShiftDay(idGiorno:Int,orario:String)
  /**
   * Case class that represent informations of a given day
   * @param shiftDay list that contains the shift that the person must make in the week
   * @param disponibilita availability that the person has indicated as
   *                      possible days that they can make extra shifts
   */
  final case class InfoShift(shiftDay:List[ShiftDay],disponibilita: Option[Disponibilita])

  /**
   * Case class that represent information for all terminal that contains absence
   * @param nomeTerminale name of terminal that contains absence
   * @param nomeTurno shift that contains absence
   * @param idTerminale id that is unique key for terminal table
   * @param idTurno id that is unique key for turno table
   * @param idRisultato id that is unique key for risultato table
   */
  final case class InfoAbsenceOnDay(nomeTerminale:String,nomeTurno:String,idTerminale:Int,idTurno:Int,idRisultato:Int)

  /**
   * Case class to replacement
   * @param idRisultato id of risultato
   * @param idPersona id of driver
   * @param nome name of driver
   * @param cognome surname of driver
   */
  final case class InfoReplacement(idRisultato:Int,idPersona:Int,nome:String,cognome:String)

  /**
   * Case class that represent information to find the instance of risultato for absent employee
   * @param idDriver id of absent employee
   * @param date date of absence
   */
  final case class InfoVacantShift(idDriver: Int, date: String)

  /**
   * Case class that represent information of daily theoretical request
   * @param day
   * @param shift
   */
  final case class RequestGiorno(day: Giorno, shift: Int)

  /**
   * Case class that represent all info to save a theoretical request
   * @param request list of request to save for each terminal
   * @param days list of giorno to save for each day of the week
   */
  final case class AssignRichiestaTeorica(request: List[RichiestaTeorica], days: List[RequestGiorno])

  /**
   * case class which represents both a normal week and a special especial week
   *
   * @param idDay represent day in week, that is to say, if monday then 1, tuesday then 2 etc.
   * @param quantita represent the quantity we want to have more of,that is, the number of drivers that we want to have as a supplement
   * @param regola represent the ruler we have to respect when assign driver in this day, this ruler can be
   *               % With respect to theoretical drivers
   *               - Quantity compared to theoretical drivers
   *               % Relative of drivers
   */
  final case class SettimanaNS(idDay:Int,quantita:Int,regola:Int)

  /**
   * case class which represent a group of driver in assignation.
   *
   * @param idGruppo id which represent the group in a period
   * @param date   represent list of date where this group is present
   * @param regola represent the ruler we have to respect when assign driver in this day, this ruler can be
   *               -respect the rule two days off in a row
   *               -respect rule a day off one worked a day off
   *               -respect two precedent case
   */
  final case class GruppoA(idGruppo:Int,date:List[Date],regola:Int)

  /**
   * case class which represent information that algorithm need for calculus of shift and free day
   * @param dateI represents date init calculus
   * @param dateF represent date finish calculus
   * @param idTerminal list with all terminal that algorithm must process
   * @param gruppo all existing groups in this parameterization
   * @param settimanaNormale all existing normal week in this parameterization
   * @param settimanaSpeciale all existing special week in this parameterization
   * @param regolaTreSabato ruler which represent if every three saturday a driver must have free day
   */
  final case class AlgorithmExecute(dateI:Date,dateF:Date,idTerminal:List[Int],gruppo: Option[List[GruppoA]],
                                    settimanaNormale: Option[List[SettimanaNS]],settimanaSpeciale: Option[List[SettimanaNS]],
                                    regolaTreSabato:Boolean)
}
