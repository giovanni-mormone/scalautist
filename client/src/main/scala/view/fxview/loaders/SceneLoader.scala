package view.fxview.loaders

import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import view.BaseView


/**
 * Helper per caricare una scena da file FXML.
 */
object SceneLoader {
  /**
   * Method that loads a fxml file from a path and sets it as the
   * root of the [[javafx.scene.Scene]] on the provided [[javafx.stage.Stage]]
   *
   * @param primaryStage
   *                     The stage where the fxml should be loaded.
   * @param controller
   *                   The fx controller class of the fxml loaded.
   * @param layoutPath
   *                   The path of the fxml to load.
   */
  def loadScene(primaryStage: Stage, controller: BaseView, layoutPath: String):Unit = {
    val loader = new FXMLLoader(getClass.getResource(layoutPath))
    loader.setController(controller)
    loader.load
    primaryStage.setScene(new Scene(loader.getRoot[Parent]))
  }
}
