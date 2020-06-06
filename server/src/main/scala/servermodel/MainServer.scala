package servermodel

import java.sql.Date

import akka.actor.typed.ActorSystem
import caseclass.CaseClassDB.Stipendio
import dbfactory.operation.{AssenzaOperation, PersonaOperation, StipendioOperation}
import servermodel.ServerConf.StartServer
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Main server
 */
object MainServer extends App{

    //ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
    AssenzaOperation.getAllFerie(2021).collect({
        case value => println("prossssssss"+value)
    })
    AssenzaOperation.getAllFerie(2020).collect({
        case value => println("mooooooo" + value)
    })
    while(true){}
}
