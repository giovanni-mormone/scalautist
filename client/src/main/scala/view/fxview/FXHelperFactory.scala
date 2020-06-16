package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.geometry.Pos
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.VBox
import javafx.stage.{Modality, Stage}
import view.BaseView
import view.fxview.loader.FXLoader

/**
 * @author Giovanni Mormone.
 *
 * Object to create Common view parts to utilize when needed
 *
 */
object FXHelperFactory {

  /**
   * Creates a modal that shows a message to the user. The modal is a [[view.BaseView]]
   * and have to be showed. It sits on top of the stage provided and blocks
   * it until the user closes the created modal.
   * @param parent
   *               The parent stage of the modal.
   * @param message
   *                The message to show to the user
   * @return
   *         The created modal.
   */
  def modalWithMessage(parent:Stage, message:String): BaseView = new FXModal(parent,message)

  /**
   * A simple javafx box to containing [[javafx.scene.control.ProgressIndicator]].
   */
  val loadingBox: VBox = {
    import javafx.geometry.Pos
    import javafx.scene.control.ProgressIndicator
    import javafx.scene.layout.VBox
    val pi = new ProgressIndicator
    val box = new VBox(pi)
    box.setId("loadingBox")
    box.setAlignment(Pos.CENTER)
    box
  }
  val defaultErrorPanel = {
    val box = new VBox()
    box.setId("error")
    box.setAlignment(Pos.CENTER)
    box
  }
  private class FXModal(parent:Stage, message:String) extends Initializable with BaseView{

    @FXML
    var messageLabel: Label = _
    @FXML
    var confirmationButton:Button = _
    private val myStage = new Stage()

    FXLoader.loadScene(myStage,this,"ModalBase")

    /**
     * Shows the view. If alredy shown does nothing
     */
    override def show(): Unit = {
      myStage.initModality(Modality.APPLICATION_MODAL)
      myStage initOwner parent
      myStage.showAndWait()
    }

    /**
     * Hides the view. If alredy hidden does nothing
     */
    override def hide(): Unit =
      myStage.hide()

    /**
     * Closes the view.
     */
    override def close(): Unit =
      myStage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      myStage.setTitle(resources.getString("title"))
      messageLabel.setText(message)
      confirmationButton.setText(resources.getString("confirm-button"))
      confirmationButton.setOnAction(_ => close())

      myStage.setOnCloseRequest(e => e.consume())
    }
  }

}
