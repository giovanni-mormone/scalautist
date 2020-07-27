package dbfactory.operation
import java.sql.Date

import caseclass.CaseClassDB.{Disponibilita, Login, Persona, StoricoContratto}
import caseclass.CaseClassHttpMessage.{Assumi, ChangePassword}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.{InstanceDisponibilita, InstancePersona}
import dbfactory.implicitOperation.OperationCrud
import dbfactory.table.PersonaTable.PersonaTableRep
import dbfactory.util.{CheckPassword, HashPassword}
import dbfactory.util.Helper._
import messagecodes.StatusCodes
import persistence.ConfigEmitterPersistence
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.Future
import scala.util.{Failure, Success}

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
   * @param personToHire
   *                          The data of the person to hire
   * @return
   * An option with the id of the hired person or an error code:
   * [[messagecodes.StatusCodes.ERROR_CODE1]] if the contratto provided not exits in the db
   * [[messagecodes.StatusCodes.ERROR_CODE2]] if the contratto not allows for disponibilita to be defined but the disponibilita is present
   * [[messagecodes.StatusCodes.ERROR_CODE3]] if the contratto is not fisso but there is a disponibilità present
   * [[messagecodes.StatusCodes.ERROR_CODE4]] if the contratto is fisso but there is not a disponibilità present
   * [[messagecodes.StatusCodes.ERROR_CODE5]] if the turni are not right according to the contratto
   */
  def assumi(personToHire: Assumi): Future[Option[Int]]
}

object PersonaOperation extends PersonaOperation {
  private val generator = scala.util.Random.alphanumeric
  private val CODICE_CONDUCENTE: Int = 3
  private val LAST_TURNO: Int = 6
  private val FIRST_TURNO: Int = 1
  private val notificationEmitter = ConfigEmitterPersistence("persona_emitter","licenzia","assumi")
  notificationEmitter.start()
  private val selectPersone: (PersonaTableRep => Rep[Boolean]) => Future[Option[List[Persona]]] =
    filter =>  InstancePersona.operation().selectFilter(filter)

  private val createString:Option[String]= Some(generator.take(10).map(c => EMPTY_STRING.concat(c.toString)).mkString)

  override def filterByName(name: String): Future[Option[List[Persona]]] =
    selectPersone(x => x.nome === name)

  override def filterBySurname(surname: String): Future[Option[List[Persona]]] =
    selectPersone(x => x.cognome === surname)

  override def login(login: Login): Future[Option[Persona]] = {
    InstancePersona.operation().selectFilter(user=>user.userName===login.user).collect {
      case Some(List(value)) if CheckPassword(login.password,value.password.toList.foldLeft(EMPTY_STRING)((_,actual)=>actual)) =>
        Some(value)
      case _ => None
    }
  }

  override def changePassword(changePassword: ChangePassword):Future[Option[Int]]= {
    InstancePersona.operation().selectFilter(user=>user.id===changePassword.id).flatMap {
      case Some(List(value)) if CheckPassword(changePassword.oldPassword,value.password.toList.foldLeft(EMPTY_STRING)((_,actual)=>actual)) =>
        this.update(value.copy(password = HashPassword(Option(changePassword.newPassword)),isNew=false)).collect {
          case None =>Some(StatusCodes.SUCCES_CODE)
          case _ => None
        }
      case None => Future.successful(None)
    }

  }

  override def recoveryPassword(idUser: Int): Future[Option[Login]] ={
    val generatedPass = createString
    val newPass = HashPassword(generatedPass).toList.foldLeft(EMPTY_STRING)((_,actual)=>actual)
    for{
      _ <-InstancePersona.operation().execQueryUpdate(f => (f.password, f.isNew), x => x.id === idUser, (newPass, true))
      credentials <- select(idUser).map(_.map(user => generatedPass match{
        case Some(x) => Login(user.userName,x)
        case _ => Login(EMPTY_STRING,EMPTY_STRING)
      }).filter(!_.user.equalsIgnoreCase(EMPTY_STRING)))
    }yield credentials
  }

