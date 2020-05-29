package passwordutilities

import scala.util.matching.Regex

/**
 * Password utilities object, such as regex to check passwords or functions to hash the password.
 */
object PasswordHelper {
  private val regex: Regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$".r

  def passwordRegex(): Regex =
    regex

}
