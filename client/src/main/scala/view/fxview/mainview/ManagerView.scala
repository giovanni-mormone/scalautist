package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Parametro, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement}
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent
import view.fxview.component.modal.Modal

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
   * @param replacement list of InfoReplacement
   */
  def drawReplacement(replacement: List[InfoReplacement]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the panel to choice parameters to run
   * shift assignment algorithm
   *
   * @param zonesList the list of [[caseclass.CaseClassDB.Zona]]
   */
  def drawRunAlgorithm(zonesList: List[Zona]): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to full the list of
   * [[caseclass.CaseClassDB.Terminale]] that the user can choose
   *
   * @param terminalsList list of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminalForParam(terminalsList: List[Terminale]): Unit

  /**
   * Method allows to draw the modal for choosing params
   */
  def modalOldParam(): Unit

  /**
   * The method draws the list of [[caseclass.CaseClassDB.Parametro]] and it allows to choose params
   *
   * @param olds list of [[caseclass.CaseClassDB.Parametro]]
   */
  def modalOldParamDraw(olds: List[Parametro]): Unit
}

object ManagerView {

  def apply(stage: Stage): ManagerView = new ManagerViewFX(stage)

  private class ManagerViewFX(stage: Stage) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent{

    private var modalResource: Modal = _
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

    override def drawRichiesta(terminal: List[Terminale]): Unit = Platform.runLater(() => managerHome.drawRichiesta(terminal))

    override def selectShift(idTerminal: Int): Unit = myController.selectShift(idTerminal)

    override def drawShiftRequest(listShift: List[Turno]): Unit = {
      Platform.runLater(() => managerHome.drawShiftRichiesta(listShift))
    }

    override def showBackMessage(str: String): Unit = {
       if(alertMessage(str)) managerHome.reDrawRichiesta()
    }

    override def sendRichiesta(richiesta: InfoRichiesta): Unit =
      myController.sendRichiesta(richiesta)

    override def drawRunAlgorithm(zonesList: List[Zona]): Unit =
      Platform.runLater(() => managerHome.drawChooseParams(zonesList))

    override def calculateShifts(params: Parametro, save: Boolean): Unit = {
      println("here")
    }

    override def getTerminals(zone: Zona): Unit = {
      myController.getTerminalsToParam(zone)
    }

    override def drawParamsPanel(): Unit =
      myController.zonesToParams()

    override def drawTerminalForParam(terminals: List[Terminale]): Unit =
      Platform.runLater(() => managerHome.showTerminalsParam(terminals))

    override def modalOldParam(): Unit =
      println("params")

    override def modalOldParamDraw(olds: List[Parametro]): Unit = {
      Platform.runLater(() =>{
        modalResource = Modal[ModalParamParent]
      })
    }
  }
}
