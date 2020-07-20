package persistence

import com.rabbitmq.client.MessageProperties
import emitter.ConnectionStart._
trait ConfigEmitterPersistence {
  def start():Unit
  def sendMessage(message:String):Unit
}
object ConfigEmitterPersistence {
  def apply(routingKey:String): ConfigEmitterPersistence = new ConfigEmitterImpl(routingKey)
  private class ConfigEmitterImpl(routingKey:String) extends ConfigEmitterPersistence{
    val EXCHANGE_NAME = "info_algorithm_logs"
    val NAME_QUEUE = "licenzia5"
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      channel.queueDeclare(NAME_QUEUE, true, false,  false,  null).getQueue
      channel.queueBind(NAME_QUEUE, EXCHANGE_NAME, routingKey)
    }
    override def sendMessage(message:String): Unit ={
      channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"))
    }


  }
}

object das extends App{
  val s = ConfigEmitterPersistence("assumi")
  s.start()
  s.sendMessage("3")
}