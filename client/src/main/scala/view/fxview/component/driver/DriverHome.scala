package view.fxview.component.driver

import java.net.URL
import java.util.ResourceBundle

import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{InfoHome, InfoShift, StipendioInformations}
import javafx.fxml.FXML
import javafx.scene.control.{Accordion, Label, Menu}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{BorderPane, Pane}
import org.controlsfx.control.PopOver
import view.fxview.{FXHelperFactory, NotificationHelper}
import view.fxview.NotificationHelper.NotificationParameters
import view.fxview.component.driver.subcomponent.{HomeBox, SalaryBox, ShiftBox}
import view.fxview.component.driver.subcomponent.parent.DriverHomeParent
import view.fxview.component.{AbstractComponent, Component}

trait DriverHome extends Component[DriverHomeParent] {
  /**
   * draw common information for a driver, for example, shift in the day
   * extra day in week and calendar
   */
  def drawHome(infoHome: InfoHome): Unit

  /**
   * draw all shifth of a driver in the week
   */
  def drawShift(shift: InfoShift): Unit

  /**
   * method that call his parent and send list with all salary of a person
   *
   * @param list list of all salary for a person
   */
  def drawSalary(list: List[Stipendio]): Unit

  /**
   * method which enable view information for a salary in the specific month
   *
   * @param information case class with all presenze, absence and salary for a month
   */
  def informationSalary(information: StipendioInformations): Unit

  /**
   * set a Vbox in the center of the DriverHome, this happens if is present a error
   */
  def stopLoading(): Unit

  def drawNotifica(str: String, tag: Long): Unit
}
object DriverHome{
  def apply(): DriverHome = new DriverHomeFX()

  private class DriverHomeFX() extends AbstractComponent[DriverHomeParent] ("driver/DriverHome")
    with DriverHome {

    @FXML
    var driverHome:BorderPane = _
    @FXML
    var home:Menu = _
    @FXML
    var turni:Menu = _
    @FXML
    var stipendi:Menu = _
    @FXML
    var notifiche:Menu = _
    @FXML
    var labelHome:Label = _
    @FXML
    var labelTurni:Label = _
    @FXML
    var labelStipendio:Label = _
    @FXML
    var labelNotifiche:Label = _
    @FXML
    var popover: PopOver = _
    @FXML
    var accordion: Accordion = _

    var homeBox:HomeBox = _

    var shiftBox:ShiftBox = _

    var salaryBox:SalaryBox = _


    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      labelHome.setText(resources.getResource("home-label"))
      labelTurni.setText(resources.getResource("turno-label"))
      labelStipendio.setText(resources.getResource("stipendi-label"))
      labelNotifiche.setText(resources.getResource("notifiche-label"))
      home.setGraphic(labelHome)
      turni.setGraphic(labelTurni)
      stipendi.setGraphic(labelStipendio)
      notifiche.setGraphic(labelNotifiche)
      labelHome.setOnMouseClicked((_:MouseEvent)=>{
        callMethod((startLoading,parent.drawHomePanel))
      })
      labelTurni.setOnMouseClicked((_:MouseEvent)=>{
        callMethod((startLoading,parent.drawShiftPanel))
      })
      labelStipendio.setOnMouseClicked((_:MouseEvent)=>{
        callMethod((startLoading,parent.drawSalaryPanel))
      })
      labelNotifiche.setOnMouseClicked((_:MouseEvent)=>{
        openAccordion()
      })
    }
    private def callMethod(call:(()=>Unit,()=>Unit)): (Unit, Unit) =(call._1(),call._2())

    override def drawHome(infoHome: InfoHome): Unit = {
      endLoading()
      driverHome.setCenter(home(infoHome))
    }

    override def drawShift(shift: InfoShift): Unit =  {
      endLoading()
      driverHome.setCenter(this.shift(shift))
    }

    override def drawSalary(list:List[Stipendio]): Unit = {
      endLoading()
      driverHome.setCenter(salary(list))
    }


    private def home(infoHome: InfoHome):Pane = {
      homeBox = HomeBox(infoHome)
      homeBox.setParent(parent)
      homeBox.pane
    }

    private def shift(shift: InfoShift):Pane = {
      shiftBox = ShiftBox(shift)
      shiftBox.setParent(parent)
      shiftBox.pane
    }

    private def salary(list:List[Stipendio]):Pane = {
      salaryBox = SalaryBox(list)
      salaryBox.setParent(parent)
      salaryBox.pane
    }

    override def informationSalary(information: StipendioInformations): Unit = salaryBox.paneInfoSalary(information)

    override def stopLoading(): Unit ={
      endLoading()
      driverHome.setCenter(FXHelperFactory.defaultErrorPanel)
    }

    private def openAccordion(): Unit ={
      popover.show(labelNotifiche)
    }

    private def consumeNotification(tag: Long): Unit={

    }

    override def drawNotifica(str: String,tag:Long): Unit = {
      NotificationHelper.drawNotifica(str,tag, NotificationParameters(accordion,popover,consumeNotification))
    }
  }
}