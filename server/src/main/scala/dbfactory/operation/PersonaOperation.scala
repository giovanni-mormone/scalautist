package dbfactory.operation
import java.sql.Date

import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.{Login, Persona, StoricoContratto}
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword, Ferie}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstancePersona, InstanceStoricoContratto}
import dbfactory.implicitOperation.OperationCrud

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dbfactory.util.Helper._

/** @author Fabian Aspée Encina, Giovanni Mormone
 *  Trait which allows to perform operations on the person table,
 *  this trait have methods that represent monadic join
 */
trait PersonaOperation extends OperationCrud[Persona]{
  /**
   * method that implement filter in database by name of the person
   * @param name name of a persona
   * @return Option of List of Persona with all fields full, only password is empty
   */
  def filterByName(name:String):Future[Option[List[Persona]]]

  /**
   * method that implement filter in database by surname of the person
   * @param surname surname of a persona
   * @return Option of List of Persona with all fields full, only password is empty
   */
  def filterBySurname(surname: String): Future[Option[List[Persona]]]

  /**
   * Method which allow login a user in the system
   * @param login case class with username and password for login in the system
   * @return Option of person with all fields full, only password is empty
   */
  def login(login:Login):Future[Option[Persona]]

  /**
   * Method which allow change password a user in the system
   * @param changePassword case class with iduser, oldpassword and newpassword for change password in the system
   * @return Option of Int that represent status of the operation
   */
  def changePassword(changePassword: ChangePassword):Future[Option[Int]]

  /**
   * Method which enable recovery password when you forget this
   * @param idUser represent identificator of user in table Persona
   * @return case class Login with new password and user = [[EMPTY_STRING]]
   */
  def recoveryPassword(idUser:Int):Future[Option[Login]]

  /**
   * Method to hire a person, it can differentiate beetween roles of the person to hire and
   * generate the relative dependences in the db, e.g. assuming a driver add in the db the
   * type of contract and eventually the disponibility of the driver to do extra work.
   * @param personaDaAssumere
   *                          The data of the person to hire
   * @return
   *         An option with the login data of the hired person
   */
  def assumi(personaDaAssumere: Assumi): Future[Option[Int]]
}

object PersonaOperation extends PersonaOperation {
  private val generator = scala.util.Random.alphanumeric

  private val selectPersone: (PersonaTableRep => Rep[Boolean]) => Future[Option[List[Persona]]] =
    filter =>  InstancePersona.operation().selectFilter(filter)

  private val createString:Option[String]= Some(generator.take(10).map(c => EMPTY_STRING.concat(c.toString)).mkString)

  override def filterByName(name: String): Future[Option[List[Persona]]] =
    selectPersone(x => x.nome === name)

  override def filterBySurname(surname: String): Future[Option[List[Persona]]] =
    selectPersone(x => x.cognome === surname)

  override def login(login: Login): Future[Option[Persona]] =
    InstancePersona.operation().
        execQueryFilter(personaSelect, x => x.userName === login.user && x.password === login.password)
      .collect{
        case Some(value) if value.nonEmpty=> convertTupleToPerson(Some(value.head))
        case Some(List()) => None
      }

  override def changePassword(changePassword: ChangePassword):Future[Option[Int]]=
    InstancePersona.operation().
        execQueryUpdate(f =>(f.password,f.isNew), x => x.id===changePassword.id && x.password===changePassword.oldPassword,(changePassword.newPassword,false))

  override def recoveryPassword(idUser: Int): Future[Option[Login]] =
    InstancePersona.operation().
      execQueryUpdate(f => (f.password, f.isNew), x => x.id === idUser, (createString.head, true))
      .collect {
      case Some(_) => Some(Login(EMPTY_STRING,createString.head))
      case None => None
    }

  override def assumi(personaDaAssumere:Assumi): Future[Option[Int]] = {
    if(personaDaAssumere.persona.ruolo == 3 && personaDaAssumere.disponibilita.isDefined){
      DisponibilitaOperation.insert(personaDaAssumere.disponibilita.head)
        .flatMap(dispId => {
          val persona = constructPersona(personaDaAssumere.persona,dispId)
          insertPersona(persona,personaDaAssumere.storicoContratto)
        }
      )
    } else {
      val persona = constructPersona(personaDaAssumere.persona,None)
      insertPersona(persona,personaDaAssumere.storicoContratto)
    }
  }

  override def delete(element:Int): Future[Option[Int]] = {
    StoricoContrattoOperation.deleteAllStoricoForPerson(element).flatMap(_ => super.delete(element))
  }

  override def deleteAll(element: List[Int]): Future[Option[Int]] = {
    StoricoContrattoOperation.deleteAllStoricoForPersonList(element).flatMap(_ => super.deleteAll(element))
  }

  private def insertPersona(persona: Persona,contratto:StoricoContratto): Future[Option[Int]] = {
    insert(persona)
      .flatMap(idPersona => StoricoContrattoOperation.insert(constructContratto(contratto,idPersona)))
    
  }

  private def constructContratto(contratto:StoricoContratto, personaId:Option[Int]): StoricoContratto = {
    StoricoContratto(contratto.dataInizio,None,personaId,contratto.contrattoId,contratto.turnoId,contratto.turnoId1)
  }

  private def constructPersona(origin: Persona, disponibilita: Option[Int]): Persona =
    Persona(origin.nome,origin.cognome,origin.numTelefono,
      createString,origin.ruolo,origin.isNew,createString.head,origin.idTerminale,disponibilita)
}
