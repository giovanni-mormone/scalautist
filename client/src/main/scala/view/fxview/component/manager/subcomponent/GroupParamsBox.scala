package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import caseclass.CaseClassHttpMessage.GruppoA
import view.fxview.component.manager.subcomponent.parent.GroupParamsParent
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm
import view.fxview.component.{AbstractComponent, Component}

trait GroupParamsBox extends Component[GroupParamsParent] {

}

object GroupParamsBox {

  def apply(params: ParamsForAlgoritm, groups: List[GruppoA], rule: List[Regola]): GroupParamsBox =
    new GroupParamsBoxFX(params, groups, rule)

  private class GroupParamsBoxFX(params: ParamsForAlgoritm, groups: List[GruppoA], rule: List[Regola])
    extends AbstractComponent[GroupParamsParent](path = "manager/subcomponent/ParamGruppo" )
    with GroupParamsBox{

    override def initialize(location: URL, resources: ResourceBundle): Unit =
      super.initialize(location, resources)
  }
}