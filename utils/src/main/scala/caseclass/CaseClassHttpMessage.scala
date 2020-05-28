package caseclass

object CaseClassHttpMessage {
  case class ChangePassword(id: Int, oldPassword: String, newPassword: String)
}
