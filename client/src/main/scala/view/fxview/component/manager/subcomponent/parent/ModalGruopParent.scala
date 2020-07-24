package view.fxview.component.manager.subcomponent.parent

import view.fxview.component.manager.subcomponent.GroupParamsBox.Group
import view.fxview.component.modal.ModalParent

trait ModalGruopParent extends ModalParent {

  /**
   *
   * @param group
   */
  def updateGroups(group: Group): Unit
}
