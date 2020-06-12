package view.fxview.component.modal

import java.net.URL
import java.util.ResourceBundle

import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXModalView
import view.fxview.component.Component

/**
 * @author Francesco Cassano
 *
 * Interface to create modal. It Implements [[view.DialogView]]
 */
trait Modal extends DialogView {
}

/**
* Companion object of [[view.fxview.component.modal.Modal]]
*
*/
object Modal {

  def apply[A <: ModalParent, B <: Component[A], C <: A](stage: Stage, caller: C, internalPane: B): Modal =
    new ModalImpl[A, B, C](stage, caller, internalPane)

  /**
   * @author Francesco Cassano
   *
   * Private [[view.fxview.component.modal.Modal]] Fx implementation.
   *
   * @param stage
   *             stage to draw modal
   * @param caller
   *               the object that calls modal
   * @param internalPane
   *                     Pane to draw inside the modal
   * @tparam A
   *           Generic type of parent. Subtype of ModalParent
   * @tparam B
   *           Generic type of component subtype of [[view.fxview.component.Component]] that has parent A
   * @tparam C
   *           Generic type that implements all modal parents methods
   */
  private class ModalImpl[A <: ModalParent, B <: Component[A], C <: A](stage: Stage, caller: C, internalPane: B)
    extends AbstractFXModalView(stage: Stage) with Modal {

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      drawChild()
    }

    protected def drawChild(): Unit = {
      pane.getChildren.add(internalPane.pane)
      internalPane.setParent(caller)
    }

    override def showMessage(message: String): Unit = {
      super.showMessage(message)
      this.close()
    }

    override def close(): Unit = {
      myStage.close()
    }
  }
}


