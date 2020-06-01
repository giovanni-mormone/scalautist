package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, StackPane}
import javafx.stage.Stage
import view.{BaseView, DialogView, GoBackView}
import view.fxview.loader.FXLoader

/**
 * @author Giovanni Mormone.
 *
 * Template class of type [[view.BaseView]] with basic funtionality to show
 * and hide a view loaded from fxml file.
 * @param myStage
 *                The [[javafx.stage.Stage]] where the view is Shown.
 *
 */
abstract class AbstractFXDialogView(val myStage:Stage) extends Initializable with DialogView{
  /**
   * The base pane of the fxView where the components are added.
   */
  @FXML
  protected var pane: StackPane = _
  protected var generalResources: ResourceBundle = _
  /**
   * Stage of this view.
   */
  FXLoader.loadScene(myStage,this,"Base")

  override def initialize(location: URL, resources: ResourceBundle): Unit ={
    myStage.setTitle(resources.getString("nome"))
    generalResources = resources
  }

  override def show(): Unit =
    myStage show

  override def hide(): Unit =
    myStage hide

  override def showMessage(message: String): Unit =
    FXHelperFactory.modalWithMessage(myStage,message).show()
}

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