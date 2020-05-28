package dbfactory.setting

import slick.basic.DatabaseConfig
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

trait DBS{
  private val dbCo:DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")
  protected val database = dbCo.db
}
trait DB[C <: GenericTable[T], T] extends DBS{
  protected def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = database.run(a)
}