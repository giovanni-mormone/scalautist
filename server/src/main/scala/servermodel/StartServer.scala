package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer

object StartServer extends App{
    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
  
}
