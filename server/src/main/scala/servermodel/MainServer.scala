package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer
/**
 * Main server
 */
object MainServer{
    def main(args: Array[String]): Unit = {
      ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
    }
}