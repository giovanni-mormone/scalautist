package dbfactory.implicitOperation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

abstract class OperationCrud[A](implicit crud:Crud[A]) {
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
