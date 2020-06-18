package testdboperation.risultato

import caseclass.CaseClassHttpMessage.InfoHome
import dbfactory.operation.RisultatoOperation
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import utils.StartServer
import RisultatoOperationTestValue._

import scala.concurrent.Future

class TestRisultato extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer {

  behavior of "getTurniInDate"

  it should "return the daily work shift " in {
    val req: Future[Option[InfoHome]] = RisultatoOperation.getTurniInDate(idPersona, date)
    req.map(info => assert(info.isDefined))
  }

}
