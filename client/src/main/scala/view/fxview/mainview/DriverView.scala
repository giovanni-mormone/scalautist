package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, StipendioInformations}
import controller.DriverController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.component.Component
import view.fxview.{AbstractFXDialogView, FXHelperFactory}
import view.fxview.component.driver.DriverHome
import view.fxview.component.driver.subcomponent.ModalDisponibilita
import view.fxview.component.driver.subcomponent.parent.{DriverHomeParent, DriverModalBoxParent, ModalDisponibilitaParent}
import view.fxview.component.modal.Modal

trait DriverView extends DialogView{
  /**
   *
   */
  def drawHomeView(infoHome: InfoHome):Unit

  /**
   * method which draw view that contains all shift of a driver into 7 days
   */
  def drawShiftView(shift: InfoShift):Unit

  /**
   * method that draw view for salary for a driver
   * @param list list of all salary for a driver
   */
  def drawSalaryView(list:List[Stipendio]):Unit

  /**
   * method which enable view information for a salary in the specific month
   * @param information case class with all presenze, absence and salary for a month
   */
  def informationSalary(information:StipendioInformations):Unit

  def drawDisponibilitaPanel(days: List[String]): Unit

  def disponibilityInserted(): Unit

  def messageErrorSalary(message:String):Unit
}
object DriverView {
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

    /**
     * Closes the view.
     */
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = DriverController()
      myController.setView(this)
      driverHome = DriverHome()
      driverHome.setParent(this)
      pane.getChildren.add(driverHome.pane)
      myController.startupDriverCheck()
    }

    ///////////////////////////////////////////////////////////////// Da VIEW A CONTROLLER impl DriverView
    override def drawHomePanel(): Unit = myController.drawHomePanel()

    override def drawShiftPanel(): Unit = myController.drawShiftPanel()


    override def drawSalaryPanel(): Unit = myController.drawSalaryPanel()

    ///////////////////////////////////////////////////////////////// Fine VIEW A CONTROLLER impl DriverView

    ///////////////////////////////////////////////////////////////// Da VIEW STIPENDIO A CONTROLLER impl DriverView
    override def infoSalary(idSalary: Int): Unit = myController.drawInfoSalary(idSalary)

    ///////////////////////////////////////////////////////////////// Fine VIEW STIPENDIO A CONTROLLER impl DriverView

    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl DriverView
    override def drawHomeView(infoHome: InfoHome): Unit = Platform.runLater(() => driverHome.drawHome(infoHome))

    override def drawShiftView(shift: InfoShift): Unit = Platform.runLater(() => driverHome.drawShift(shift))

    override def drawSalaryView(list: List[Stipendio]): Unit = Platform.runLater(() => driverHome.drawSalary(list))

    override def informationSalary(information: StipendioInformations): Unit =
      Platform.runLater(() => driverHome.informationSalary(information))

    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl DriverView
    override def messageErrorSalary(message: String): Unit = {
      Platform.runLater(() => {
        super.showMessage(generalResources.getResource(message))
        driverHome.stopLoading()
      })
    }

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
  }
}