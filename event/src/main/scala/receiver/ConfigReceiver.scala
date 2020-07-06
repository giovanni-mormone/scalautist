package receiver

import com.rabbitmq.client.{CancelCallback, DeliverCallback}

trait ConfigReceiver {
  def start():Unit
  def receiveMessage():Unit
}
object ConfigReceiver {
  def apply(): ConfigReceiver = new ConfigReceiverImpl()
  private class ConfigReceiverImpl() extends ConfigReceiver{
    import emitter.ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    val routingKey = "info_algorithm"
    var queueName: String =""
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "direct")
      queueName = channel.queueDeclare.getQueue
      channel.queueBind(queueName, EXCHANGE_NAME, routingKey)
    }
    override def receiveMessage(): Unit ={
      val deliverCallback:DeliverCallback = (consumerTag, delivery) => {
          val message = new String(delivery.getBody, "UTF-8")
          println(s"Received $message with tag $consumerTag")
      }
      val cancel: CancelCallback = _ => {}
      val autoAck = true
      channel.basicConsume(queueName, autoAck, deliverCallback, cancel)
    }
  }
}
