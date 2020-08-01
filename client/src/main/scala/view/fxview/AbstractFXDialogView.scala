package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.application.Platform
import javafx.fxml.{FXML, Initializable}
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import view.DialogView
import view.fxview.loader.FXLoader
import view.fxview.util.ResourceBundleUtil._
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
  private val image = new Image(getClass.getResource("/images/program_icon.png").toString)
  myStage.getIcons.add(image)
  /**
   * Stage of this view.
   */
  FXLoader.loadScene(myStage,this,"Base")

  override def initialize(location: URL, resources: ResourceBundle): Unit ={
    myStage.setTitle(resources.getResource("nome"))
    generalResources = resources
  }


  override def close(): Unit = {
    Platform.exit()
  }

  override def show(): Unit =
    myStage.show()

  override def hide(): Unit =
    myStage.hide()

  override def showMessage(message: String): Unit =
    FXHelperFactory.modalWithMessage(myStage,message).show()

  override def showMessageFromKey(message: String): Unit =
    Platform.runLater(() =>showMessage(generalResources.getResource(message)))

  override def alertMessage(message: String): Boolean =
    FXHelperFactory.modalAlert(myStage,message)

  myStage.setOnCloseRequest(e => {
    e.consume()
    close()
  })

}
