package utils


import util.RunJar

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServerC {

  RunJar.runJar()
  val result: Int = Await.result(DatabaseHelper().runScript3(),Duration.Inf)
  require(result==1)
}
