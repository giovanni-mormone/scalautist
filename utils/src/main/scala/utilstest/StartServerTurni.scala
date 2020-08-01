package utilstest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServerTurni {
  val result: Int = Await.result(DatabaseHelper().runScriptT(),Duration.Inf)
  require(result==1)
}
