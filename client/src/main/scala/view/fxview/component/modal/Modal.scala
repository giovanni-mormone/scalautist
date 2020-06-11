package view.fxview.component.modal

import java.net.URL
import java.util.ResourceBundle

import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXModalView
import view.fxview.component.Component

trait Modal extends DialogView {

}

object Modal {

  def apply[A <: ModalParent, B <: Component[A], C <: A](stage: Stage, caller: C, internalPane: B): Modal =
    new ModalImpl[A, B, C](stage, caller, internalPane)

  /**
   * @author Francesco Cassano
   * @param caller
   * @param internalPane
   * @tparam A
   * @tparam B
   * @tparam C
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

    override def close(): Unit =
      myStage.close()
  }
}


