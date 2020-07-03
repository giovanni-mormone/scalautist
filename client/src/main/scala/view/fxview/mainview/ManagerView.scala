package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{Parametro, Terminale, Turno}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent

trait ManagerView extends DialogView {

  def drawShiftRequest(value: List[Turno]):Unit

  def drawRichiesta(terminal: List[Terminale]): Unit


  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the list of turns that needs
   * a replacement
   *
   * @param absences
   */
  def drawAbsence(absences: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the list of people that needs
   * a replacement
   * @param replacement
   */
  def drawReplacement(replacement: List[InfoReplacement]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view ro draw the panel to choice parameters to run
   * shift assignment algorithm
   *
   * @param oldParameters the list of old parameters to show
   */
  def drawRunAlgorithm(oldParameters: List[Parametro]): Unit
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
    override def drawAbsencePanel(): Unit = {
      managerHome.startLoading()
      myController.dataToAbsencePanel()
    }


    ////////CALLS FROM CHILDREN TO MAKE THINGS -> ASK TO CONTROLLER
    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      managerHome.loadingReplacements()
      myController.absenceSelected(idRisultato, idTerminale, idTurno)
    }

    override def replacementSelected(idRisultato: Int, idPersona: Int): Unit =
      myController.replacementSelected(idRisultato, idPersona)


    //////CALLS FROM CONTROLLER////////////
    override def drawAbsence(absences: List[InfoAbsenceOnDay]): Unit =
      Platform.runLater(() =>{
        managerHome.endLoading()
        managerHome.drawManageAbsence(absences)
      })

    override def drawReplacement(replacement: List[InfoReplacement]): Unit =
      Platform.runLater(() => managerHome.drawManageReplacement(replacement))

    override def showMessageFromKey(message: String): Unit = message match {
      case "no-replacement-error" =>
        Platform.runLater(() => {
          super.showMessageFromKey(message)
          managerHome.stopLoadingReplacements()
        })
      case _ => super.showMessageFromKey(message)
    }

    override def drawRichiestaPanel(): Unit = myController.datatoRichiestaPanel()

    override def drawRichiesta(terminal: List[Terminale]): Unit =  Platform.runLater(() => managerHome.drawRichiesta(terminal))

    override def selectShift(idTerminal: Int): Unit = myController.selectShift(idTerminal)

    override def drawShiftRequest(listShift: List[Turno]): Unit = {
      Platform.runLater(() => managerHome.drawShiftRichiesta(listShift))
    }

    override def showBackMessage(str: String): Unit = {
       if(alertMessage(str)) managerHome.reDrawRichiesta()
    }

    override def sendRichiesta(richiesta: InfoRichiesta): Unit =
      myController.sendRichiesta(richiesta)

    override def drawRunAlgorithm(oldParameters: List[Parametro]): Unit =
      Platform.runLater(() => managerHome.drawChooseParams(oldParameters))

  }
}
