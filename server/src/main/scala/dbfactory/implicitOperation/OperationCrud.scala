package dbfactory.implicitOperation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
 
/** @author Fabian Aspée Encina
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
      crud.select(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future
  }

  /**
   * Select all element in a table in the database
   * @return List of all element in the table of the database that we have selected
   */
  def selectAll : Future[List[A]] = {
    val promiseInsert = Promise[List[A]]
    Future {
      crud.selectAll onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future
  }

  /**
   * Generic operation which enable insert any element in any table in database
   * @param element case class that represent instance of the table in database
   * @return Future of Int that represent status of operation
   */
  def insert(element:A):Future[Int] = {
    val promiseInsert = Promise[Int]
    Future {
      crud.insert(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future
  }

  /**
   * Generic operation which enable insert a List of any element in any table in database
   * @param element List of case class that represent instance of the table in database
   * @return Future of List of Int that represent status of operation
   */
  def insertAll (element:List[A]):Future[List[Int]]= {
    val promiseInsert = Promise[List[Int]]
    Future {
      crud.insertAll(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future

  }

  /**
   * Generic operation which enable delete one instance into database, this method receive a case class
   * as parameter and send this to Crud trait -> [[dbfactory.implicitOperation.Crud]]
   * @param element case class that represent instance of database  [[caseclass.CaseClassDB]]
   * @return Future of Int that represent status of operation
   */
  def delete(element:A):Future[Int]= {
    val promiseInsert = Promise[Int]
    Future {
      crud.delete(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future

  }

  /**
   * Generic operation which enable delete one or more instance into database, this method receive a list of
   * case class as parameter and send this to Crud trait -> [[dbfactory.implicitOperation.Crud]]
   * @param element list of case class that represent instance of database  [[caseclass.CaseClassDB]]
   * @return Future of Int that represent status of operation
   */
  def deleteAll(element:List[A]): Future[Int]= {
    val promiseInsert = Promise[Int]
    Future {
      crud.deleteAll(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future

  }
  /**
   * Generic operation which receive as input a case class [[caseclass.CaseClassDB]] that represent a instance
   * we want update into database
   * @param element case class that represent instance into database we want update
   * @return Future of Int that represent status of operation
   */
  def update(element:A):Future[Int]= {
    val promiseInsert = Promise[Int]
    Future {
      crud.update(element) onComplete {
        case Success(value) =>promiseInsert.success(value)
        case Failure(exception) => promiseInsert.failure(exception)
      }
    }
    promiseInsert.future

  }
}
