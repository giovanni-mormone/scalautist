package dbfactory.setting
import slick.jdbc.SQLServerProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.runtime.{universe => runtime}

/**
 * Generic Crud is a trait which enables operation generic in all tables
 * @tparam T case class that represent instance in database [[caseclass.CaseClassDB]]
 * @tparam C class that represent the table in database, allow make query in database [[dbfactory.table]]
 */
sealed trait GenericCRUD[T,C <: GenericTable[T]] extends GenericTableQuery[T,C] with DB[C,T] {
  /**
   *  A possibly parameterized query that will be cached for repeated efficient execution without having to recompile it every time
   */
  protected val queryById = Compiled((id: Rep[Int]) => tableDB().filter(_.id === id))
  /**
   *  Query per make filter using clause ''in'' of SQL
   */
  protected val queryByIdPlus: Seq[Int] => Query[C, T, Seq] = (id: Seq[Int]) => tableDB().filter(_.id.inSet(id))

  /**
   * Generic select that allow select from any table in database
   * @param id represent id in table in database
   * @return Option of type required
   */
  def select(id:Int): Future[Option[T]]

  /**
   * Select All record in the table required
   * @return List of type required
   */
  def selectAll: Future[List[T]]

  /**
   * Generic Insert that allow insert into any table in database
   * @param c case class which represents the object that we want to insert into database
   * @return int that represent status of operation
   */
  def insert(c: T): Future[Int]

  /**
   * Insert all record in the list
   * @param c all record we want insert into database
   * @return int which represent status of operation
   */
  def insertAll(c: List[T]): Future[List[Int]]

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
   * @param c record we want update into database
   * @return int which represent status of operation
   */
  def update(c: T): Future[Int]

}
object GenericCRUD{
  case class Brands[T,C<: GenericTable[T]:runtime.TypeTag]() extends GenericCRUD[T,C] {
    override def selectAll: Future[List[T]] = super.run(tableDB().result).map(_.toList)
    override def select(id: Int): Future[Option[T]] = super.run(queryById(id).result.headOption)
    override def insert(elem: T): Future[Int]= super.run((tableDB() returning tableDB().map(_.id)) += elem)
    override def insertAll(c: List[T]): Future[List[Int]] =  super.run((tableDB returning tableDB().map(_.id)) ++= c).map(_.toList)
    override def delete(id: Int): Future[Int] = super.run(queryById(id).delete)
    override def deleteAll(id: List[Int]): Future[Int] = super. run(queryByIdPlus(id).delete)
    override def update(elem: T): Future[Int] = super.run(tableDB().insertOrUpdate(elem))
  }
}