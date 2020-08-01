package view.fxview.component.modal

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.VBox
import javafx.stage.Stage
import view.fxview.AbstractFXModalView
import view.fxview.component.AbstractComponent
import view.fxview.component.manager.subcomponent.InfoLabel
import view.fxview.component.manager.subcomponent.parent.ModalRunParent
import view.fxview.util.ResourceBundleUtil._


case class ModalInfo(stage:Stage,parent:ModalRunParent) extends AbstractFXModalView(stage){
  private var modal:ModalInfoA = _
  def start():Unit = {
    modal = ModalInfo()
    pane.getChildren.add(modal.setParent(parent).pane)
    show()
  }
  def message(message:String): Unit = modal.printMessage(message)
  def isShow:Boolean = myStage.isShowing
  override def show():Unit={
    myStage.show()
  }
  override def close(): Unit = {
    myStage.close()
  }
}
/**
 * Companion object of [[ModalInfo]]
 */
object ModalInfo{
  private val instance = new ModalInfoFX()
  def apply(): ModalInfoA  = instance
  private class ModalInfoFX()
    extends AbstractComponent[ModalRunParent]("manager/subcomponent/InfoAlgorithmBox")
      with ModalInfoA {
    @FXML
    var messagesHeader: VBox = _
    @FXML
    var close: Button = _
    @FXML
    var title: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      title.setText(resources.getResource("title"))
      intiButton()
    }

    private def intiButton(): Unit = {
      close.setText(resources.getResource(key = "close"))
      close.setOnAction(_=>parent.closeModal())
    }

    override def printMessage(information: String): Unit = {
      val info = InfoLabel(information)
      messagesHeader.getChildren.add(info.pane)
    }
  }

}

