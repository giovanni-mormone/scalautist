package persistence

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

object ConnectionStart {
  val factory: ConnectionFactory = new ConnectionFactory
  factory.setHost("localhost")
  val connection: Connection = factory.newConnection
  val channel: Channel = connection.createChannel()

}
