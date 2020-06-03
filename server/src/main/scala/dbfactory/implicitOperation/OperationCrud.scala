package dbfactory.implicitOperation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

/**
 * @author Fabian Aspée Encina
 * Generic abstract class which enable make operations in any table in database
 * every class in the package [[dbfactory.operation]] must extend this abstract class
 * @param crud Interface [[dbfactory.implicitOperation.Crud]] which enable make call operation of type
 *             select, insert, update, delete
 * @tparam A Is a case class [[caseclass.CaseClassDB]] that is passed as a parameter
 */
abstract class OperationCrud[A](implicit crud:Crud[A]) {
  /**
   * Generic Method which enable select one record in any table in database by Id
   * @param element Id of record what select in database
   * @return Future of Option of Type A -> case class [[caseclass.CaseClassDB]]
   */
  def select (element:Int):Future[Option[A]]= {
    val promiseInsert = Promise[Option[A]]
    Future {
      crud.select(element) onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future
  }

  /**
   * Select all element in a table in the database
   * @return List of all element in the table of the database that we have selected
   */
  def selectAll : Future[Option[List[A]]] = {
    val promiseInsert = Promise[Option[List[A]]]
    Future {
      crud.selectAll onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future
  }

  /**
   * Generic operation which enable insert any element in any table in database
   * @param element case class that represent instance of the table in database
   * @return Future of Int that represent status of operation
   */
  def insert[B](element:A, promiseInsert: Promise[Option[B]]): Unit = {
    Future {
      crud.insert(element) onComplete(t => checkOption(t,promiseInsert))
    }
  }

  /**
   * Generic operation which enable insert a List of any element in any table in database
   * @param element List of case class that represent instance of the table in database
   * @return Future of List of Int that represent status of operation
   */
  def insertAll (element:List[A]):Future[Option[List[Int]]]= {
    val promiseInsert = Promise[Option[List[Int]]]
    Future {
      crud.insertAll(element) onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future
  }

  /**
   * Generic operation which enable delete one instance into database, this method receive a case class
   * as parameter and send this to Crud trait -> [[dbfactory.implicitOperation.Crud]]
   * @param element case class that represent instance of database  [[caseclass.CaseClassDB]]
   * @return Future of Int that represent status of operation
   */
  def delete(element:Int):Future[Option[Int]]= {
    val promiseInsert = Promise[Option[Int]]
    Future {
      crud.delete(element) onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future

  }

  /**
   * Generic operation which enable delete one or more instance into database, this method receive a list of
   * case class as parameter and send this to Crud trait -> [[dbfactory.implicitOperation.Crud]]
   * @param element list of case class that represent instance of database  [[caseclass.CaseClassDB]]
   * @return Future of Int that represent status of operation
   */
  def deleteAll(element:List[Int]): Future[Option[Int]]= {
    val promiseInsert = Promise[Option[Int]]
    Future {
      crud.deleteAll(element) onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future

  }
  /**
   * Generic operation which receive as input a case class [[caseclass.CaseClassDB]] that represent a instance
   * we want update into database
   * @param element case class that represent instance into database we want update
   * @return Future of Int that represent status of operation
   */
  def update(element:A):Future[Option[Int]]= {
    val promiseInsert = Promise[Option[Int]]
    Future {
      crud.update(element) onComplete(t => checkOption(t,promiseInsert))
    }
    promiseInsert.future
  }

  private def checkOption[M](list:Try[Option[M]],promise: Promise[Option[M]]):Unit = list match{
    case Success(Some(List())) =>promise.success(None)
    case Success(value) =>promise.success(value)
    case Failure(exception) => promise.failure(exception)
  }

}
