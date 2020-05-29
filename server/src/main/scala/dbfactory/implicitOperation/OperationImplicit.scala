package dbfactory.implicitOperation

import dbfactory.setting.GenericCRUD.Brands
import dbfactory.setting.GenericTable

abstract class OperationImplicit[A,B<:GenericTable[A]](implicit dbInstance:ImplicitInstanceTableDB[A,B]) {
  private[implicitOperation] def typeDB(): Brands[A, B] = dbInstance.typeDB()
}
