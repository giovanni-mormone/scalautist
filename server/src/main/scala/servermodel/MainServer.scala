package servermodel

import java.sql.Date

import akka.actor.typed.ActorSystem
import dbfactory.operation.StipendioOperation
import servermodel.ServerConf.StartServer

/**
 * Main server
 */
object MainServer extends App{

    //ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
    StipendioOperation.calculateStipendi(new Date(System.currentTimeMillis()))


    while(true){}
}
