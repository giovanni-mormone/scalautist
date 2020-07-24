package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.InfoAlgorithm
import view.fxview.component.modal.ModalParent

/**
 * //TODO
 */
trait ModalParamParent extends ModalParent {

  /**
   *
   * @param param
   */
  def loadParam(param: InfoAlgorithm): Unit

}
