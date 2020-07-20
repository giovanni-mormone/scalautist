package persistence

import com.rabbitmq.client.MessageProperties

trait ConfigEmitter {
  def start():Unit
  def sendMessage(message:String):Unit
}
object ConfigEmitter {
  def apply(routingKey:String): ConfigEmitter = new ConfigEmitterImpl(routingKey)
  private class ConfigEmitterImpl(routingKey:String) extends ConfigEmitter{
    import ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    val NAME_QUEUE = "licenzia5"
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      val queueNameComentario = channel.queueDeclare(NAME_QUEUE, true, false,  false,  null).getQueue;
      channel.queueBind(queueNameComentario, EXCHANGE_NAME, routingKey);
    }
    override def sendMessage(message:String): Unit ={
      channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"))
    }


  }
}
object das extends App{
  val s = ConfigEmitter("assumi")
  s.start()
  s.sendMessage("3")
}
