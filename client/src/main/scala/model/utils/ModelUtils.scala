package model.utils

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {

  private val generator = scala.util.Random.alphanumeric

  /**
   * Password generator.
   * @return
   * Long string 10 random alphanumeric characters
   */
  def generatePassword = {
    val password: String = ""
    generator take 10 foreach(c => password.concat(c.toString))
    password
  }
}
