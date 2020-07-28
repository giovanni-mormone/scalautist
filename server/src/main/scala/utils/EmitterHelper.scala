package utils

import java.sql.Date

import com.typesafe.config.ConfigFactory
import emitter.ConfigEmitter
import persistence.ConfigEmitterPersistence

object EmitterHelper {
  private val algorithmEmitter = ConfigEmitter("info_algorithm")
  private val config = ConfigFactory.defaultApplication()
  private val assenzaEmitter = ConfigEmitterPersistence("assenza_emitter","malattie","vacanze")
  private val personaEmitter = ConfigEmitterPersistence("persona_emitter","licenzia","assumi")

  algorithmEmitter.start()
  assenzaEmitter.start()
  personaEmitter.start()

  /**
   * Gets a specific text from a key in the config file.
   */
  val getFromKey: String => String = key => config.getString(key)

  /**
   * Emits a message during the algorithm execution.
   */
  val emitForAlgorithm :String => Unit = message => algorithmEmitter.sendMessage(message)

  /**
   * sends a message for an absence notification.
   */
  val sendAssenzaNotification : (String,String) => Unit = (message, keyPath) =>
    assenzaEmitter.sendMessage(message,keyPath)

  /**
   * sends a message after an operation on the persons.
   */
  val sendPersonaNotification : (String,String) => Unit = (message, keyPath) =>
    personaEmitter.sendMessage(message,keyPath)

  /**
   * Emits a message to a particular person after a straordinary is set
   * @param idPersona
   *                  The person to notify
   * @param result
   *               The straordinary set
   */
  def notificationExtraordinary(idPersona: Int, result: Option[List[(Date, Int)]]): Unit = {
    val notificationEmitter = ConfigEmitterPersistence("disponibilita_emitter", s"$idPersona")
    notificationEmitter.start()
    result.foreach(_.foreach(date => notificationEmitter.sendMessage(getFromKey("do-extraordinary").concat(date._1.toString), s"$idPersona")))
  }
}
