package dbfactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DummyDB {

  def dummyReq() = Future {
    "ACAB: All Cannelloni Are Buoni"
  }

}
