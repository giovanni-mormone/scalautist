package dbfactory.operation

import caseclass.CaseClassDB.Presenza
import dbfactory.implicitOperation.OperationCrud
trait PresenzaOperation extends OperationCrud[Presenza]{
}
object PresenzaOperation extends PresenzaOperation {
}