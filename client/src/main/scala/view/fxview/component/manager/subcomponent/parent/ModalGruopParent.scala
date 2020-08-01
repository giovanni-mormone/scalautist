package view.fxview.component.manager.subcomponent.parent

import view.fxview.component.manager.subcomponent.GroupParamsBox.Group
import view.fxview.component.modal.ModalParent

trait ModalGruopParent extends ModalParent {

  /**
   * method that allow update group when user create a new group
   * @param group case class that contains information for a determinate group
   */
  def updateGroups(group: Group): Unit
}
