package view.fxview.mainview

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.{CaseClassDB, CaseClassHttpMessage}
import caseclass.CaseClassDB.{Terminale, Turno}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoReplacement, ResultAlgorithm}
import controller.ManagerController
import javafx.application.Platform
import javafx.stage.Stage
import utils.TransferObject.InfoRichiesta
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.manager.ManagerHome
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent

trait ManagerView extends DialogView {
  def drawNotification(str: String, tag: Long): Unit

  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit


  def drawShiftRequest(value: List[CaseClassDB.Turno]): Unit

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
   *
   * @param replacement
   */
  def drawReplacement(replacement: List[InfoReplacement]): Unit

  def drawResultTerminal(terminal: List[Terminale]): Unit

  def consumeNotification(tag: Long): Unit
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

    override def sendRichiesta(richiesta: InfoRichiesta): Unit = myController.sendRichiesta(richiesta)

    override def drawResultPanel(): Unit = myController.dataToResultPanel()

    override def drawResultTerminal(terminal: List[Terminale]):Unit =  Platform.runLater(() => managerHome.drawResultTerminal(terminal))

    override def resultForTerminal(value: Option[Int], date: Date, date1: Date): Unit = myController.resultForTerminal(value,date,date1)

    override def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit = Platform.runLater(() => managerHome.drawResult(resultList,dateList))

    override def drawNotification(str: String,tag:Long): Unit = Platform.runLater(()=>managerHome.drawNotifica(str,tag))

    override def consumeNotification(tag:Long):Unit=myController.consumeNotification(tag)
  }
}
