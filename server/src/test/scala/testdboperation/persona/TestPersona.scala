package testdboperation.persona

import caseclass.CaseClassDB._
import dbfactory.operation.PersonaOperation
import messagecodes.StatusCodes
import org.scalatest._
import utils.StartServer
import org.scalatest.flatspec.AsyncFlatSpec
import scala.concurrent.Future

class TestPersona  extends  AsyncFlatSpec with BeforeAndAfterEach with StartServer{
  import LoginAndCrudValues._
  import testdboperation.assenza.AssumiOperationTestsValues._

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
    selectPersona map {pers =>  assert(pers.get == persona) }
  }
  it should "return a List of Person when selectAll" in {
    val selectAllPersona: Future[Option[List[Persona]]] = PersonaOperation.selectAll
    selectAllPersona map { selectAll => assert(selectAll.head.length == 11) }
  }
  it should "return a None when update a person for id" in {
    val updatePersonaP: Future[Option[Int]] = PersonaOperation.update(updatePersona)
    updatePersonaP map {update => assert(update.isEmpty) }
  }
  behavior of "FilterBy"
  it should "return a List of Persona with length 1 and nome Matto when searchs in the db with that name" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("MAtto")
    searchName map {list => assert(list.head.length == 1 && list.head.head.nome.equals("MAtto"))}
  }
  it should "return None when search by name not present in the db" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("Gevgvtia")
    searchName map {list => assert(list.isEmpty)}
  }
  it should "return a List of Persona with length 1 and cognome Mattesi when searchs in the db with that surname" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterBySurname("Mattesi")
    searchName map {list => assert(list.head.length == 1 && list.head.head.cognome.equals("Mattesi"))}
  }
  behavior of "Delete and DeleteAll"
  it should "return a 1 int when removed from db" in {
    val fire: Future[Option[Int]] = PersonaOperation.delete(6)
    fire map {login => assert(login.head == 1)}
  }
  it should "return None if not finds the person to delete" in {
    val fire: Future[Option[Int]] = PersonaOperation.delete(26)
    fire map {login => assert(login.isEmpty)}
  }
  it should "return a 2 when removes a list of 2 persons present db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(7,8))
    fire map {login => assert(login.head == 2)}
  }
  it should "return a None when tries to remove a list of persons but not all are present in the db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(80,1))
    fire map {login => assert(login.head == 1)}
  }
  it should "return None when removes a list of 2 persons not present in the db" in {
    val fire: Future[Option[Int]] = PersonaOperation.deleteAll(List(101,80))
    fire map {login => assert(login.isEmpty)}
  }
  behavior of "Assumi"
  it should "return None when search by name before inserting in the database" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterBySurname("Mattia")
    searchName map {list => assert(list.isEmpty)}
  }
  it should "return None when search by surname not present in the db" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterBySurname("Gevgvtia")
    searchName map {list => assert(list.isEmpty)}
  }
  it should "return the id of the assumed user when good parameters inserted" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaGood)
    assumi map {login => assert(login.head == 12)}
  }
  it should "return a list of length 1 when search by name after inserting in the database" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("Mattia")
    searchName map {list => assert(list.head.length == 1 && list.head.head.nome.equals("Mattia"))}
  }
  it should "return the id of the assumed user when inserting it a second time (omonimous people)" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaGood)
    assumi map {login => assert(login.head == 13)}
  }
  it should "return a list of length 2 when search by name after inserting in the database the same person another time" in {
    val searchName: Future[Option[List[Persona]]] = PersonaOperation.filterByName("Mattia")
    searchName map {list => assert(list.head.length == 2 && list.head.head.nome.equals("Mattia"))}
  }
  it should "return an ERROR_CODE1 when tries to insert a persona but the contratto not exist in the db" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaNoSuchContratto)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE1)}
  }
  it should "return an ERROR_CODE2 when tries to insert a persona that is not a conducente giving a disponibilita for him" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadRuoloForDisp)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE2)}
  }
  it should "return an ERROR_CODE3 when tries to insert a persona that has a turno rotatorio but disponibilita is defined" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE3)}
  }
  it should "return an ERROR_CODE4 when tries to insert a persona that has a turno fisso but no disponibilita is defined" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno2)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE4)}
  }
  it should "return an ERROR_CODE5 when tries to insert a persona that has a turno fisso partTime but 2 disponibilità" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno3)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an ERROR_CODE5 when tries to insert a persona that has a turno fisso fullTime but 1 disponibilità" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno4)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an ERROR_CODE5 when tries to insert a persona that has a turno fisso partTime but second disponibilità defined" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno5)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an ERROR_CODE5 when tries to insert a persona that has a turno fisso fulltime but disponibilita are not consecutive" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno6)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an ERROR_CODE5 when tries to insert a persona that has a turno fisso fulltime but disponibilita are inverted" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaBadDispForTurno7)
    assumi map {status => assert(status.head == StatusCodes.ERROR_CODE5)}
  }
  it should "return an number >0 when tries to insert a persona that has a turno fisso parttime well defined" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaGoodPartTimeFisso)
    assumi map {status => assert(status.head == 14 )}
  }
  it should "return an number >0 when tries to insert a persona that has a turno fisso fulltime with last and first turno" in {
    val assumi: Future[Option[Int]] = PersonaOperation.assumi(insertPersonaGoodFullTimeFissoHeadTail)
    assumi map {status => assert(status.head == 15)}
  }
}
