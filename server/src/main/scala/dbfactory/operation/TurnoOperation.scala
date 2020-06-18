package dbfactory.operation

import java.sql.Date

import caseclass.CaseClassDB.Turno
import caseclass.CaseClassHttpMessage.InfoHome
import dbfactory.implicitOperation.OperationCrud

import scala.concurrent.Future
trait TurnoOperation extends OperationCrud[Turno]{
}
object TurnoOperation extends TurnoOperation {
}