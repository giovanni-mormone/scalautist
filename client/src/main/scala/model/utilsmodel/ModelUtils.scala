package model.utilsmodel

import caseclass.CaseClassHttpMessage.Id

/**
 * @author Francesco Cassano
 * Some utility function
 */
object ModelUtils {
  implicit def id(id:Int):Id = Id(id)
}
