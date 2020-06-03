package dbfactory.setting
import dbfactory.implicitOperation.Crud
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.runtime.{universe => runtime}

/**
 * @author Fabian Aspée Encina
 * Generic Crud is a trait which enables operation generic in all tables
 * @tparam C case class that represent instance in database [[caseclass.CaseClassDB]]
 * @tparam T class that represent the table in database, allow make query in database [[dbfactory.table]]
 */
sealed trait GenericCRUD[C,T <: GenericTable[C]] extends GenericTableQuery[C,T] with DB[C,T] with Crud[C]{
  /**
   *  A possibly parameterized query that will be cached for repeated efficient execution without having to recompile it every time
   */
  protected val queryById = Compiled((id: Rep[Int]) => tableDB().filter(_.id === id))
  /**
   *  Query per make filter using clause ''in'' of SQL
   */
  protected val queryByIdPlus: Seq[Int] => Query[T, C, Seq] = (id: Seq[Int]) => tableDB().filter(_.id.inSet(id))

}
object GenericCRUD{
  case class GenericOperationCRUD[C,T<: GenericTable[C]:runtime.TypeTag]() extends GenericCRUD[C,T] {
    override def selectAll: Future[Option[List[C]]] = super.run(tableDB().result).map(t => Option(t.toList))
    override def select(id: Int): Future[Option[C]] = super.run(queryById(id).result.headOption)
    override def insert(element: C): Future[Option[Int]]= super.run((tableDB() returning tableDB().map(_.id)) += element).map(t => Option(t))
    override def insertAll(element: List[C]): Future[Option[List[Int]]] =  super.run((tableDB returning tableDB().map(_.id)) ++= element).map(t =>Option(t.toList))
    override def delete(id: Int): Future[Option[Int]] = super.run(queryById(id).delete).map(t=> Option(t))
    override def deleteAll(id: List[Int]): Future[Option[Int]] = super. run(queryByIdPlus(id).delete).map(t => Option(t))
    override def update(element: C): Future[Option[Int]] = super.run((tableDB() returning tableDB().map(_.id)).insertOrUpdate(element))
  }
}

