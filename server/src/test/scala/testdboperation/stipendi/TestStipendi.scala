package testdboperation.stipendi

import caseclass.CaseClassDB.Stipendio
import dbfactory.operation.StipendioOperation
import messagecodes.StatusCodes
import org.scalatest._
import utils.StartServer

import scala.concurrent.Future

class TestStipendi extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer{
  import StipendioOperationTestValue._
  behavior of "Stipendi"
  it should "return the the given list when getting stipendi for persona with id 2" in {
    val stipendiReq: Future[Option[List[Stipendio]]] = StipendioOperation.getstipendiForPersona(2)
      stipendiReq map {list => assert(list.head.equals(stipendiId2))}
  }
  it should "return StatusCodes.SUCCES_CODE when computing a month computable" in {
    val stipendiReq: Future[Option[Int]] = StipendioOperation.calculateStipendi(goodDateToCompute)
    stipendiReq map {list => assert(list.head == StatusCodes.SUCCES_CODE)}
  }
  it should "return StatusCodes.ERROR_CODE1 when computing a month alredy computed" in {
    val stipendiReq: Future[Option[Int]] = StipendioOperation.calculateStipendi(goodDateToCompute)
    stipendiReq map {list => assert(list.head == StatusCodes.ERROR_CODE1)}
  }

}
