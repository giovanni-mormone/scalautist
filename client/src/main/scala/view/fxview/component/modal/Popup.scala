package view.fxview.component.modal

import javafx.stage.Stage
import view.fxview.AbstractFXModalView

class Popup (stage: Stage) extends AbstractFXModalView(stage){
  /**
   * Closes the view.
   */
  override def close(): Unit =
    myStage.close()
}
