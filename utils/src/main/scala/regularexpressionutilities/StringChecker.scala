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

trait Checker{

  def checkRegex: Regex
}

/**
 * @author Francesco Cassano
 *
 * Number utilities object, such as regex to check numbers.
 */
object NumberChecker extends Checker {
  private val regex: Regex = "^\\d$".r

  /**
   * Stores the regex to validate a number.
   *
   * @return
   *        The regex needed to validate a number.
   */
  override def checkRegex: Regex = regex
}

object NumbersChecker extends Checker {
  private val regex: Regex = "-?[0-9]".r

  override def checkRegex: Regex = regex
}

/**
 * @author Francesco Cassano
 *
 * Name or surname utilities object, such as regex to check Persona's name single character.
 */
object NameChecker extends Checker {
  private val regex: Regex = "^[A-Za-z ']$".r

  /**
   * Stores the regex to validate a name.
   *
   * @return
   *        The regex needed to validate a name.
   */
  override def checkRegex: Regex = regex
}

/**
 * @author Francesco Cassano
 *
 * Name of Zona utilities object, such as regex to check Zona's name single character.
 */
object ZonaChecker extends Checker {
  private val regex: Regex = "^[A-Za-z0-9]$".r   //"^[A-Za-z].[A-Za-z0-9]$".r

  override def checkRegex: Regex = regex
}
