import caseclass.CaseClassDB.{Login, Persona}
import dbfactory.operation.PersonaOperation
import org.scalatest._
import DatabaseHelper._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
trait Init{
  protected var persona:Persona = _
  protected var login:Login =_
}
class TestPersona  extends  AsyncFlatSpec with BeforeAndAfterEach with Init{
  override def beforeEach(): Unit = {
    val result = 1//Await.result(runScript(),Duration.Inf)
    require(result==1)
    login = Login("Fabian","admin")
    persona=Persona("Fabian","Aspee","569918598","",1,None,Some(1))
  }
  behavior of "Login"
  it should "eventually a person log in" in {
    val futureLogin: Future[Option[Persona]] = PersonaOperation.login(login)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    futureLogin map { login => assert(login.getOrElse() == persona) }
  }
  it should "eventually return 0" in {
    val futureLogin: Future[Option[Persona]] = PersonaOperation.login(Login("persona","persona"))
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    futureLogin map { login => assert(login.isEmpty) }
  }
}
