package emitter

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

object ConnectionStart {
  val factory = new ConnectionFactory
  factory.setHost("localhost")
  val connection: Connection = factory.newConnection
  val channel: Channel = connection.createChannel

}
