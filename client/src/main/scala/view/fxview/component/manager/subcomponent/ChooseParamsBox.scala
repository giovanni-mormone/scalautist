package view.fxview.component.manager.subcomponent

import caseclass.CaseClassDB.Parametro
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.{ChooseParamsParent, ManagerRichiestaParent}

trait ChooseParamsBox extends Component[ChooseParamsParent] {

}

object ChooseParamsBox {

  def apply(oldParmas: List[Parametro]): ChooseParamsBox = new ChooseParamsBoxFX(oldParmas)

  private class ChooseParamsBoxFX(oldParams: List[Parametro])
    extends AbstractComponent[ChooseParamsParent]("manager/subcomponent/ChooseParamsBox")
      with ChooseParamsBox {

  }

}
