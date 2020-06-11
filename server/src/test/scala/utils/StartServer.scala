package utils


import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer {

  val result: Int = Await.result(DatabaseHelper().runScript(),Duration.Inf)
  require(result==1)
}
