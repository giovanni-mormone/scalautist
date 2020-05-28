package dbfactory.operation
import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.{Login, Persona}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstancePersona
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{PersonaTableQuery, TerminaleTableQuery}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
trait PersonaOperation extends OperationCrud[Persona]{
  def filterByName(name:String):Future[Option[List[Persona]]]
  def filterBySurname(surname: String): Future[Option[List[Persona]]]
  def monadicInnerJoin():Unit
  def login(login:Login):Future[Option[Persona]]

}
object PersonaOperation extends PersonaOperation {

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
  override def monadicInnerJoin(): Unit = {
    val monadicInnerJoin = for {
      c <- PersonaTableQuery.tableQuery()
      s <- TerminaleTableQuery.tableQuery()
      if c.id===s.id
    } yield (c.nome,s.nomeTerminale)

    InstancePersona.operation().execJoin(monadicInnerJoin)  onComplete(t=>{
      println(t)
    })
  }
  override def login(login: Login): Future[Option[Persona]] = {
    val promiseLogin = Promise[Option[Persona]]
    InstancePersona.operation().execQueryFilter(f => (f.nome, f.cognome, f.numTelefono, Option[String](""), f.ruolo, f.terminaleId, f.id.?), x => x.nome === login.user && x.password === login.password)
      .onComplete{
      case Success(value) if value.nonEmpty => promiseLogin.success(convertTupleToPerson(Some(value.head)))
      case Success(_) =>promiseLogin.success(None)
      case Failure(exception) => promiseLogin.failure(exception)
    }
    promiseLogin.future
  }
  private def convertTupleToPerson(persona:Option[(String, String, String,Option[String], Int,Option[Int], Option[Int])]):Option[Persona] = persona.map(value =>Persona.apply _ tupled value)
  def changePassword():Future[Option[Int]]={//query update all values that match with filter
    val changePassword = Promise[Option[Int]]
    InstancePersona.operation().execQueryUpdate(f => (f.nome, f.cognome), x => x.nome === "Fabian" && x.password === "912345",("Pedro","Sanchez"))
      .onComplete{
        case Success(value) if value==1 => changePassword.success(Some(value))
        case Success(value) =>changePassword.success(Some(value))
        case Failure(exception) => changePassword.failure(exception)
      }
    changePassword.future
  }
}
object ttt extends App{//verificar changepassword
  PersonaOperation.monadicInnerJoin()
  val per = Persona("Fabian","Aspee","569918598",Some("912345"),1,None,Some(8))
  PersonaOperation.update(per)onComplete{
    case Success(value) => println(value)
    case Failure(exception) => println(exception)
  }
  PersonaOperation.changePassword() onComplete{
    case Success(value) => println(value)
    case Failure(exception) => println(exception)
  }
  while (true){}
}