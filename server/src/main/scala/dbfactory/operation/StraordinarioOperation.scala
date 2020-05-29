package dbfactory.operation

import caseclass.CaseClassDB.Straordinario
import dbfactory.implicitOperation.OperationCrud
trait StraordinarioOperation extends OperationCrud[Straordinario]{

}
object StraordinarioOperation extends StraordinarioOperation {

}