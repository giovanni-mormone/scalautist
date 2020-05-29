import akka.actor.typed.ActorSystem
import servermodel.ServerConf
import servermodel.ServerConf.StartServer

//@RunWith(classOf[JUnitRunner])  //TODO scalatest
object Test extends App {
  ActorSystem[StartServer](ServerConf(), "AkkaHttpServer")
}

object TestRequest extends App {
  
}
