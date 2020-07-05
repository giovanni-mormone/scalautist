package emitter
import com.rabbitmq.client.ConnectionFactory

trait ConfigEmitter{
  def start():Unit
}
object ConfigEmitter {
  def apply(): ConfigEmitter = new ConfigEmitterImpl()
  private class ConfigEmitterImpl() extends ConfigEmitter{
    private val EXCHANGE_NAME = "topic_logs"
    val factory = new ConnectionFactory
    factory.setHost("localhost")
    override def start(): Unit = {
      try {
        val connection = factory.newConnection
        val channel = connection.createChannel
        try {
          channel.exchangeDeclare(EXCHANGE_NAME, "topic")
          val routingKey:String = ??? //getRouting(argv)
          val message:String = ??? //getMessage(argv)
          channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"))
          System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'")
        } finally {
          if (connection != null) connection.close()
          if (channel != null) channel.close()
        }
      }
    }
  }
}

