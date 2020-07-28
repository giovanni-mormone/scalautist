package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.AlgorithmExecute
import view.fxview.component.modal.ModalParent

trait ModalRunParent extends ModalParent {
  /**
   * method that close modal that contains information in real-time of status algorithm
   */
  def closeModal(): Unit


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
