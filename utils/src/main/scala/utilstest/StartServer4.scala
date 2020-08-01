package utilstest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer4 {
  val result: Int = Await.result(DatabaseHelper().runScript4(),Duration.Inf)
  require(result==1)
}
