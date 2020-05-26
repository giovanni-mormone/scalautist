package view.fxview.loaders

import java.util.{Locale, ResourceBundle}

import com.typesafe.config.ConfigFactory
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import view.BaseView

/**
 * Helper per caricare una scena da file FXML.
 */
object FXLoader {
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
    val loader = new FXMLLoader(getClass.getResource("/fxml/"+layoutPath+".fxml"))
    val locale = new Locale("it","IT")
    val bundle = ResourceBundle.getBundle("fxml.properties.Base",locale)
    loader.setController(controller)
    loader.setResources(bundle)
    loader.load
    primaryStage.setScene(new Scene(loader.getRoot[Parent]))
  }
}
