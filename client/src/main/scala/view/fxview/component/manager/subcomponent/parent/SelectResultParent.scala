package view.fxview.component.manager.subcomponent.parent

import java.sql.Date

trait SelectResultParent {
  def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit

}
