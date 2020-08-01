package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, StipendioInformations}
import controller.DriverController
import javafx.application.Platform
import javafx.stage.Stage
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.driver.DriverHome
import view.fxview.component.driver.subcomponent.ModalDisponibilita
import view.fxview.component.driver.subcomponent.parent.{DriverHomeParent, DriverModalBoxParent, ModalDisponibilitaParent}
import view.fxview.component.modal.Modal
import view.fxview.util.ResourceBundleUtil._
import view.mainview.DriverView

object DriverViewFX {
  def apply(stage: Stage): DriverView = new DriverViewHomeFX(stage)

  /**
   * DriverHomeView FX implementation
   *
   * @param stage
   *              Stage that load view
   */
  private class DriverViewHomeFX(stage: Stage) extends AbstractFXDialogView(stage)
    with DriverView with DriverHomeParent with DriverModalBoxParent {

    private var myController: DriverController = _
    private var driverHome: DriverHome = _
    private var modal: Modal = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = DriverController()
      myController.setView(this)
      driverHome = DriverHome()
      driverHome.setParent(this)
      pane.getChildren.add(driverHome.pane)
      myController.startupDriverCheck()
      myController.startListenNotification()

    }

    override def drawHomePanel(): Unit = myController.drawHomePanel()

    override def drawShiftPanel(): Unit = myController.drawShiftPanel()

    override def drawSalaryPanel(): Unit = myController.drawSalaryPanel()

    override def infoSalary(idSalary: Int): Unit = myController.drawInfoSalary(idSalary)

    override def drawHomeView(infoHome: InfoHome): Unit = Platform.runLater(() => driverHome.drawHome(infoHome))

    override def drawShiftView(shift: InfoShift): Unit = Platform.runLater(() => driverHome.drawShift(shift))

    override def drawSalaryView(list: List[Stipendio]): Unit = Platform.runLater(() => driverHome.drawSalary(list))

    override def informationSalary(information: StipendioInformations): Unit = Platform.runLater(() => driverHome.informationSalary(information))

    override def showMessageError(message: String): Unit =
      Platform.runLater(() => {
        super.showMessageFromKey(message)
        driverHome.stopLoading()
      })

    override def drawDisponibilitaPanel(days: List[String]): Unit =
      Platform.runLater(() => {
        modal = Modal[ModalDisponibilitaParent, Component[ModalDisponibilitaParent], DriverModalBoxParent](myStage, this, ModalDisponibilita(days), closable = false)
        modal.show()
      })

    override def selectedDays(day1: String, day2: String): Unit = {
      modal.startLoading()
      myController.sendDisponibility(day1,day2)
    }

    override def disponibilityInserted(): Unit = {
      Platform.runLater(()=> {
        modal.showMessage(generalResources.getResource("disponibilita-inserted"))
        modal.close()
        myController.drawHomePanel()
      })
    }

    override def showMessageFromKey(message: String): Unit = message match {
      case "update-disponibility-failed" =>
        Platform.runLater(()=>{
          super.showMessageFromKey(message)
          modal.endLoading()
        })
      case _ => super.showMessageFromKey(message)
    }

    override def drawNotification(str: String,tag:Long): Unit = Platform.runLater(()=>driverHome.drawNotifica(str,tag))

  }
}