package servermodel

import java.sql.Date

import akka.actor.typed.ActorSystem
import caseclass.CaseClassDB.{Assenza, Stipendio}
import dbfactory.operation.{AssenzaOperation, PersonaOperation, StipendioOperation}
import servermodel.ServerConf.StartServer

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Main server
 */
object MainServer extends App{

    ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")

}
