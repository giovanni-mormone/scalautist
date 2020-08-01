package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.InfoAlgorithm
import utils.TransferObject.DataForParamasModel
import view.fxview.component.modal.ModalParent

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal modal to make requests to controller
 */
trait ModalParamParent extends ModalParent {

  /**
   * It asks controller to get information of parameter to show
   * @param idp id of the parameter
   * @param data instance of  [[DataForParamasModel]] that contains information about parameter
   */
  def getInfoToShow(idp: Int, data: DataForParamasModel): Unit

  /**
   * It notifies controller to load a specific parameter information
   * @param param instance of [[InfoAlgorithm]] that contains information about parameter
   */
  def loadParam(param: InfoAlgorithm): Unit

}
