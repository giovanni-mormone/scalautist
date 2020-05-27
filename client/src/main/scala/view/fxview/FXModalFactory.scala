package view.fxview

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label}
import javafx.stage.{Modality, Stage}
import view.BaseView
import view.fxview.loader.FXLoader

/**
 * Object to create standard modals with a provided message
 */
object FXModalFactory {

  def apply(myStage:Stage, parent:Stage, message:String): BaseView = new FXModal(myStage,parent,message)

  private class FXModal(myStage:Stage, parent:Stage, message:String) extends Initializable with BaseView{

    @FXML
    var messageLabel: Label = _
    @FXML
    var confirmationButton:Button = _

    FXLoader.loadScene(myStage,this,"ModalBase")

    /**
     * Shows the view. If alredy shown does nothing
     */
    override def show(): Unit = {
      myStage.initModality(Modality.APPLICATION_MODAL)
      myStage initOwner parent
      myStage showAndWait
    }

    /**
     * Hides the view. If alredy hidden does nothing
     */
    override def hide(): Unit =
      myStage hide

    /**
     * Closes the view.
     */
    override def close(): Unit =
      myStage close

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      myStage.setTitle(resources.getString("title"))
      messageLabel.setText(message)
      confirmationButton.setText(resources.getString("confirm-button"))
      confirmationButton.setOnAction(_ => close)
    }
  }

}
