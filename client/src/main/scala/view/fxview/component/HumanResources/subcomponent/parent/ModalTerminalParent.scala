package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Terminale
import view.fxview.component.modal.ModalParent

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
