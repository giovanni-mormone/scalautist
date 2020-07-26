package view.fxview

import javafx.scene.Scene
import javafx.stage.Stage
import view.GoBackView


/**
 * @author Giovanni Mormone.
 *
 * Template class of type [[view.GoBackView]] with basic funtionality to show
 * and hide a view loaded from fxml file and to go back to a previous scene, if present.
 * @param myStage
 *              The [[javafx.stage.Stage]] where the view is Shown.
 * @param oldScene
 *                 The Scene to show if go back is called.
 *
 */
abstract class AbstractFXViewWithBack(override val myStage:Stage, oldScene: Option[Scene]) extends AbstractFXDialogView(myStage) with GoBackView{
  override def back(): Unit =
    myStage.setScene(oldScene.getOrElse(myStage.getScene))
}
