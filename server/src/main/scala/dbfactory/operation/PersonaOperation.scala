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
import utils.StatusCodes

import scala.concurrent

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
 *
   * @param personaDaAssumere
   *                          The data of the person to hire
   * @return
   * An option with the id of the hired person or an error code:
   * [[utils.StatusCodes.ERROR_CODE1]] if the contratto provided not exits in the db
   * [[utils.StatusCodes.ERROR_CODE2]] if the contratto not allows for disponibilita to be defined but the disponibilita is present
   * [[utils.StatusCodes.ERROR_CODE3]] if the contratto is not fisso but there is a disponibilità present
   * [[utils.StatusCodes.ERROR_CODE4]] if the contratto is fisso but there is not a disponibilità present
   * [[utils.StatusCodes.ERROR_CODE5]] if the turni are not right according to the contratto
   */
  def assumi(personaDaAssumere: Assumi): Future[Option[Int]]
}

object PersonaOperation extends PersonaOperation {
  private val generator = scala.util.Random.alphanumeric
  private val CODICE_CONDUCENTE: Int = 3
  private val LAST_TURNO: Int = 6
  private val FIRST_TURNO: Int = 1

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
        case None => None
        case Some(value)  => value.map(x=>convertTupleToPerson(Some(x))).head

      }

  override def changePassword(changePassword: ChangePassword):Future[Option[Int]]=
    InstancePersona.operation().
        execQueryUpdate(f =>(f.password,f.isNew), x => x.id===changePassword.id && x.password===changePassword.oldPassword,(changePassword.newPassword,false))

  override def recoveryPassword(idUser: Int): Future[Option[Login]] ={
    val newPass = createString.head
    for{
      persona <-InstancePersona.operation().execQueryUpdate(f => (f.password, f.isNew), x => x.id === idUser, (newPass, true))
      credentials <- select(idUser).map(_.map(user => Login(user.userName,newPass)))
    }yield credentials
  }


  override def assumi(personaDaAssumere:Assumi): Future[Option[Int]] = {
    val persona = personaDaAssumere.persona
    val disponibilita = personaDaAssumere.disponibilita
    val newContratto = personaDaAssumere.storicoContratto
    for{
      contratto <- ContrattoOperation.select(newContratto.contrattoId)
      result <- if (contratto.isEmpty) completeCall(StatusCodes.ERROR_CODE1) else if
      (disponibilita.isDefined && persona.ruolo != CODICE_CONDUCENTE) completeCall(StatusCodes.ERROR_CODE2) else if
      (disponibilita.isDefined && !contratto.head.turnoFisso) completeCall(StatusCodes.ERROR_CODE3) else if
      (disponibilita.isEmpty && contratto.head.turnoFisso) completeCall(StatusCodes.ERROR_CODE4) else if
      (contratto.head.turnoFisso && wrongTurni(contratto.head.partTime,newContratto.turnoId,newContratto.turnoId1)) completeCall(StatusCodes.ERROR_CODE5) else if
      (persona.ruolo != CODICE_CONDUCENTE || disponibilita.isEmpty) insertPersona(constructPersona(persona,None),newContratto) else
        for{dispId <- DisponibilitaOperation.insert(disponibilita.head)
            idPers <- insertPersona(constructPersona(persona,dispId),newContratto)} yield idPers
    }yield result
  }

  private def wrongTurni(partTime:Boolean,turno1:Option[Int],turno2:Option[Int]): Boolean = (partTime,turno1,turno2) match{
    case (true, Some(_),None) => false
    case (false,Some(x),Some(y)) if consecutive(x,y) => false
    case _ => true
  }

  private val consecutive: (Int,Int) => Boolean = (t1,t2) =>
    t2 - t1 == 1 || (t1 == LAST_TURNO && t2 == FIRST_TURNO)

  val completeCall: Int => Future[Option[Int]] = value => Future.successful(Some(value))

  /*override def delete(element:Int): Future[Option[Int]] = {
    StoricoContrattoOperation.deleteAllStoricoForPerson(element).flatMap(_ => super.delete(element))
  }

  override def deleteAll(element: List[Int]): Future[Option[Int]] = {
    StoricoContrattoOperation.deleteAllStoricoForPersonList(element).flatMap(_ => super.deleteAll(element))
  }*/

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
