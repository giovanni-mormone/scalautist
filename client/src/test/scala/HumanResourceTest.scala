
import java.sql.Date

import akka.actor.Terminated
import caseclass.CaseClassDB.{Contratto, Disponibilita, Login, Persona, StoricoContratto, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import model.utils.ModelUtils._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AsyncFlatSpec
import model.entity.{HumanResourceModel, PersonaModel}
import utils.ClientAkkaHttp

import scala.concurrent.Future

class HumanResourceTest extends AsyncFlatSpec with BeforeAndAfterEach with ClientAkkaHttp {
  var terminale:HumanResourceModel=_
  var persona:PersonaModel=_
  protected var insertPersona: Assumi = _
  val zona:Zona = Zona("Cesena",Some(1))
  val personaC: Persona =Persona("Fabian","Aspee","569918598",Some(""),1,isNew = true,"admin",None,None,Some(1))
  override def beforeEach(): Unit = {
    terminale = HumanResourceModel()
    persona = PersonaModel()
    //val persona:Persona = Persona("Conducente","Maestro","91485236",Some(""),3,isNew = true,"tutu2",Some(3),Some(1),Some(6))

    val daAssumere:Persona = Persona("JuanitoS","PerezS","569918598",Some(""),3,isNew = true,"")
    val contratto:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,1,Some(1),Some(2))
    val disp:Disponibilita = Disponibilita("Lunes","Sabato")
    insertPersona = Assumi(daAssumere,contratto,Some(disp))
  }

  behavior of "contract"
  it should "return login with credential of a person" in {
    val futureRecruit:Future[Option[Login]]=terminale.recruit(insertPersona)
    futureRecruit map { recruit => assert(recruit.isDefined)}
  }
  it should "return  a person" in {
    val futureSecondLogin: Future[Option[Persona]] = persona.login("admin","admin")
    futureSecondLogin map { login => assert(login.contains(personaC)) }
  }
  it should "eventually return None whit login error" in {
    val futureLogin: Future[Option[Persona]] = persona.login("persona","prsona")
    futureLogin map { login => assert(login.isEmpty) }
  }
  it should "return ok when delete person" in {
    val futureDelete:Future[Option[Int]]=terminale.fires(6) 
    futureDelete map { recruit => assert(recruit.contains(410))}

  }
  it should "return list of terminal lenght 2" in {
    val futureTerminale:Future[Option[List[Terminale]]]=terminale.getTerminalByZone(1)
    futureTerminale map { terminale => assert(terminale.head.length==2)}
  }
  it should "return type contract with length 8" in {
    val futureContract:Future[Option[List[Contratto]]]=terminale.getAllContract
    futureContract map { contract => assert(contract.head.length==8)}
  }
  it should "return all shift with length 6" in {
    val futureshift:Future[Option[List[Turno]]]=terminale.getAllShift
    futureshift map { shift => assert(shift.head.length==6)}
  }
  it should "return a list of zone length 4 when get operation" in {
    val futureZona:Future[Option[List[Zona]]]=terminale.getAllZone
    futureZona map { zona => assert(zona.head.length==4)}
  }
  it should "shutdown System" in {
    val futureTerminated:Future[Terminated]=terminale.shutdownActorSystem()
    futureTerminated map { terminated => assert(true)}
  }
}
