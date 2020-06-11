package model.utilsmodel

import caseclass.CaseClassHttpMessage.{Id, Request}

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {
  implicit def id(id:Int):Request[Int] = Request(Some(id))

}
