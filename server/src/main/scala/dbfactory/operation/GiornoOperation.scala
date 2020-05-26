package dbfactory.operation

import caseclass.CaseClassDB.Giorno
import dbfactory.implicitOperation.OperationCrud
trait GiornoOperation extends OperationCrud[Giorno]{

}
object GiornoOperation extends GiornoOperation {

}
