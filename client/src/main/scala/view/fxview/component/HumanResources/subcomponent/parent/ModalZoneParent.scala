package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Zona
import view.fxview.component.modal.ModalParent

trait ModalZoneParent extends ModalParent {

  /**
   * Delete a Zona form the db
   *
   * @param zona
   *             [[caseclass.CaseClassDB.Zona]] instance to delete
   */
  def deleteZona(zona: Zona): Unit

  /**
   * Update a Zona in the db
   *
   * @param zona
   *             [[caseclass.CaseClassDB.Zona]] instance to update
   */
  def updateZona(zona:Zona): Unit
}
