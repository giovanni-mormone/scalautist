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
import utils.TransferObject.{DataForParamasModel, InfoRichiesta}
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.{ManagerHomeParent, ModalGruopParent, ModalParamParent, _}
import view.fxview.component.manager.subcomponent.util.ParamsForAlgorithm
import view.fxview.component.manager.subcomponent._
import view.fxview.component.modal.Modal
import view.mainview.ManagerView

object ManagerViewFX {

  def apply(stage: Stage,userName: String, userId:String): ManagerView = new ManagerViewFX(stage,userName,userId)

  private class ManagerViewFX(stage: Stage,userName: String, userId:String) extends AbstractFXDialogView(stage)
    with ManagerView with ManagerHomeParent with ManagerHomeModalParent{

    private var modalResource: Modal = _
    private var modalInfo :ModalInfo = _
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
      Option(modalInfo) match {
        case Some(modalInfo) if modalInfo.isShow => modalInfo.message("HOLA")
        case None => modalInfo =  ModalInfo(stage)
          modalInfo.start()
          modalInfo.message("HOLA")
      }
    }

    override def drawAbsencePanel(): Unit = {
      managerHome.startLoading()
      myController.dataToAbsencePanel()
    }

    override def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit = {
      managerHome.loadingReplacements()
      myController.absenceSelected(idRisultato, idTerminale, idTurno)
    }

    override def replacementSelected(idRisultato: Int, idPersona: Int): Unit =
      myController.replacementSelected(idRisultato, idPersona)

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
      case _ =>
        managerHome.endLoading()
        super.showMessageFromKey(message)
    }

    override def drawRichiestaPanel(): Unit = {
      myController.datatoRichiestaPanel()
    }

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

    override def weekParams(params: ParamsForAlgorithm): Unit =
      myController.weekParam(params)

    override def loadParam(param: InfoAlgorithm): Unit =
      Platform.runLater(() => {
        modalResource.close()
        managerHome.drawLoadedParam(param)
      })

    override def groupParam(params: ParamsForAlgorithm): Unit =
      myController.groupParam(params)

    override def resetWeekParams(): Unit =
      drawParamsPanel()

    override def drawWeekParam(params: ParamsForAlgorithm, rules: List[Regola]): Unit =
      Platform.runLater(() => managerHome.drawWeekParams(params, rules))

    override def drawGroupParam(params: ParamsForAlgorithm, rule: List[Regola]): Unit =
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
      Platform.runLater(() => modalResource.close())
      Platform.runLater(() => {
        modalResource = Modal[ModalParamParent, Component[ModalParamParent], ModalParamParent](myStage, caller = this,
          ParamsModal(data))
        modalResource.show()
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
      myController.infoParamToShow(idp, data)

    override def saveParam(param: InfoAlgorithm): Unit =
      myController.saveParam(param)
    override def openZonaModal(zona: Zona): Unit = {
      Platform.runLater(() => {
        //          homeView()
        modalResource = Modal[ModalZoneParent, Component[ModalZoneParent], ManagerHomeModalParent](myStage, this, ModalZone(zona))
        modalResource.show()
      })
    }

    override def newZona(zona: Zona): Unit =
      myController.saveZona(zona)

    override def deleteZona(zona: Zona): Unit =
      myController.deleteZona(zona)

    override def updateZona(zona: Zona): Unit =
      myController.updateZona(zona)

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


    override def showInfoAlgorithm(message:String):Unit={
      Platform.runLater(() =>{
        Option(modalInfo) match {
          case Some(modalInfo) if modalInfo.isShow => modalInfo.message(message)
          case None => modalInfo =  ModalInfo(stage)
            modalInfo.start()
        }
      })
    }

    override def confirmRun(messages: List[String], algorithmExecute: AlgorithmExecute): Unit = {
      println(messages)
      Platform.runLater(() =>{
        modalResource = Modal[ModalRunParent, Component[ModalRunParent], ModalRunParent](myStage, this,
          RunModal(messages, algorithmExecute))
        modalResource.show()
      })
    }

    override def executeAlgorthm(info: AlgorithmExecute): Unit = {
      Platform.runLater(() => modalResource.close())
      myController.executeAlgorithm(info)
    }

    override def cancel(): Unit =
      Platform.runLater(() => modalResource.close())
  }

}
