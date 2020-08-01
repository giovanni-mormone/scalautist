package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassDB.Terminale
import view.fxview.component.modal.ModalParent

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal modal to make requests to controller
 */
trait ModalTerminalParent extends ModalParent {

  /**
   * Delete a Zona form the db
   *
   * @param terminal
   *             [[caseclass.CaseClassDB.Terminale]] instance to delete
   */
  def deleteTerminal(terminal: Terminale): Unit

  /**
   * Update a Zona in the db
   *
   * @param terminal
   *             [[caseclass.CaseClassDB.Terminale]] instance to update
   */
  def updateTerminal(terminal: Terminale): Unit
}
