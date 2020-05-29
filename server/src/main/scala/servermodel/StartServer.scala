package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer

object StartServer {

  def startServer(): Unit = {
    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
  }
}
