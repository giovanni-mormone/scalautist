package utils

import com.typesafe.config.ConfigFactory
import emitter.ConfigEmitter
import persistence.ConfigEmitterPersistence

object EmitterHelper {
  private val algorithmEmitter = ConfigEmitter("info_algorithm")
  private val config = ConfigFactory.defaultApplication()
  private val assenzaEmitter = ConfigEmitterPersistence("assenza_emitter","malattie","vacanze")
  private val personaEmitter = ConfigEmitterPersistence("persona_emitter","licenzia","assumi")

  assenzaEmitter.start()
  personaEmitter.start()

  val getFromKey: String => String = key => config.getString(key)

  val emitForAlgorithm :String => Unit = message => algorithmEmitter.sendMessage(message)

  val sendAssenzaNotification : (String,String) => Unit = (message, keyPath) =>
    assenzaEmitter.sendMessage(message,keyPath)

  val sendPersonaNotification : (String,String) => Unit = (message, keyPath) =>
    personaEmitter.sendMessage(message,keyPath)
}
