package utilstest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer5 {
  val result: Int = Await.result(DatabaseHelper().runScript5(),Duration.Inf)
  require(result==1)
}
