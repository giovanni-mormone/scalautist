package caseclass

/**
 * Object that encapsulates case class diverse of encapsulates in [[caseclass.CaseClassDB]],
 * this is util because [[spray.json]] required this for make serialization of the objects
 */
object CaseClassHttpMessage {

  /**
   *
   * @param id
   * @param oldPassword
   * @param newPassword
   */
  case class ChangePassword(id: Int, oldPassword: String, newPassword: String)

  /**
   * case class which enable send id and use post for receive
   * @param id id that represent identifies of a element into database for operation
   *           select or delete.
   */
  case class Id(id:Int)
}
