package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoAbsence
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent

trait ManagerView extends DialogView {

  def drawAbsence(absences: List[InfoAbsence]): Unit
}

object ManagerView {

  def apply(stage: Stage): ManagerView = new ManagerViewFX(stage)

  private class ManagerViewFX(stage: Stage) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent{

    private var myController: ManagerController = _
    private var managerHome: ManagerHome = _

    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = ManagerController()
      myController.setView(this)
      managerHome = ManagerHome()
      pane.getChildren.add(managerHome.setParent(this).pane)
    }

    /////////CALLS FROM CHILDREN TO DRAW -> SEND TO CONTROLLER/////////////
    override def drawAbsencePanel(): Unit =
      myController.dataToAbsencePanel()


    ////////CALLS FROM CHILDREN TO MAKE THINGS -> ASK TO CONTROLLER
    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      myController.absenceSelected(idRisultato, idTerminale, idTurno)
    }

    //////CALLS FROM CONTROLLER////////////
    override def drawAbsence(absences: List[InfoAbsence]): Unit =
      Platform.runLater(() => managerHome.drawManageAbsence(absences))
  }
}
