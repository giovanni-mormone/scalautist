package persistence

import com.rabbitmq.client.{CancelCallback, DeliverCallback}
trait ConfigReceiverPersistence{
  def start():Unit
  def receiveMessage(method:String=>Unit):Unit
}
object ConfigReceiverPersistence {
  def apply(routingKey:String,nameQueue:String): ConfigReceiverPersistence = new ConfigReceiverImpl(routingKey,nameQueue)
  private class ConfigReceiverImpl(routingKey:String,nameQueue:String) extends ConfigReceiverPersistence{

    import emitter.ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    val NAME_QUEUE: String = nameQueue
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      channel.queueDeclare(NAME_QUEUE, false, false,  false,  null).getQueue
      channel.queueBind(NAME_QUEUE, EXCHANGE_NAME, routingKey)
      channel.basicQos(0,1,false)
    }

    override def receiveMessage(method:String=>Unit): Unit ={
      val deliverCallback:DeliverCallback = (consumerTag, delivery) => {
        val message = new String(delivery.getBody, "UTF-8")
        method(message)
        channel.basicAck(delivery.getEnvelope.getDeliveryTag,false)
      }
      val cancel: CancelCallback = _ => {}
      val autoAck = false
      channel.basicConsume(NAME_QUEUE, autoAck, deliverCallback, cancel)
    }
  }
}
object ssa extends App{
  def g(s:String):Unit = println(s)
  val e = ConfigReceiverPersistence("assumi","licenzia")
  e.start()
  e.receiveMessage(g)
}
object ssa1 extends App{
  def g(s:String):Unit = println(s)
  val e = ConfigReceiverPersistence("assumi","licenzia2")
  e.start()
  e.receiveMessage(g)
}
object ssa2 extends App{
  def g(s:String):Unit = println(s)
  val e = ConfigReceiverPersistence("assumi","licenzia3")
  e.start()
  e.receiveMessage(g)
}