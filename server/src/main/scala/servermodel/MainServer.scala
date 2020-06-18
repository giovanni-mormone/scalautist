package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer
/**
 * Main server
 */
object MainServer extends App{

    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")

}
