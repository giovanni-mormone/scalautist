package view.fxview.mainview

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage._
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.ParamsModal.DataForParamasModel
import view.fxview.component.manager.subcomponent.parent.{ManagerHomeParent, ModalGruopParent, ModalParamParent, _}
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm
import view.fxview.component.manager.subcomponent._
import view.fxview.component.modal.Modal

trait ManagerView extends DialogView {
  /**
   *
   * @param info
   * @param name
   * @param terminals
   * @param rules
   */
  def drawShowParams(info: AlgorithmExecute, name: Option[String], terminals: List[Terminale], rules: List[Regola]): Unit

  def drawNotification(str: String, tag: Long): Unit

  /**
   * method that draw result for all driver in the time frame
   * @param resultList all result of the driver with yours information for selected time frame
   * @param dateList list of all date that the manager wants to see
   */
  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit

  /**
   * method that draw all shift that existing in database this is for theorical request
   * @param value List with all shift
   */
  def drawShiftRequest(value: List[Turno]): Unit

  /**
   * method that draw all terminal that existing in database this is for theorical request
   * @param terminal list with all terminal that existing in database
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * method that send InfoRichiesta, this case class contains all information
   * for the request and the terminal that this infoRichiesta is associated
   * @param richiesta case class that all information relationship with theorical request
   */
  def sendRichiesta(richiesta: InfoRichiesta): Unit

  /**
   * Show the zone view
   *
   * @param zones
   *              List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZonaView(zones: List[Zona]): Unit

  /**
   * show terminal view
   *
   * @param zones
   *              Listo of [[caseclass.CaseClassDB.Zona]]
   * @param terminals
   *                   Listo of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit

  /**
   * show terminal modal
   *
   * @param zoneList
   *                 List of [[caseclass.CaseClassDB.Zona]]
   * @param terminal
   *                 instance of [[caseclass.CaseClassDB.Terminale]]
   */
  def openTerminalModal(zoneList: List[Zona], terminal: Terminale): Unit

  /**
   * Method used by a [[controller.ManagerController]] to tell the view to draw the list of turns that needs
   * a replacement
   *
   * @param absences case class that contains info with all absence for this day
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
  def modalOldParamDraw(olds: List[Parametro], terminals: List[Terminale], rules: List[Regola]): Unit

  /**
   *
   * @param data
   */
  def showInfoParam(data: DataForParamasModel)

  /**
   *
   * @param params
   */
  def drawWeekParam(params: ParamsForAlgoritm, rules: List[Regola])

  /**
   *
   * @param params
   */
  def drawGroupParam(params: ParamsForAlgoritm,  rule: List[Regola])

  def drawResultTerminal(terminal: List[Terminale]): Unit

  def refreshTerminalPanel(messageKey: String): Unit
  def refreshZonaPanel(messageKey: String): Unit

  def consumeNotification(tag: Long): Unit
}

object ManagerView {

  def apply(stage: Stage,userName: String, userId:String): ManagerView = new ManagerViewFX(stage,userName,userId)

  private class ManagerViewFX(stage: Stage,userName: String, userId:String) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent with ManagerHomeModalParent{

    private var modalResource: Modal = _
    private var myController: ManagerController = _
    private var managerHome: ManagerHome = _
    private val REPLACEMENT_WITHOUT_ERROR = "no-replacement-error"
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = ManagerController()
      myController.setView(this)
      managerHome = ManagerHome(userName,userId)
      pane.getChildren.add(managerHome.setParent(this).pane)
      myController.startListenNotification()
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
      case REPLACEMENT_WITHOUT_ERROR =>
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

    override def drawRunAlgorithm(terminals: List[Terminale]): Unit =
      Platform.runLater(() => managerHome.drawChooseParams(terminals))

    override def drawParamsPanel(): Unit =
      myController.chooseParams()

    override def modalOldParam(terminals: List[Terminale]): Unit =
      myController.modalOldParams(terminals)

    override def modalOldParamDraw(olds: List[Parametro], terminals: List[Terminale], rules: List[Regola]): Unit =
      Platform.runLater(() =>{
        modalResource = Modal[ModalParamParent, Component[ModalParamParent], ModalParamParent](myStage, caller = this,
          ParamsModal(DataForParamasModel(olds, terminals, rules)))
        modalResource.show()
      })

    override def openModal(initDate: LocalDate, endDate: LocalDate, dateNo: List[LocalDate], rules: List[Regola]): Unit = {
      Platform.runLater(() => {
        modalResource = Modal[ModalGruopParent, Component[ModalGruopParent], ModalGruopParent](myStage, caller = this,
          GroupModal(initDate, endDate, dateNo, rules))
        modalResource.show()
      })
    }

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

