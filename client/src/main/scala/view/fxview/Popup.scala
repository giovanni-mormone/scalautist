package view.fxview

import javafx.stage.Stage

class Popup (stage: Stage) extends AbstractFXModalView(stage){
  /**
   * Closes the view.
   */
  override def close(): Unit =
    myStage.close()
}
