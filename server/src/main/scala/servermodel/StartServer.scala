package servermodel

import akka.actor.typed.ActorSystem
import servermodel.ServerConf.StartServer

object StartServer extends App{
<<<<<<< HEAD

    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")

=======
    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
  
>>>>>>> fb3075978af9d1f415f1f2c6300adaf35f0b4d20
}
