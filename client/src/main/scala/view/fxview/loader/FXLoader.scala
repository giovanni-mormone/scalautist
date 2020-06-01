package view.fxview.loader

import java.util.{Locale, ResourceBundle}

import com.typesafe.config.ConfigFactory
import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import view.BaseView
import view.fxview.component.Component

/**
 * @author Giovanni Mormone.
 *
 * Helper per caricare una scena da file FXML.
 *
 */
object FXLoader {
  private val SCENE_PATH = "/javafx/fxml/"
  private val COMPONENT_PATH = "/javafx/fxml/component/"
  private val EXTENSION = ".fxml"
  private val SCENE_BUNDLE_PATH = "javafx.fxml.properties."
  private val COMPONENT_BUNDLE_PATH = "javafx.fxml.properties.component."


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
    val loader = new FXMLLoader(getClass.getResource(SCENE_PATH +  layoutPath + EXTENSION))
    val bundle = ResourceBundle.getBundle(SCENE_BUNDLE_PATH + layoutPath ,new Locale("it","IT"))
    loader.setController(controller)
    loader.setResources(bundle)
    loader.load
    primaryStage.setScene(new Scene(loader.getRoot[Parent]))
  }

  /**
   * Method that loads a component from fxml files and returns the loaded pane,
   * setting the provided controller as fx controller for the pane.
   *
   * @param controller
   *                   The fx controller class of the fxml loaded.
   * @param layoutPath
   *                   The path of the fxml to load.
   * @return
   *         A [[javafx.scene.layout.Pane]], loaded from FXML
   */
  def loadComponent(controller: Component[_], layoutPath: String): Pane = {
    val loader = new FXMLLoader(getClass.getResource(COMPONENT_PATH + layoutPath + EXTENSION))
    val bundle = ResourceBundle.getBundle(COMPONENT_BUNDLE_PATH + layoutPath.replace("/",".") ,new Locale("it","IT"))
    loader.setController(controller)
    loader.setResources(bundle)
    loader.load()
  }
}
