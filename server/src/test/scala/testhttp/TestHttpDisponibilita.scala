package testhttp

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.wordspec.AnyWordSpec
import servermodel.MainServer
import utils.StartServer

object TestHttpDisponibilita {
  private def startServer(): Unit = MainServer
}

class TestHttpDisponibilita extends AnyWordSpec with ScalatestRouteTest with StartServer {
  import TestHttpDisponibilita._
  startServer()


}