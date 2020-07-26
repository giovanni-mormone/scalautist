package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.AlgorithmExecute
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import view.fxview.component.AbstractComponent
import view.fxview.component.manager.subcomponent.parent.ModalRunParent
import view.fxview.util.ResourceBundleUtil._

trait RunModal extends AbstractComponent[ModalRunParent] {

}

object RunModal {

  def apply(message: List[String], algorithmExecute: AlgorithmExecute): RunModal = new RunModalFX(message, algorithmExecute)

  private class RunModalFX(error: List[String], algorithmExecute: AlgorithmExecute)
  extends AbstractComponent[ModalRunParent]("manager/subcomponent/RunModal")
  with RunModal {

    @FXML
    var messagesHeader: VBox = _
    @FXML
    var ok: Button = _
    @FXML
    var cancel: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initLabel()
      intiButton()
    }

    private def initLabel(): Unit =
      error.foreach(error => messagesHeader.getChildren.add(TerminalModalLabels("terminal", resources.getResource(error)).pane))

    private def intiButton(): Unit = {
      ok.setText(resources.getResource(key = "ok"))
      cancel.setText(resources.getResource(key = "no"))

      ok.setOnAction(_ => parent.executeAlgorthm(algorithmExecute))
      cancel.setOnAction(_ => parent.cancel())
    }
  }
}