    override def drawGroupParam(params: ParamsForAlgoritm, rule: List[Regola]): Unit =
      Platform.runLater(() => managerHome.drawGroupsParam(params, rule))

    override def resetGroupsParams(): Unit =
      drawParamsPanel()

    override def sendRichiesta(richiesta: InfoRichiesta): Unit = myController.sendRichiesta(richiesta)

    override def drawResultPanel(): Unit = myController.dataToResultPanel()

    override def drawResultTerminal(terminal: List[Terminale]):Unit =  Platform.runLater(() =>
      managerHome.drawResultTerminal(terminal)
     )

    override def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit = {
      managerHome.loadingResult()
      myController.resultForTerminal(value,date,date1)
    }

    override def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit = Platform.runLater(() => {
      managerHome.stopLoadingResult()
      managerHome.drawResult(resultList,dateList)
    })

    override def drawNotification(str: String,tag:Long): Unit = Platform.runLater(()=>managerHome.drawNotifica(str,tag))

    override def consumeNotification(tag:Long):Unit=myController.consumeNotification(tag)

    override def updateGroups(group: GroupParamsBox.Group): Unit = {
      modalResource.close()
      managerHome.updateGroup(group)
    }

    override def showParams(info: AlgorithmExecute, name: Option[String]): Unit =
      myController.showParamAlgorithm(info, name)

    override def run(info: AlgorithmExecute): Unit =
      myController.runAlgorithm(info)

    override def resetParams(): Unit =
      drawParamsPanel()

    override def drawShowParams(info: AlgorithmExecute, name: Option[String], terminals: List[Terminale], rules: List[Regola]): Unit =
      Platform.runLater(() => managerHome.drawShowParams(info, name, terminals, rules))

    override def showInfoParam(data: DataForParamasModel): Unit = {
      Platform.runLater(() => {
        modalResource.close()
        modalResource = Modal[ModalParamParent, Component[ModalParamParent], ModalParamParent](myStage, caller = this,
          ParamsModal(data))
      })
    }

    override def drawZonaView(zones: List[Zona]): Unit =
      Platform.runLater(() => managerHome.drawZona(zones))

    override def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit =
      Platform.runLater(() => managerHome.drawTerminal(zones, terminals))

    override def openTerminalModal(zoneList: List[Zona], terminal: Terminale): Unit = {
      Platform.runLater(() => {
        modalResource = Modal[ModalTerminalParent, Component[ModalTerminalParent], ManagerHomeModalParent](myStage, this, ModalTerminal(zoneList, terminal))
        modalResource.show()
      })
    }

    override def getInfoToShow(idp: Int, data: DataForParamasModel): Unit =
      myController.getInfoParamToShow(idp, data)

    override def saveParam(param: InfoAlgorithm): Unit =
      myController.saveParam(param)
      override def openZonaModal(zona: Zona): Unit = {
        Platform.runLater(() => {
//          homeView()
          modalResource = Modal[ModalZoneParent, Component[ModalZoneParent], ManagerHomeModalParent](myStage, this, ModalZone(zona))
          modalResource.show()
        })
      }

    /////////////////////////////////////////////////////////   zona
    override def newZona(zona: Zona): Unit =
      myController.saveZona(zona)

    override def deleteZona(zona: Zona): Unit =
      myController.deleteZona(zona)

    override def updateZona(zona: Zona): Unit =
      myController.updateZona(zona)

    /////////////////////////////////////////////////////////   terminale
    override def newTerminale(terminal: Terminale): Unit =
      myController.saveTerminal(terminal)

    override def deleteTerminal(terminal: Terminale): Unit =
      myController.deleteTerminal(terminal)

    override def updateTerminal(terminal: Terminale): Unit =
      myController.updateTerminal(terminal)

    override def drawZonePanel(): Unit =
      myController.dataToZone()

    override def drawTerminalPanel(): Unit =
      myController.dataToTerminal()

    override def openTerminalModal(terminal: Int): Unit =
      myController.terminalModalData(terminal)

    override def refreshTerminalPanel(messageKey: String): Unit =
      Platform.runLater(() => {
        showMessageFromKey(messageKey)
        verifyModalResource(drawTerminalPanel)
      })

    override def refreshZonaPanel(messageKey: String): Unit =
      Platform.runLater(() => {
        showMessageFromKey(messageKey)
        verifyModalResource(drawZonePanel)
      })

    private def verifyModalResource(function: () => Unit): Unit = {
      Option(modalResource) match {
        case None => function()
        case _ =>
          modalResource.close()
          function()
      }
    }
  }

}
