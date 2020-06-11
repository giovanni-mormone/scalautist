package testdboperation.stipendi

import caseclass.CaseClassDB.Stipendio
import dbfactory.operation.StipendioOperation
import org.scalatest._
import utils.StartServer

import scala.concurrent.Future

class TestStipendi extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer{

  behavior of "Stipendi"
  it should "" in {
    val stipendiReq: Future[Option[List[Stipendio]]] = StipendioOperation.getstipendiForPersona(8)
      stipendiReq map {list => println(list);assert(list.isDefined)}
  }

}
