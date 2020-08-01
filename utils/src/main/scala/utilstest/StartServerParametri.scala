package utilstest

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServerParametri {
  val result: Int = Await.result(DatabaseHelper().runScriptParametri(),Duration.Inf)
  require(result==1)
}
