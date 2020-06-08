package utils
import dbfactory.setting.DB
import slick.basic.{DatabaseConfig, StaticDatabaseConfig}
import slick.jdbc.JdbcProfile
import slick.jdbc.SQLServerProfile.api._

import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

@StaticDatabaseConfig("#tsql")
class DatabaseHelper private{
  import DatabaseHelper._
  def runScript():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sql")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }
}
object DatabaseHelper{
  def apply(): DatabaseHelper = new DatabaseHelper()
  private val dbCo:DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")
  private val database = dbCo.db
  private val clean_DB: String = Source.fromResource("Scalautist.sql").mkString
  private val inserts_sql: String = Source.fromResource("ScalautistTest.sql").mkString

}
