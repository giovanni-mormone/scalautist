package view.fxview

import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import view.BaseView

/**
 * Template class of type [[view.BaseView]] with basic funtionality to show
 * and hide a view loaded from fxml file.
 */
abstract class AbstractFXView extends Initializable with BaseView{

  @FXML
  protected var pane: BorderPane = _
  protected var stage: Stage = _

  def apply(primaryStage:Stage) {

  }
}
