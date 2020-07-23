package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Parametro, Regola, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{GruppoA, InfoAbsenceOnDay, InfoAlgorithm, InfoReplacement}
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.ParamsModal
import view.fxview.component.manager.subcomponent.parent.{ManagerHomeParent, ModalParamParent}
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm
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
   * @param terminals the list of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawRunAlgorithm(terminals: List[Terminale]): Unit

  /**
   * The method draws the list of [[caseclass.CaseClassDB.Parametro]] and it allows to choose params
   *
   * @param olds list of [[caseclass.CaseClassDB.Parametro]]
   */
  def modalOldParamDraw(olds: List[InfoAlgorithm], terminals: List[Terminale], rules: List[Regola]): Unit

  /**
   *
   * @param params
   */
  def drawWeekParam(params: ParamsForAlgoritm, rules: List[Regola])

  /**
   *
   * @param params
   */
  def drawGroupParam(params: ParamsForAlgoritm, groups: List[GruppoA], rule: List[Regola])
}

object ManagerView {

  def apply(stage: Stage): ManagerView = new ManagerViewFX(stage)

  private class ManagerViewFX(stage: Stage) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent {

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

    override def drawRunAlgorithm(terminals: List[Terminale]): Unit =
      Platform.runLater(() => managerHome.drawChooseParams(terminals))

    override def drawParamsPanel(): Unit =
      myController.chooseParams()

    override def modalOldParam(terminals: List[Terminale]): Unit =
      myController.modalOldParams(terminals)

    override def modalOldParamDraw(olds: List[InfoAlgorithm], terminals: List[Terminale], rules: List[Regola]): Unit =
      Platform.runLater(() =>{
        modalResource = Modal[ModalParamParent, Component[ModalParamParent], ModalParamParent](myStage, caller = this,
          ParamsModal(olds, terminals, rules))
        modalResource.show()
      })

    override def weekParams(params: ParamsForAlgoritm): Unit =
      myController.weekParam(params)

    override def loadParam(param: InfoAlgorithm): Unit =
      Platform.runLater(() => {
        modalResource.close()
        managerHome.drawLoadedParam(param)
      })

    override def groupParam(params: ParamsForAlgoritm): Unit =
      myController.groupParam(params)

    override def resetWeekParams(): Unit =
      drawParamsPanel()

    override def drawWeekParam(params: ParamsForAlgoritm, rules: List[Regola]): Unit =
      Platform.runLater(() => managerHome.drawWeekParams(params, rules))

    override def drawGroupParam(params: ParamsForAlgoritm, groups: List[GruppoA], rule: List[Regola]): Unit =
      Platform.runLater(() => managerHome.drawGroupsParam(params, groups, rule))

    override def resetGroupsParams(): Unit =
      drawParamsPanel()
  }
}
