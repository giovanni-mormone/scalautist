package view.fxview.component.manager.subcomponent.parent

import java.sql.Date

trait SelectResultParent {

  /**
   * Used to request the result for a terminal in a time period
   * @param value
   *              The id of the terminal to search
   * @param date
   *             The First date of the period requested
   * @param date1
   *              The Last date of the period requested
   */
  def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit

}
