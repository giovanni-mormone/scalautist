package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Assenza
import view.fxview.component.modal.ModalParent

/**
 * @author Fabian Aspee Encina
 *
 * It is the interface of the methods used by absence modal to make requests to controller
 */
trait ModalAbsenceParent extends ModalParent {

  /**
   * It saves a [[caseclass.CaseClassDB.Assenza]] into the database
   * @param absence
   */
  def saveAbsence(absence: Assenza)
}
