package view.fxview

import javafx.scene.control.{Accordion, Label, TitledPane}
import javafx.scene.layout.VBox
import org.controlsfx.control.PopOver

object NotificationHelper {

  final case class NotificationParameters(accordion:Accordion, popover:PopOver, consumeNotification: Long => Unit)

  def drawNotifica(str: String,tag:Long, parameters: NotificationParameters): Unit = {
    val firstTitled = new TitledPane
    firstTitled.setText(str)
    firstTitled.setId(tag.toString)
    val content = new VBox
    val label = new Label("Orario")
    parameters.popover.setHideOnEscape(true);
    parameters.popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    parameters.popover.setAutoFix(true);
    content.getChildren.add(label)
    firstTitled.setContent(content)
    firstTitled.setOnMouseClicked(_=>parameters.consumeNotification(tag))
    parameters.accordion.getPanes.add(firstTitled)
    parameters.popover.getRoot.getChildren.removeIf(_=>parameters.popover.getRoot.getChildren.contains(parameters.accordion))
    parameters.popover.getScene.setRoot(parameters.accordion)
  }
}
