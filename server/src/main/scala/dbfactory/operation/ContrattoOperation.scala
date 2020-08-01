package dbfactory.operation
import caseclass.CaseClassDB.Contratto
import dbfactory.implicitOperation.OperationCrud

trait ContrattoOperation extends OperationCrud[Contratto]{

}
object ContrattoOperation extends ContrattoOperation {

}
