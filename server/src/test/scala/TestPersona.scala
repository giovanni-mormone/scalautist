import caseclass.CaseClassDB.{Login, Persona}
import dbfactory.operation.PersonaOperation
import org.scalatest._
import DatabaseHelper._
import caseclass.CaseClassHttpMessage.ChangePassword

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Init{
  protected var persona:Persona = _
  protected var persona2:Persona = _
  protected var newPersona:Persona = _
  protected var updatePersona:Persona = _
  protected var listNewPerson:List[Persona] = _
  protected var login:Login =_
  protected var changePassword:ChangePassword =_
  val result: Int =1// Await.result(runScript(),Duration.Inf)
  require(result==1)
}

class TestPersona  extends  AsyncFlatSpec with BeforeAndAfterEach with Init{

  override def beforeEach(): Unit = {

    login = Login("Fabian","admin")
    changePassword = ChangePassword(1,"admin","admin")
    persona=Persona("Fabian","Aspee","569918598",Some(""),1,isNew = true,"admin",None,Some(1))
    persona2=Persona("Fabian","Aspee","569918598",Some(""),1,isNew = false,"admin",None,Some(1))
    updatePersona=Persona("Fabian Andres","Aspee Encina","59613026",Some(""),1,isNew = false,"admin",None,Some(1))
    newPersona=Persona("Juanito","Perez","569918598",Some(""),1,isNew = true,"adminF")
    listNewPerson=List(newPersona,Persona("Juanito","Perez","569918598",Some(""),1,isNew = true,"adminF"))
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
    val futureRecoveryPassword: Future[Login] = PersonaOperation.recoveryPassword(1)
    futureRecoveryPassword map { recoveryPassword => assert(recoveryPassword.password.length == 10) }
  }
  behavior of "CRUD"
  it should "return a int when insert into database" in {
    val insertPersona: Future[Int] = PersonaOperation.insert(newPersona)
    insertPersona map { insert => assert(insert.isValidInt) }
  }
  it should "return a List of int lenght 2 when insert into database" in {
    val insertAllPersona: Future[List[Int]] = PersonaOperation.insertAll(listNewPerson)
    insertAllPersona map { insertAll => assert(insertAll.length == 2) }
  }
  it should "return a person when select for id" in {
    val selectPersona: Future[Option[Persona]] = PersonaOperation.select(persona2.matricola.get)
    selectPersona map {persona =>  assert(persona.get == this.persona2) }
  }
  it should "return a List of Person when selectAll" in {
    val selectAllPersona: Future[List[Persona]] = PersonaOperation.selectAll
    selectAllPersona map { selectAll => assert(selectAll.length == 8) }
  }
  it should "return a int when update a person for id" in {
    val updatePersonaP: Future[Int] = PersonaOperation.update(updatePersona)
    updatePersonaP map {update => assert(update == 1) }
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
