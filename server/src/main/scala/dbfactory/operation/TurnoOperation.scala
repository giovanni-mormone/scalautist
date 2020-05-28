package dbfactory.operation

import caseclass.CaseClassDB.Turno
import dbfactory.implicitOperation.OperationCrud
trait TurnoOperation extends OperationCrud[Turno]{

}
object TurnoOperation extends TurnoOperation {

}