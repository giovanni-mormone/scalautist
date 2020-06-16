package view.fxview.component.driver

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Stipendio, Turno}
import caseclass.CaseClassHttpMessage.{InfoHome, StipendioInformations}
import javafx.fxml.FXML
import javafx.scene.control.{Label, Menu}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{BorderPane, Pane}
import view.fxview.FXHelperFactory
import view.fxview.component.driver.subcomponent.{HomeBox, SalaryBox, ShiftBox}
import view.fxview.component.driver.subcomponent.parent.DriverHomeParent
import view.fxview.component.{AbstractComponent, Component}

trait DriverHome extends Component[DriverHomeParent]{
  /**
   *
   */
  def drawHome(infoHome: InfoHome):Unit

  /**
   *
   */
  def drawShift(shift:List[Turno]):Unit

  /**
   * method that call his parent and send list with all salary of a person
   * @param list list of all salary for a person
   */
  def drawSalary(list:List[Stipendio]):Unit

  /**
   * method which enable view information for a salary in the specific month
   * @param information case class with all presenze, absence and salary for a month
   */
  def informationSalary(information:StipendioInformations):Unit
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
    var labelHome:Label = _
    @FXML
    var labelTurni:Label = _
    @FXML
    var labelStipendio:Label = _

    var homeBox:HomeBox = _

    var shiftBox:ShiftBox = _

    var salaryBox:SalaryBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      labelHome.setText(resources.getString("home-label"))
      labelTurni.setText(resources.getString("turno-label"))
      labelStipendio.setText(resources.getString("stipendi-label"))
      home.setGraphic(labelHome)
      turni.setGraphic(labelTurni)
      stipendi.setGraphic(labelStipendio)
      labelHome.setOnMouseClicked((_:MouseEvent)=>parent.drawHomePanel())
      labelTurni.setOnMouseClicked((_:MouseEvent)=>parent.drawShiftPanel())
      labelStipendio.setOnMouseClicked((_:MouseEvent)=>{
        driverHome.setCenter(FXHelperFactory.loadingBox)
        parent.drawSalaryPanel()
      })
    }

    override def drawHome(infoHome: InfoHome): Unit = driverHome.setCenter(home(infoHome))

    override def drawShift(shift:List[Turno]): Unit = driverHome.setCenter(this.shift(shift))

    override def drawSalary(list:List[Stipendio]): Unit = driverHome.setCenter(salary(list))


    private def home(infoHome: InfoHome):Pane = {
      homeBox = HomeBox(infoHome)
      homeBox.setParent(parent)
      homeBox.pane
    }

    private def shift(list:List[Turno]):Pane = {
      shiftBox = ShiftBox(list)
      shiftBox.setParent(parent)
      shiftBox.pane
    }

    private def salary(list:List[Stipendio]):Pane = {
      salaryBox = SalaryBox(list)
      salaryBox.setParent(parent)
      salaryBox.pane
    }

    override def informationSalary(information: StipendioInformations): Unit = salaryBox.paneInfoSalary(information)
  }
}