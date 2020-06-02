package utils

import DatabaseHelper.runScript

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait StartServer {
  val result: Int = Await.result(runScript(),Duration.Inf)
  require(result==1)
}
