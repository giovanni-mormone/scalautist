
import caseclass.CaseClassDB.{Contratto, Terminale, Turno, Zona}
import model.Model
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import model.entity.HumanResourceModel

import scala.concurrent.Future

class HumanResourceTest extends AsyncFlatSpec with BeforeAndAfterEach with Model {
  var terminale:HumanResourceModel=_
  override def beforeEach(): Unit = {
      terminale = HumanResourceModel()
  }
  behavior of "contract"
  it should "return list of terminal lenght 2" in {
    val futureTerminale:Future[Option[List[Terminale]]]=terminale.getTerminalByZone(1)
    futureTerminale map { terminale => assert(terminale.head.length==2)}
  }
  it should "return defined option with zona" in {
    val futureZona:Future[Option[Zona]]=terminale.getZone(1)
    futureZona map { terminale => assert(terminale.isDefined)}
  }
  it should "return type contract with length 8" in {
    val futureContract:Future[Option[List[Contratto]]]=terminale.getAllContract
    futureContract map { contract => assert(contract.head.length==8)}
  }
  it should "return all shift with length 6" in {
    val futureshift:Future[Option[List[Turno]]]=terminale.getAllShift
    futureshift map { shift => assert(shift.head.length==6)}
  }
}
