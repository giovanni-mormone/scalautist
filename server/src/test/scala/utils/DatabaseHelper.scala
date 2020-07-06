package utils
import slick.basic.{DatabaseConfig, StaticDatabaseConfig}
import slick.jdbc.JdbcProfile
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Awaitable, Future, Promise}
import scala.io.Source
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
  def runScript2():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sql2")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }
  def runScript3():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sql3")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }

  def runScript4():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sql4")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }
  def runScript5(): Future[Int] = {
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sql5")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }
  def runScriptT():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sqlT")onComplete{
        case Success(_) =>promiseSql.success(1)
        case Failure(_) => promiseSql.success(0)
      }
      case Failure(_) => promiseSql.success(0)
    }
    promiseSql.future
  }

  def runScriptRichiesta():Future[Int]={
    val promiseSql = Promise[Int]
    database.run(sqlu"#$clean_DB") onComplete{
      case Success(_) => DatabaseHelper.database.run(sqlu"#$inserts_sqlRich")onComplete{
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
  private val inserts_sql2: String = Source.fromResource("ScalautistTest2.sql").mkString
  private val inserts_sql3: String = Source.fromResource("ScalautistTestDisponibilita.sql").mkString
  private val inserts_sql4: String = Source.fromResource("ScalautistTestAssenza.sql").mkString
  private val inserts_sql5: String = Source.fromResource("ScalautistTestResult.sql").mkString
  private val inserts_sqlT: String = Source.fromResource("ScalautistTestTurni.sql").mkString
  private val inserts_sqlRich: String = Source.fromResource("ScalautistTestRichiesta.sql").mkString

}
