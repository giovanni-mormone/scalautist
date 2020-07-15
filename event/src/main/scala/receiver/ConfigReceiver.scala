package receiver

import com.rabbitmq.client.{CancelCallback, DeliverCallback}

trait ConfigReceiver {
  def start():Unit
  def receiveMessage(method:String=>Unit):Unit
}
object ConfigReceiver {
  def apply(routingKey:String): ConfigReceiver = new ConfigReceiverImpl(routingKey)
  private class ConfigReceiverImpl(routingKey:String) extends ConfigReceiver{
    import emitter.ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    var queueName: String =""
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "direct")
      queueName = channel.queueDeclare.getQueue
      channel.queueBind(queueName, EXCHANGE_NAME, routingKey)
    }
    override def receiveMessage(method:String=>Unit): Unit ={
      val deliverCallback:DeliverCallback = (consumerTag, delivery) => {
          val message = new String(delivery.getBody, "UTF-8")
          method(message)
      }
      val cancel: CancelCallback = _ => {}
      val autoAck = true
      channel.basicConsume(queueName, autoAck, deliverCallback, cancel)
    }
  }
}
