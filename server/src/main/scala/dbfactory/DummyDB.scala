package dbfactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object DummyDB {

  def dummyReq(): Future[String] = Future {
    "ACAB: All Cannelloni Are Buoni"
  }

}
