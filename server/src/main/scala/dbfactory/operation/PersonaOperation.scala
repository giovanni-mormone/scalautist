package dbfactory.operation
import dbfactory.table.PersonaTable.PersonaTableRep
import slick.jdbc.SQLServerProfile.api._
import caseclass.CaseClassDB.{Login, Persona}
import dbfactory.implicitOperation.ImplicitInstanceTableDB.InstancePersona
import dbfactory.implicitOperation.OperationCrud
import dbfactory.setting.Table.{PersonaTableQuery, TerminaleTableQuery}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
trait PersonaOperation extends OperationCrud[Persona]{
  def filterByName(name:String):Future[Seq[Persona]]
  def filterBySurname(surname: String): Future[List[Persona]]
  def monadicInnerJoin():Unit
  def login(login:Login):Future[Persona]
}
object PersonaOperation extends PersonaOperation {

  override def filterByName(name: String): Future[List[Persona]] = {
    val promiseFilterByName = Promise[List[Persona]]
    execFilter(promiseFilterByName,x => x.nome===name)
    promiseFilterByName.future

  }
  override def filterBySurname(surname: String): Future[List[Persona]] = {
     val promiseFilterBySurname = Promise[List[Persona]]
    execFilter(promiseFilterBySurname,x => x.cognome===surname)
     promiseFilterBySurname.future
  }
  private def execFilter(promiseFilterBySurname: Promise[List[Persona]],f:PersonaTableRep=>Rep[Boolean]): Future[Unit] = Future {
    InstancePersona.operation().selectFilter(f) onComplete {
      result=>promiseFilterBySurname.success(result.get)
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
  override def login(login: Login): Future[Persona] = {
    val promiseFilterBySurname = Promise[List[Persona]]
    execFilter(promiseFilterBySurname,x => x.nome===login.user && x.cognome===login.password)
    promiseFilterBySurname.future.map(t=>t.head)

  }
}
