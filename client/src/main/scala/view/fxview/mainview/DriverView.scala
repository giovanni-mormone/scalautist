package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.StipendioInformations
import controller.DriverController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.driver.DriverHome
import view.fxview.component.driver.subcomponent.parent.DriverHomeParent

trait DriverView extends DialogView{
  /**
   *
   */
  def drawHomeView():Unit

  /**
   * method which draw view that contains all shift of a driver into 7 days
   */
  def drawShiftView():Unit

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
    with DriverView with DriverHomeParent{

    private var myController: DriverController = _
    private var driverHome: DriverHome = _

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
    }
    ///////////////////////////////////////////////////////////////// Da VIEW A CONTROLLER impl DriverView
    override def drawHomePanel(): Unit = myController.drawHomePanel()

    override def drawTurnoPanel(): Unit = myController.drawShiftPanel()

    override def drawStipendioPanel(): Unit = myController.drawSalaryPanel()

    ///////////////////////////////////////////////////////////////// Fine VIEW A CONTROLLER impl DriverView

    ///////////////////////////////////////////////////////////////// Da VIEW STIPENDIO A CONTROLLER impl DriverView
    override def infoSalary(idSalary:Int): Unit = myController.drawInfoSalary(idSalary)

    ///////////////////////////////////////////////////////////////// Fine VIEW STIPENDIO A CONTROLLER impl DriverView

    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl DriverView
    override def drawHomeView(): Unit = driverHome.drawHome()

    override def drawShiftView(): Unit = driverHome.drawShift()

    override def drawSalaryView(list:List[Stipendio]): Unit = Platform.runLater(()=>driverHome.drawSalary(list))

    override def informationSalary(information: StipendioInformations): Unit =
      Platform.runLater(()=>driverHome.informationSalary(information))

    override def showMessage(message: String): Unit = Platform.runLater(()=>super.showMessage(message))
    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl DriverView
  }
}