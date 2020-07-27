package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.AlgorithmExecute
import view.fxview.component.modal.ModalParent

trait ModalRunParent extends ModalParent {

  /**
   *
   */
  def cancel(): Unit

  /**
   *
   * @param info
   */
  def executeAlgorthm(info: AlgorithmExecute): Unit
}
