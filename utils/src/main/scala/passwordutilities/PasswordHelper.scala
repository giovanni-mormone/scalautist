package passwordutilities

import scala.util.matching.Regex

/**
 * @author Giovanni Mormone.
 *
 * Password utilities object, such as regex to check passwords or functions to hash the password.
 *
 */
object PasswordHelper {
  private val regex: Regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$".r

  /**
   * Stores the regex to validate a password.
   * @return
   *         The regex needed to validate a password
   */
  def passwordRegex(): Regex =
    regex

}
