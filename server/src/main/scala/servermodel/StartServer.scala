package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer

object StartServer extends App {

  startServer()

  def startServer(): Unit = {
    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
  }
}
