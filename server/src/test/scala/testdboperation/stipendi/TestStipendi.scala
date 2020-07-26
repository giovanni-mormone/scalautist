package testdboperation.stipendi

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.StipendioInformations
import dbfactory.operation.StipendioOperation
import messagecodes.StatusCodes
import org.scalatest._
import utils.StartServer
import org.scalatest.flatspec.AsyncFlatSpec
import scala.concurrent.Future

class TestStipendi extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer{
  import StipendioOperationTestValue._
  behavior of "GetStipendiPersona"
  it should "return the the given list when getting stipendi for persona with id 2" in {
    val stipendiReq: Future[Option[List[Stipendio]]] = StipendioOperation.getstipendiForPersona(2)
      stipendiReq map {list => assert(list.head.equals(stipendiId2))}
  }
  it should "return None when asking for the stipendi of Persone not in the DB" in {
    val stipendiReq: Future[Option[List[Stipendio]]] = StipendioOperation.getstipendiForPersona(76)
    stipendiReq map {list => assert(list.isEmpty)}
  }
  behavior of "CalculateStipendi"
  it should "return StatusCodes.SUCCES_CODE when computing a month computable" in {
    val stipendiReq: Future[Option[Int]] = StipendioOperation.calculateStipendi(goodDateToCompute)
    stipendiReq map {list => assert(list.head == StatusCodes.SUCCES_CODE)}
  }
  it should "return StatusCodes.ERROR_CODE1 when computing a month alredy computed" in {
    val stipendiReq: Future[Option[Int]] = StipendioOperation.calculateStipendi(goodDateToCompute)
    stipendiReq map {list => assert(list.head == StatusCodes.ERROR_CODE1)}
  }
  it should "return StatusCodes.ERROR_CODE2 when computing a month that has no presenza for it's last day" in {
    val stipendiReq: Future[Option[Int]] = StipendioOperation.calculateStipendi(badDateToCompute)
    stipendiReq map {list => assert(list.head == StatusCodes.ERROR_CODE2)}
  }
  behavior of "GetStipendioInformation"
  it should "return the good InformazioniStipendio when when getting the info for the stipendio" in {
    val stipendiReq: Future[Option[StipendioInformations]] = StipendioOperation.getStipendioInformations(3)
    stipendiReq map {list => println("INFO "+ list);assert(list.head.equals(stipendiInfoStip3))}
  }
  it should "return None when searching for informations of a stipendio not present in the db" in {
    val stipendiReq: Future[Option[StipendioInformations]] = StipendioOperation.getStipendioInformations(65)
    stipendiReq map {list => println("INFO "+ list);assert(list.isEmpty)}
  }
}
