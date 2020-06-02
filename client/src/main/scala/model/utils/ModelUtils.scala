package model.utils

import caseclass.CaseClassDB.{Contratto, Login, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Id

import scala.concurrent.{Future, Promise}

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {
  implicit def id(id:Int):Id = Id(id)
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
