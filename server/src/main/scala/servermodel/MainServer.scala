package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer
/**
 * Main server
 */
object MainServer{
    def main(args: Array[String]): Unit = {
      println(1212112)
      ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
    }
}