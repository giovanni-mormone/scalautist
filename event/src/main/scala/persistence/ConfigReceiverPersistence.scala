package persistence

import com.rabbitmq.client.{CancelCallback, DeliverCallback}

trait ConfigReceiverPersistence {
  def start(): Unit

  def consumeNotification(tag: Long): Unit

  def receiveMessage(method: (String, Long) => Unit): Unit
}
object ConfigReceiverPersistence {
  def apply(nameQueue:String,routingKey:String*): ConfigReceiverPersistence = new ConfigReceiverImpl(nameQueue,routingKey)
  private class ConfigReceiverImpl(nameQueue:String,routingKey:Seq[String]) extends ConfigReceiverPersistence{

    import emitter.ConnectionStart._
    val EXCHANGE_NAME = "general_info"
    val NAME_QUEUE: String = nameQueue
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      channel.queueDeclare(NAME_QUEUE, false, false,  false,  null).getQueue
      routingKey.foreach(routingKey=> channel.queueBind(NAME_QUEUE, EXCHANGE_NAME, routingKey))
      channel.basicQos(0,1,false)
    }

    override def receiveMessage(method:(String,Long)=>Unit): Unit ={
      val deliverCallback:DeliverCallback = (consumerTag, delivery) => {
        val message = new String(delivery.getBody, "UTF-8")
        method(message,delivery.getEnvelope.getDeliveryTag)
        channel.basicAck(delivery.getEnvelope.getDeliveryTag,true)
      }
      val cancel: CancelCallback = _ => {}
      val autoAck = false
      channel.basicConsume(NAME_QUEUE, autoAck, deliverCallback, cancel)
    }
    override def consumeNotification(tag:Long): Unit ={
      channel.basicAck(tag,false)
    }
  }
}