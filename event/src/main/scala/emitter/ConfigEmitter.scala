package emitter

trait ConfigEmitter{
  def start():Unit
  def sendMessage(message:String):Unit
}
object ConfigEmitter {
  def apply(): ConfigEmitter = new ConfigEmitterImpl()
  private class ConfigEmitterImpl() extends ConfigEmitter{
    import ConnectionStart._
    val EXCHANGE_NAME = "info_algorithm_logs"
    val routingKey = "info_algorithm"
    override def start(): Unit = {
       channel.exchangeDeclare(EXCHANGE_NAME, "direct")
    }
    override def sendMessage(message:String): Unit ={
      channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"))
    }
  }
}

