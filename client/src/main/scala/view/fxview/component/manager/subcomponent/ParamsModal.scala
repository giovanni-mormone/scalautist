package view.fxview.component.manager.subcomponent

import caseclass.CaseClassDB.Parametro
import view.fxview.component.AbstractComponent
import view.fxview.component.manager.subcomponent.parent.ModalParamParent

trait ParamsModal {

}

object ParamsModal {

  def apply(oldsParam: List[Parametro]): ParamsModal = new ParamsModalFX(oldsParam)

  private class ParamsModalFX(value: List[Parametro]) extends AbstractComponent[ModalParamParent]("")
    with ParamsModal {

  }
}
