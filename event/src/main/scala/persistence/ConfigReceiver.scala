package persistence

import com.rabbitmq.client.{CancelCallback, DeliverCallback}
object Common{
  var nameQueue:String=""
}
trait ConfigReceiver{
  def start():Unit
  def receiveMessage(method:String=>Unit):Unit
}
object ConfigReceiver {
  def apply(routingKey:String,nameQueue:String): ConfigReceiver = new ConfigReceiverImpl(routingKey,nameQueue)
  private class ConfigReceiverImpl(routingKey:String,nameQueue:String) extends ConfigReceiver{

    import emitter.ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    val NAME_QUEUE: String = nameQueue
    override def start(): Unit = {
      channel.exchangeDeclare(EXCHANGE_NAME, "topic",true)
      channel.queueDeclare(NAME_QUEUE, false, false,  false,  null).getQueue;
      channel.queueBind(NAME_QUEUE, EXCHANGE_NAME, routingKey);
      channel.basicQos(0,1,false);
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
  val e = ConfigReceiver("assumi","licenzia")
  e.start()
  e.receiveMessage(g)
}
object ssa1 extends App{
  def g(s:String):Unit = println(s)
  val e = ConfigReceiver("assumi","licenzia2")
  e.start()
  e.receiveMessage(g)
}
object ssa2 extends App{
  def g(s:String):Unit = println(s)
  val e = ConfigReceiver("assumi","licenzia3")
  e.start()
  e.receiveMessage(g)
}