import caseclass.CaseClassDB.{Contratto, Terminale}
import dbfactory.operation.{ContrattoOperation, TerminaleOperation}
import org.scalatest._
import utils.StartServer

import scala.concurrent.Future

trait Init2{
  protected var terminale: List[Terminale] = _
}

class TestTerminale extends  AsyncFlatSpec with BeforeAndAfterEach with Init2 with StartServer{

  override def beforeEach(): Unit = {
    terminale = List(Terminale("Cansas",1,Some(1)),Terminale("Pajaritos",1,Some(5)))
  }

  behavior of "getTerminaleInZona"
  it should "eventually return a list of terminali with length of 2 " in {
    val futureTerminali: Future[Option[List[Terminale]]] = TerminaleOperation.getTermininaliInZona(1)
    futureTerminali map {terminale => assert(terminale.head.length == 2)}
  }
  it should "eventually return a list of terminali equals to the provided list " in {
    val futureTerminali: Future[Option[List[Terminale]]] = TerminaleOperation.getTermininaliInZona(1)
    futureTerminali map {t => assert(t.head.equals(terminale))}
  }
  it should "eventually return a list of terminali with length of 1 " in {
    val futureTerminali: Future[Option[List[Terminale]]] = TerminaleOperation.getTermininaliInZona(3)
    futureTerminali map {terminale => assert(terminale.head.length == 1)}
  }
  it should "return None when zona is wrong " in {
    val futureTerminali: Future[Option[List[Terminale]]] = TerminaleOperation.getTermininaliInZona(30)
    futureTerminali map {terminale => assert(terminale.isEmpty)}
  }
}
