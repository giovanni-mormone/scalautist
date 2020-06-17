package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import controller.ManagerController
import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent

trait ManagerView extends DialogView {

}

object ManagerView {

  def apply(stage: Stage): ManagerView = new ManagerViewFX(stage)

  private class ManagerViewFX(stage: Stage) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent{

    private var myController: ManagerController = _
    private var managerHome: ManagerHome = _

    /**
     * Closes the view.
     */
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = ManagerController()
      myController.setView(this)
      managerHome = ManagerHome()
      managerHome.setParent(this)
      pane.getChildren.add(managerHome.pane)
    }
  }
}
