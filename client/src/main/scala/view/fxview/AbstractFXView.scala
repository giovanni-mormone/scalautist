package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import view.BaseView
import view.fxview.loader.FXLoader

/**
 * Template class of type [[view.BaseView]] with basic funtionality to show
 * and hide a view loaded from fxml file.
 */
abstract class AbstractFXView(stage:Stage) extends Initializable with BaseView{
  /**
   * The base pane of the fxView where the components are added.
   */
  @FXML
  protected var pane: BorderPane = _
  /**
   * Stage of this view.
   */
  protected var myStage: Stage = stage
  FXLoader.loadScene(myStage,this,"BorderBase")

  override def initialize(location: URL, resources: ResourceBundle): Unit ={
    myStage.setTitle(resources.getString("nome"))
  }
}
