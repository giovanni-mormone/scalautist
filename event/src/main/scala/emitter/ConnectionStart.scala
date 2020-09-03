package emitter

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

object ConnectionStart {
  val factory = new ConnectionFactory
  factory.setHost("20.54.217.161")
  factory.setPort(5672)
  val connection: Connection = factory.newConnection
  val channel: Channel = connection.createChannel

}
