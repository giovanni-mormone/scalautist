package dbfactory.setting

import dbfactory.setting.DB.dbCo
import slick.basic.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

/**
 * @author Fabian Aspée Encina
 * This trait allow run any operation that should make in database
 * @tparam T class that represent the table in database, allow make query in database [[dbfactory.table]]
 * @tparam C case class that represent instance in database [[caseclass.CaseClassDB]]
 */
trait DB[C, T<: GenericTable[C]]{
  /**
   * Generic operation that run any operation into database
   * @param a A Database I/O Action that can be executed on a database.
   *          The DBIOAction type allows a separation of execution logic and
   *          resource usage management logic from composition logic.
   * @tparam R The result type when executing the DBIOAction and fully materializing the result.
   * @return Future of type R, in this case represent a case class that is defined in [[caseclass.CaseClassDB]]
   *         for more details view [[slick.dbio.DBIOAction]]
   */
  protected def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = DB.run(a)

}

/**
 * @author Fabian Aspée Encina
 *   trait which enable make conection with database in remote
 */
object DB{
  private lazy val dbCo:DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")

  /**
   * Get the configured Database.
   * It is instantiated lazily when this method is called for the first time, and must be closed after use.
   */
  private val database = dbCo.db
  private def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R]= DB.database.run(a)


}
