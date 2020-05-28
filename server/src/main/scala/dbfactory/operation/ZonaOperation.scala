package dbfactory.operation

import caseclass.CaseClassDB.Zona
import dbfactory.implicitOperation.OperationCrud
trait ZonaOperation extends OperationCrud[Zona]{

}
object ZonaOperation extends ZonaOperation {

}