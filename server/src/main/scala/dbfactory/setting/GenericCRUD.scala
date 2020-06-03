package dbfactory.setting
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
sealed trait GenericCRUD[C,T <: GenericTable[C]] extends GenericTableQuery[C,T] with DB[C,T] {
  /**
   *  A possibly parameterized query that will be cached for repeated efficient execution without having to recompile it every time
   */
  protected val queryById = Compiled((id: Rep[Int]) => tableDB().filter(_.id === id))
  /**
   *  Query per make filter using clause ''in'' of SQL
   */
  protected val queryByIdPlus: Seq[Int] => Query[T, C, Seq] = (id: Seq[Int]) => tableDB().filter(_.id.inSet(id))

  /**
   * Generic select that allow select from any table in database
   * @param id represent id in table in database
   * @return Option of type required
   */
  def select(id:Int): Future[Option[C]]

  /**
   * Select All record in the table required
   * @return List of type required
   */
  def selectAll: Future[List[C]]

  /**
   * Generic Insert that allow insert into any table in database
   * @param element case class which represents the object that we want to insert into database
   * @return int that represent status of operation
   */
  def insert(element: C): Future[Int]

  /**
   * Insert all record in the list
   * @param element all record we want insert into database
   * @return int which represent status of operation
   */
  def insertAll(element: List[C]): Future[List[Int]]

  /**
   *  Generic delete by id, delete element into database by your id
   * @param id id which represent element we want delete into database
   * @return int which represent status of operation
   */
  def delete(id:Int): Future[Int]

  /**
   * which [[delete(1)]] but for the list id, that is to say all record in database we want delete
   * @param id list of int we want delete into database
   * @return int which represent status of operation
   */
  def deleteAll(id:List[Int]): Future[Int]

  /**
   *  Generic update for any table in database, this operation works by table id, if nothing is found then insert record
   * @param element record we want update into database
   * @return int which represent status of operation
   */
  def update(element: C): Future[Int]

}
object GenericCRUD{
  case class GenericOperationCRUD[C,T<: GenericTable[C]:runtime.TypeTag]() extends GenericCRUD[C,T] {
    override def selectAll: Future[List[C]] = super.run(tableDB().result.headOption).map(_.toList)
    override def select(id: Int): Future[Option[C]] = super.run(queryById(id).result.headOption)
    override def insert(element: C): Future[Int]= super.run((tableDB() returning tableDB().map(_.id)) += element)
    override def insertAll(element: List[C]): Future[List[Int]] =  super.run((tableDB returning tableDB().map(_.id)) ++= element).map(_.toList)
    override def delete(id: Int): Future[Int] = super.run(queryById(id).delete)
    override def deleteAll(id: List[Int]): Future[Int] = super. run(queryByIdPlus(id).delete)
    override def update(element: C): Future[Int] = super.run(tableDB().insertOrUpdate(element))
  }
}

