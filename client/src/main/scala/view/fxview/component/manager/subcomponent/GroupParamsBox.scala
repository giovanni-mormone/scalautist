package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.GroupParamsParent

trait GroupParamsBox extends Component[GroupParamsParent] {

}

object GroupParamsBox {

  def apply(): GroupParamsBox = new GroupParamsBoxFX()

  private class GroupParamsBoxFX() extends AbstractComponent[GroupParamsParent](path = "manager/subcomponent/ParamGruppo" )
  with GroupParamsBox{

    override def initialize(location: URL, resources: ResourceBundle): Unit =
      super.initialize(location, resources)
  }
}