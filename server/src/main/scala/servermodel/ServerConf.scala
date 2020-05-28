package servermodel

import scala.util.{Failure, Success}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import routes.Route._


object ServerConf {
  final case class StartServer()

  private val interface = "0.0.0.0"
  private val port = 8080

  def apply():Behavior[StartServer]=
    Behaviors.setup{
      context =>
        startHttpServer(route, context.system)
        Behaviors.empty
    }

  private def startHttpServer(routes: Route, system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext
    Http().bindAndHandle(routes, interface, port).onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
}
