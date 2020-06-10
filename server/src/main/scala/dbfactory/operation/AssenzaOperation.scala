package dbfactory.operation

import caseclass.CaseClassDB.Assenza
import dbfactory.implicitOperation.OperationCrud

trait AssenzaOperation extends OperationCrud[Assenza]{
}

object AssenzaOperation extends AssenzaOperation{

}
