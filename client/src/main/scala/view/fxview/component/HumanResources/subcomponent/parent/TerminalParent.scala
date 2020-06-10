package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Terminale

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal views to make requests to controller
 */
trait TerminalParent {

  /**
   *
   * @param terminal
   */
  def newTerminale(terminal: Terminale): Unit
}
