package dbfactory.operation

import caseclass.CaseClassDB.Richiesta
import dbfactory.implicitOperation.OperationCrud
trait RichiestaOperation extends OperationCrud[Richiesta]{

}
object RichiestaOperation extends RichiestaOperation {

}