package view.fxview.component.HumanResources.subcomponent.parent

import caseclass.CaseClassDB.Assenza
import view.fxview.component.modal.ModalParent

trait ModalAbsenceParent extends ModalParent {
  def saveAbsence(absence: Assenza)
}
