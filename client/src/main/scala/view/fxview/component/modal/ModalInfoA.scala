package view.fxview.component.modal

import view.fxview.component.Component
import view.fxview.component.manager.subcomponent.parent.ModalRunParent

/**
 * @author Fabian Aspee
 * This trait allows to manage view information in real-time of the algorithm status
 */
trait ModalInfoA extends Component[ModalRunParent] {
  /**
   * method that show information in real time of status algorithm
   * @param information status of algorithm
   */
  def printMessage(information:String):Unit
}
