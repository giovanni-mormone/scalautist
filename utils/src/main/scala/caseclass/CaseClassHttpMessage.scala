package caseclass

import caseclass.CaseClassDB.{Disponibilita, Persona, StoricoContratto, Straordinario}

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
  case class Id(id:Int)

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
   * @param giorniVacanza quantity of day of a persona in holiday
   */
  case class Ferie(idPersona:Int,nomeCognome:String,giorniVacanza:Int)
}
