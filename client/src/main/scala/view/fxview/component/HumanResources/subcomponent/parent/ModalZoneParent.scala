package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Zona
import view.fxview.component.modal.ModalParent

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal modal to make requests to controller
 */
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
