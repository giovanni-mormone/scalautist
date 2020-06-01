package dbfactory.implicitOperation

import dbfactory.setting.GenericCRUD.Brands
import dbfactory.setting.GenericTable

/**
 * @author Fabian Aspée Encina
 * @param dbInstance implicit instance that it has within itself a instance of operation in database.
 *                   This based on the table where you are working
 * @tparam A case class [[caseclass.CaseClassDB]] that represent a instance of any table into database
 * @tparam B class that define structure of a table into database, this instance extends GenericTable
 *           which in turn call case class -> A -> [[caseclass.CaseClassDB]]
 *
 */
abstract class OperationImplicit[A,B<:GenericTable[A]](implicit dbInstance:ImplicitInstanceTableDB[A,B]) {
  /**
   * Method which enable select a instance operation, which enable make operation into database
   * @return instance for make operation into database [[dbfactory.implicitOperation.ImplicitInstanceTableDB]]
   */
  private[implicitOperation] def typeDB(): Brands[A, B] = dbInstance.typeDB()
}
