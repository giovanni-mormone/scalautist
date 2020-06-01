package dbfactory.operation
import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.{Login, Persona, Straordinario}
import caseclass.CaseClassHttpMessage.ChangePassword
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstancePersona
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{PersonaTableQuery, TerminaleTableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
import dbfactory.util.Helper._

/** @author Fabian Aspée Encina
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
   *
   */
  def monadicInnerJoin():Unit

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
  def recoveryPassword(idUser:Int):Future[Login]
}
object PersonaOperation extends PersonaOperation {

  private val generator = scala.util.Random.alphanumeric

  override def filterByName(name: String): Future[Option[List[Persona]]] = {
    val promiseFilterByName = Promise[Option[List[Persona]]]
    execFilter(promiseFilterByName,x => x.nome===name)
    promiseFilterByName.future
  }
  override def filterBySurname(surname: String): Future[Option[List[Persona]]] = {
    val promiseFilterBySurname = Promise[Option[List[Persona]]]
    execFilter(promiseFilterBySurname,x => x.cognome===surname)
    promiseFilterBySurname.future
  }
  private def execFilter(promiseFilterBySurname: Promise[Option[List[Persona]]],f:PersonaTableRep=>Rep[Boolean]): Future[Unit] = Future {
    InstancePersona.operation().selectFilter(f) onComplete {
      case Success(value) if value.nonEmpty=>promiseFilterBySurname.success(Some(value))
      case Success(_) =>promiseFilterBySurname.success(None)
      case Failure(_)=>promiseFilterBySurname.success(None)
    }
  }
  override def insert(element: Persona): Future[Int] ={
    val persona = Persona(element.nome,element.cognome,element.numTelefono,createString,element.ruolo,element.isNew,createString.head)
    super.insert(persona)
  }
  private val createString:Option[String]= Some(generator.take(10).map(c => EMPTY_STRING.concat(c.toString)).mkString)

  override def login(login: Login): Future[Option[Persona]] = {
    val promiseLogin = Promise[Option[Persona]]
    InstancePersona.operation().
      execQueryFilter(personaSelect, x => x.userName === login.user && x.password === login.password)
      .onComplete{
      case Success(value) if value.nonEmpty => promiseLogin.success(convertTupleToPerson(Some(value.head)))
      case Success(_) =>promiseLogin.success(None)
      case Failure(exception) => promiseLogin.failure(exception)
    }
    promiseLogin.future
  }
  override def changePassword(changePassword: ChangePassword):Future[Option[Int]]={
    val changePasswordP = Promise[Option[Int]]
    InstancePersona.operation().
      execQueryUpdate(f =>(f.password,f.isNew), x => x.id===changePassword.id && x.password===changePassword.oldPassword,(changePassword.newPassword,false))
      .onComplete{
        case Success(value) if value==1 => changePasswordP.success(Some(value))
        case Success(value) =>changePasswordP.success(Some(value))
        case Failure(exception) => changePasswordP.failure(exception)
      }
    changePasswordP.future
  }
  override def recoveryPassword(idUser: Int): Future[Login] = {
    val recoveryPasswordP = Promise[Login]
    val newPassword = createString
    InstancePersona.operation().
      execQueryUpdate(f =>(f.password,f.isNew), x => x.id===idUser,(newPassword.head,true))
      .onComplete{
        case Success(value) if value==1 => recoveryPasswordP.success(Login(EMPTY_STRING,newPassword.head))
        case Failure(exception) => recoveryPasswordP.failure(exception)
      }
    recoveryPasswordP.future
  }

  override def monadicInnerJoin(): Unit = {
    val monadicInnerJoin = for {
      c <- PersonaTableQuery.tableQuery()
      s <- TerminaleTableQuery.tableQuery()
      if c.terminaleId===s.id
    } yield (c.nome,s.nomeTerminale)

    InstancePersona.operation().execJoin(monadicInnerJoin)  onComplete(t=>{
      println(t)
    })
  }

}
