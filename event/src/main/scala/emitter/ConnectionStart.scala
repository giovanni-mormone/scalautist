package emitter

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

object ConnectionStart {
  val factory = new ConnectionFactory
  factory.setHost("40.114.191.202")
  factory.setPort(5672)
  val connection: Connection = factory.newConnection
  val channel: Channel = connection.createChannel

}
