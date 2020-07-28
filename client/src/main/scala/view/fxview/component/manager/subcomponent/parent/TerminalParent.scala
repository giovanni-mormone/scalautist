package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.Terminale

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal views to make requests to controller
 */
trait TerminalParent {

  /**
   * Method used to add a new zone to thee db
   * @param terminal
   *                 The terminal to add
   */
  def newTerminale(terminal: Terminale): Unit

  /**
   * Opens the modal to modify a terminal
   * @param terminal
   *                 The Terminal to modify
   */
  def openTerminalModal(terminal: Int): Unit
}
