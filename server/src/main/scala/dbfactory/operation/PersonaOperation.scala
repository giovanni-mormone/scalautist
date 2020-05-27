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
    val promiseFilterBySurname = Promise[Option[List[Persona]]]
    execFilter(promiseFilterBySurname,x => x.nome===login.user && x.password===login.password)
    promiseFilterBySurname.future.map(definePerson)
  }
  private def definePerson(persona: Option[List[Persona]]):Option[Persona]={
    if(persona.isDefined){
      val personaImp = persona.get.head
      Some(Persona(personaImp.nome,personaImp.cognome,
        personaImp.numTelefono,"",personaImp.ruolo,personaImp.idTerminale,personaImp.matricola))
    }else  None
  }
}
