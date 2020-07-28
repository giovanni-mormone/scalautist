package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.image.Image
import javafx.scene.layout.StackPane
import javafx.stage.{Modality, Stage}
import view.DialogView
import view.fxview.loader.FXLoader
import view.fxview.util.ResourceBundleUtil._


/**
 * @author Fabian Aspee, Giovanni Mormone.
 *
 * Template class of type [[view.DialogView]] with basic funtionality to show
 * and hide a view loaded from fxml file.
 * @param parentStage
 *                The [[javafx.stage.Stage]] where the view is Shown.
 *
 */
abstract class AbstractFXModalView(val parentStage:Stage) extends Initializable with DialogView{
  /**
   * The base pane of the fxView where the components are added.
   */
  @FXML
  protected var pane: StackPane = _
  protected var generalResources: ResourceBundle = _
  protected val myStage = new Stage()
  private val PREDEF_WIDTH_SIZE: Double = 350
  private val PREDEF_HEIGHT_SIZE: Double = 300
  private val image = new Image(getClass.getResource("../../../images/program_icon.png").toString)
  myStage.getIcons.add(image)

  /**
   * Stage of this view.
   */
  FXLoader.loadScene(myStage,this,"Base")

  override def initialize(location: URL, resources: ResourceBundle): Unit ={
    myStage.setTitle(resources.getResource("nome"))
    myStage.setResizable(false)
    myStage.setMinWidth(PREDEF_WIDTH_SIZE)
    myStage.setMinHeight(PREDEF_HEIGHT_SIZE)
    generalResources = resources
  }

  override def show(): Unit = {
    myStage.initModality(Modality.APPLICATION_MODAL)
    myStage initOwner parentStage
    myStage.showAndWait()
  }
  override def hide(): Unit =
    myStage.hide()

  override def showMessage(message: String): Unit =
    FXHelperFactory.modalWithMessage(myStage,message).show()

  override def showMessageFromKey(message: String): Unit = {
    showMessage(generalResources.getResource(message))
  }
  override def alertMessage(message: String): Boolean =
    FXHelperFactory.modalAlert(myStage,message)
}