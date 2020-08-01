package view.fxview.component.HumanResources.subcomponent.util

trait ViewCode {}

/**
 * @author Francesco Cassano
 *         String to identify entities
 */
object EmployeeView extends ViewCode {
  val fire: String = "Fire"
  val ill: String = "Ill"
  val holiday: String = "Holiday"
  val recruit: String = "Recruit"
  val zone: String = "Zone"
  val terminal: String = "Terminal"
  val shift: String = "Shift"
  val contract: String = "Contract"
}

object TerminalView extends ViewCode {
  val recruit: String = "Recruit"
  val termialManager: String = "Terminal"
}

object ErrorName {
  val NOTFOUND: String = "NotFound"
  val BADREQUEST: String = "BadRequest"
  val UNKNOWN: String = "Unknown"
  val NOTCONN: String = "ConnectionFail"
  val ERROR1: String = "Error1"
  val ERROR2: String = "Error2"
  val ERROR3: String = "Error3"
  val ERROR4: String = "Error4"
  val ERROR5: String = "Error5"
  val ERROR6: String = "Error6"
}