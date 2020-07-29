package view.fxview

import javafx.scene.layout.VBox
import org.controlsfx.control.PopOver
import view.fxview.component.manager.subcomponent.InfoLabel

object NotificationHelper {
  final case class NotificationParameters(popover:PopOver,vBox:VBox, consumeNotification: Long => Unit)
  def drawNotifica(str: String,tag:Long, parameters: NotificationParameters): Unit = {
    parameters.popover.setHideOnEscape(true)
    parameters.popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER)
    parameters.popover.setAutoFix(true)
    val label = InfoLabel(str)
    parameters.popover.getRoot.getChildren.removeIf(_=>parameters.popover.getRoot.getChildren.contains(parameters.vBox))
    parameters.vBox.getChildren.remove(label.pane)
    parameters.vBox.getChildren.add(label.pane)
    parameters.popover.setContentNode(parameters.vBox)
  }

}
