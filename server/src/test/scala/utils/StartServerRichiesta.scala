package utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServerRichiesta {
  val result: Int = Await.result(DatabaseHelper().runScriptRichiesta(),Duration.Inf)
  require(result==1)
}