  override def assumi(personToHire:Assumi): Future[Option[Int]] = {
    val persona = personToHire.persona
    val disponibilita = personToHire.disponibilita
    val newContratto = personToHire.storicoContratto
    val dispListToDispId: Option[List[Disponibilita]] => Option[Int] = {
      case Some(List(value)) => value.idDisponibilita
      case None => None
    }
    InstancePersona.operation().selectFilter(filter => filter.nome === persona.nome && filter.cognome === persona.cognome).flatMap(result =>{
      val nPersone = result.toList.flatten.length
      ContrattoOperation.select(newContratto.contrattoId).flatMap{
        case None => completeCall(StatusCodes.ERROR_CODE1)
        case Some(_) if disponibilita.isDefined && persona.ruolo != CODICE_CONDUCENTE => completeCall(StatusCodes.ERROR_CODE2)
        case Some(contratto) if disponibilita.isDefined && !contratto.turnoFisso =>completeCall(StatusCodes.ERROR_CODE3)
        case Some(contratto) if disponibilita.isEmpty && contratto.turnoFisso => completeCall(StatusCodes.ERROR_CODE4)
        case Some(contratto) if contratto.turnoFisso && wrongTurni(contratto.partTime,newContratto.turnoId,newContratto.turnoId1) =>completeCall(StatusCodes.ERROR_CODE5)
        case Some(_) if persona.ruolo != CODICE_CONDUCENTE || disponibilita.isEmpty => insertPersona(constructPersona(persona,None,nPersone),newContratto)
        case Some(_) => disponibilita.map(disp => InstanceDisponibilita.operation()
          .selectFilter(filter => filter.settimana === disp.settimana &&
            (filter.giorno1 === disp.giorno1 && filter.giorno2 === disp.giorno2 ||
              filter.giorno1 === disp.giorno2 && filter.giorno2 === disp.giorno1)).
          flatMap(dispId => insertPersona(constructPersona(persona, dispListToDispId(dispId),nPersone), newContratto))).convert()
      }
    })

  }

  override def delete(element: Int): Future[Option[Int]] = {
    super.delete(element).collect{
      case x =>
        notificationEmitter.sendMessage("Licenziato un conducente","licenzia")
        x
    }
  }

  override def deleteAll(element: List[Int]): Future[Option[Int]] = {
    super.deleteAll(element).collect{
      case Some(x) =>
        notificationEmitter.sendMessage("licenziati " + x + " conducenti","licenzia")
        Some(x)
    }
  }

  private def wrongTurni(partTime:Boolean,turno1:Option[Int],turno2:Option[Int]): Boolean = (partTime,turno1,turno2) match{
    case (true, Some(_),None) => false
    case (false,Some(x),Some(y)) if consecutive(x,y) => false
    case _ => true
  }

  private val consecutive: (Int,Int) => Boolean = (t1,t2) =>
    t2 - t1 == 1 || (t1 == LAST_TURNO && t2 == FIRST_TURNO)

  val completeCall: Int => Future[Option[Int]] = value => Future.successful(Some(value))

  private def insertPersona(persona: Persona,contratto:StoricoContratto): Future[Option[Int]] =
    for{
      person <- insert(persona)
      contract <- if(person.isDefined) StoricoContrattoOperation.insert(constructContratto(contratto,person)) else Future.successful(None)
    }yield if (contract.isDefined) {
      notificationEmitter.sendMessage("Inserita persona, il tizio si chiama " + persona.nome + " " + persona.cognome,"assumi")
      person
    } else None


  private def constructContratto(contratto:StoricoContratto, personaId:Option[Int]): StoricoContratto = {
    StoricoContratto(contratto.dataInizio,None,personaId,contratto.contrattoId,contratto.turnoId,contratto.turnoId1)
  }

  private def constructPersona(origin: Persona, disponibilita: Option[Int], numeroPersona: Int): Persona =
    Persona(origin.nome,origin.cognome,origin.numTelefono,
      HashPassword(createString),origin.ruolo,origin.isNew,createUserName(origin.nome,origin.cognome,numeroPersona),origin.idTerminale,disponibilita)

  private val createUserName:(String,String,Int) => String = (name,surname,numero) =>
    if(numero == 0) name + surname else name + surname + numero
}
