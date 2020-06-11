package caseclass

import java.sql.Date

import caseclass.CaseClassDB.{Disponibilita, Persona, StoricoContratto}

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
  case class Dates(date:Date)

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
  case class Assumi(persona:Persona,storicoContratto: StoricoContratto,disponibilita:Option[Disponibilita]=None)

  /**
   * case class that represent the day that one person to have of holiday
   * @param idPersona represent the user id, this is for search user into database and
   *                  and be able to change password
   * @param nomeCognome name and surname of the person
   * @param giorniVacanza
   *                      Remaining day of holidays for the person
   */
  case class Ferie(idPersona:Int,nomeCognome:String,giorniVacanza:Int=0)
}
