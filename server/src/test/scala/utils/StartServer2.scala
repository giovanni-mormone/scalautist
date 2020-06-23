package utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer2 {
  val result: Int = Await.result(DatabaseHelper().runScript2(),Duration.Inf)
  require(result==1)
}