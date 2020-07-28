package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.AlgorithmExecute
import view.fxview.component.modal.ModalParent

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal modal to make requests to controller
 */
trait ModalRunParent extends ModalParent {

  /**
   * Method that reset information
   */
  def cancel(): Unit

  /**
   * Method that asks controller to run algorithm using that information
   * @param info instance of [[AlgorithmExecute]] that contains information to run algorithm
   */
  def executeAlgorthm(info: AlgorithmExecute): Unit
}
