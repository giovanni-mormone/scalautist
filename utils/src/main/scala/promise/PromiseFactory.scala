package promise

import caseclass.CaseClassDB.Login

import scala.concurrent.Promise

/**
 * @autgor Giovanni Mormone
 *  Helper object to create Custom Promises.
 */
object PromiseFactory {

  /**
   * An Option Int Promise.
   */
  def intPromise():Promise[Option[Int]] = Promise[Option[Int]]
  /**
   * An Option [[caseclass.CaseClassDB.Login]] Promise.
   */
  def loginPromise():Promise[Option[Login]] = Promise[Option[Login]]
}
