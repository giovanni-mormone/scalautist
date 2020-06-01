package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer

/**
 * Main server
 */
object StartServer extends App{

    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")

}
