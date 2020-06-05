package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import view.fxview.component.HumanResources.subcomponent.parent.ZonaParent
import view.fxview.component.{AbstractComponent, Component}

/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.ZonaParent]]
 */
trait ZonaBox extends Component[ZonaParent] {

}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.ZonaBox]]
 *
 */
object ZonaBox {

  def apply(): ZonaBox = new ZonaBoxFX()

  private class ZonaBoxFX extends AbstractComponent[ZonaParent]("humanresources/subcomponent/ZonaBox")
    with ZonaBox {

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      ???
    }
  }
}

