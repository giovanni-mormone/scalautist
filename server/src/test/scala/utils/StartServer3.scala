package utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer3 {
  val result: Int = Await.result(DatabaseHelper().runScript3(),Duration.Inf)
  require(result==1)
}
