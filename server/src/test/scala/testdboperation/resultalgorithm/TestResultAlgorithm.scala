package testdboperation.resultalgorithm

import caseclass.CaseClassHttpMessage.ResultAlgorithm
import dbfactory.operation.RisultatoOperation
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach}
import utils.StartServer4

import scala.concurrent.Future

class TestResultAlgorithm extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer4{

  import ResultAlgorithmOperationTest._
  behavior of "Get Result Algorithm"
  it should "return None if terminal not exist" in {
    val parameters: Future[Option[List[ResultAlgorithm]]] = RisultatoOperation.getResultAlgorithm(idTerminalNotExist,dateI,dateF)
    parameters map { one =>
      assert(one.isEmpty)
    }
  }
  it should "return None if time frame not contains info" in {
    val parameters: Future[Option[List[ResultAlgorithm]]] = RisultatoOperation.getResultAlgorithm(idTerminal,dateIWithoutInfo,dateFWithoutInfo)
    parameters map { one =>
      assert(one.isEmpty)
    }
  }
}
