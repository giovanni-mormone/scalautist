package persistence

import com.rabbitmq.client.MessageProperties
import emitter.ConnectionStart._
trait ConfigEmitterPersistence {
  def start():Unit
  def sendMessage(message:String,routingKey:String):Unit
}
object ConfigEmitterPersistence {
  def apply(nameQueue:String,routingKey:String*): ConfigEmitterPersistence = new ConfigEmitterImpl(nameQueue,routingKey)
  private class ConfigEmitterImpl(nameQueue:String,routingKey:Seq[String]) extends ConfigEmitterPersistence{
    val EXCHANGE_NAME = "general_info"
    val NAME_QUEUE: String = nameQueue
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      channel.queueDeclare(NAME_QUEUE, true, false,  false,  null).getQueue
      routingKey.foreach(routingKey=>channel.queueBind(NAME_QUEUE, EXCHANGE_NAME, routingKey))
    }
    override def sendMessage(message:String,routingKey:String): Unit ={
      channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"))
    }
  }
}

object das extends App{
  val s = ConfigEmitterPersistence("emitter","assumi","licenzia")
  s.start()
  s.sendMessage("Abbiamo assunto a pedrito","assumi")
}
object das2 extends App{
  val s = ConfigEmitterPersistence("emitter","assumi","licenzia")
  s.start()
  s.sendMessage("Abbiamo licenziato a pedrito","licenzia")
}
object das22 extends App{
  val s = ConfigEmitterPersistence("emitter","OPA2")
  s.start()
  s.sendMessage("Abbiamo licenziato a pedrito :C","OPA2")
}