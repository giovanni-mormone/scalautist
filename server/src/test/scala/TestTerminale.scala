import DatabaseHelper.runScript
import caseclass.CaseClassDB.{Persona, Terminale}
import dbfactory.operation.{PersonaOperation, TerminaleOperation}
import org.scalatest._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Init2{
  protected var terminale: List[Terminale] = _

  val result: Int = Await.result(runScript(),Duration.Inf)
  require(result==1)
}

class TestTerminale extends  AsyncFlatSpec with BeforeAndAfterEach with Init2{

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
}
