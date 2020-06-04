package regularexpressionutilities

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

/**
 * @author Francesco Cassano
 *
 * Number utilities object, such as regex to check passwords or functions to hash the password.
 */
object NumberChecker {
  private val regex: Regex = "^\\d$".r

  /**
   * Stores the regex to validate a number.
   *
   * @return
   *        The regex needed to validate a number.
   */
  def numberRegex: Regex = regex
}

/**
 * @author Francesco Cassano
 *
 * Name or surname utilities object, such as regex to check passwords or functions to hash the password.
 */
object NameChecker {
  private val regex: Regex = "^[A-Za-z ']$".r

  /**
   * Stores the regex to validate a name.
   *
   * @return
   *        The regex needed to validate a name.
   */
  def nameRegex: Regex = regex
}


