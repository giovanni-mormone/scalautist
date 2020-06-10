import java.sql.Date

import caseclass.CaseClassDB.{Assenza, Disponibilita, Login, Persona, StoricoContratto, Zona}
import dbfactory.operation.{AssenzaOperation, PersonaOperation, ZonaOperation}
import org.scalatest._
import utils.StartServer
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword}

import scala.concurrent.Future

trait Init{
  protected var persona:Persona = _
  protected var persona2:Persona = _
  protected var newPersona:Persona = _
  protected var updatePersona:Persona = _
  protected var listNewPerson:List[Persona] = _
  protected var login:Login =_
  protected var changePassword:ChangePassword =_
  protected var insertPersona: Assumi = _
  protected var assenza:Assenza = _

}

class TestPersona  extends  AsyncFlatSpec with BeforeAndAfterEach with Init with StartServer{

  override def beforeEach(): Unit = {

    login = Login("admin","admin")
    changePassword = ChangePassword(1,"admin","admin")
    persona=Persona("Fabian","Aspee","569918598",Some(""),1,isNew = true,"admin",None,None,Some(1))
    persona2=Persona("Fabian","Aspee","569918598",Some(""),1,isNew = false,"admin",None,None,Some(1))
    updatePersona=Persona("Fabian Andres","Aspee Encina","59613026",Some(""),1,isNew = false,"admin",None,None,Some(1))
    newPersona=Persona("Juanito","Perez","569918598",Some(""),1,isNew = true,"adminF")
    listNewPerson=List(newPersona,Persona("Juanito","Perez","569918598",Some(""),1,isNew = true,"adminF"))

    val daAssumere:Persona = Persona("Mattia","Mommo","1234567789",None,3,isNew = true,"",Some(2))
    val contratto:StoricoContratto = StoricoContratto(new Date(System.currentTimeMillis()),None,None,2,Some(3),Some(4))
    val disp:Disponibilita = Disponibilita("Lunes","Martes")
    insertPersona = Assumi(daAssumere,contratto,Some(disp))

    assenza = Assenza(2,new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()*2),malattia = false)


  }
  behavior of "Login"
  it should "eventually return a person" in {
    val futureLogin: Future[Option[Persona]] = PersonaOperation.login(login)
    futureLogin map { login => assert(login.head == persona) }
  }
  it should "eventually return a Option Int = 1 when change password" in {
    val futureChangePassword: Future[Option[Int]] = PersonaOperation.changePassword(changePassword)
    futureChangePassword map { changePassword => assert(changePassword.head == 1) }
  }
  it should "eventually return a person whit isNew = false" in {
    val futureSecondLogin: Future[Option[Persona]] = PersonaOperation.login(login)
    futureSecondLogin map { login => assert(!login.head.isNew) }
  }
  it should "eventually return None whit login error" in {
    val futureLogin: Future[Option[Persona]] = PersonaOperation.login(Login("persona","persona"))
    futureLogin map { login => assert(login.isEmpty) }
  }
  it should "eventually return Login with new password" in {
    val futureRecoveryPassword: Future[Option[Login]] = PersonaOperation.recoveryPassword(1)
    futureRecoveryPassword map { recoveryPassword => assert(recoveryPassword.head.password.length == 10) }
  }
  it should "eventually  Login with new password" in {
    val futureZone: Future[Option[Int]] = ZonaOperation.update(Zona("Cesena2",Some(5)))
    futureZone map { zone => assert(zone.isDefined) }
  }
  behavior of "CRUD"
  it should "return a int when insert into database" in {
    val insertPersona: Future[Option[Int]] = PersonaOperation.insert(newPersona)
    insertPersona map { insert => assert(insert.isDefined) }
  }
  it should "return a List of int lenght 2 when insert into database" in {
    val insertAllPersona: Future[Option[List[Int]]] = PersonaOperation.insertAll(listNewPerson)
    insertAllPersona map { insertAll => assert(insertAll.head.length == 2) }
  }
  it should "return a person when select for id" in {
    val selectPersona: Future[Option[Persona]] = PersonaOperation.select(persona.matricola.get)
    selectPersona map {persona =>  assert(persona.get == this.persona) }
  }
  it should "return a List of Person when selectAll" in {
    val selectAllPersona: Future[Option[List[Persona]]] = PersonaOperation.selectAll
    selectAllPersona map { selectAll => assert(selectAll.head.length == 11) }
  }
  it should "return a None when update a person for id" in {
    val updatePersonaP: Future[Option[Int]] = PersonaOperation.update(updatePersona)
    updatePersonaP map {update => assert(update.isEmpty) }
  }
  behavior of "PersoneManagment"
  it should "return a login with credential of user" in {
    val assumi: Future[Option[Login]] = PersonaOperation.assumi(insertPersona)
    assumi map {login => assert(login.isDefined)}
  }
  it should "return a login with credential of user2" in {
    val assumi: Future[Option[Login]] = PersonaOperation.assumi(insertPersona)
    assumi map {login => assert(login.isDefined)}
  }
  it should "return a 1 int when removed from db" in {
    val fire: Future[Option[Int]] = PersonaOperation.delete(6)
    fire map {login => assert(login.head == 1)}
  }

  it should "return None if not finds the person to delete" in {
    val fire: Future[Option[Int]] = PersonaOperation.delete(26)
    fire map {login => assert(login.isEmpty)}
  }
  it should "return a List of Persona with length 2 and nome Mattia when searchs in the db with that name" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("Mattia")
    searchName map {list => assert(list.head.length == 2)}
  }
  it should "return None when search by name not present in the db" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("Gevgvtia")
    searchName map {list => assert(list.isEmpty)}
  }
  it should "return a List of Persona with length 1 and cognome Mattesi when searchs in the db with that surname" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterBySurname("Mattesi")
    searchName map {list => assert(list.head.length == 1)}
  }
  it should "return None when search by surname not present in the db" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterBySurname("Gevgvtia")
    searchName map {list => assert(list.isEmpty)}
  }
  it should "return a 2 when removes a list of 2 persons present db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(7,8))
    fire map {login => assert(login.head == 2)}
  }
  it should "return a 1 when removes a list of 2 persons but only 1 present in the db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(1,80))
    fire map {login => assert(login.head == 1)}
  }
  it should "return None when removes a list of 2 persons not present in the db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(101,80))
    fire map {login => assert(login.isEmpty)}
  }

  behavior of "assenza"
  it should "return an int id for the assenza" in {
    val assence: Future[Option[Int]] = AssenzaOperation.insert(assenza)
    assence map {assence => assert(assence.head.isValidInt)}
  }
 /* it should "return a int when delete a person for id" in {
    val deletePersona: Future[Int] = PersonaOperation.delete(persona)
    deletePersona map {delete=>  assert(delete == 1) }
  }
  it should "return a int when delete List a person for id" in {
    val deleteAllPersona: Future[Int] = PersonaOperation.deleteAll(listNewPerson)
    deleteAllPersona map {deleteAll=>  assert(deleteAll == 1) }
  }*/

}
