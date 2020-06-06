package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, Pane}
import view.fxview.component.HumanResources.subcomponent.parent.HRHomeParent
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
import view.fxview.component.HumanResources.subcomponent._
import view.fxview.component.{AbstractComponent, Component}


/**
 * @author Francesco Cassano
 * 
 * Interface allows to communicate with the internal view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.HRHomeParent]]
 */
trait HRHome extends Component[HRHomeParent]{

  /**
   * Initialize Recruit view before show
   *
   * @param zone
   *             List of [[caseclass.CaseClassDB.Zona]] to use in view
   * @param contratti
   *                  List of [[caseclass.CaseClassDB.Contratto]] type to use in view
   * @param turni
   *              List of [[caseclass.CaseClassDB.Turno]] type to use in view
   */
  def drawRecruit(zone: List[Zona], contratti: List[Contratto], turni: List[Turno])

  /**
   * Show Terminale after Zona is chosen
   *
   * @param terminali
   *                  List of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawRecruitTerminals(terminali: List[Terminale]): Unit

  /**
   * Initialize Fire view before show
   *
   * @param employees
   *                  List of [[caseclass.CaseClassDB.Persona]]
   */
  def drawFire(employees: List[Persona]): Unit

  /**
   * Initialize zona Manager view before show
   *
   * @param zones
   * *                  List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZona(zones: List[Zona]):Unit

  /**
   *
   * @param employees
   */
  def drawIllBox(employees: List[Persona]): Unit

  /**
   *
   * @param employees
   */
  def drawHolidayBox(employees: List[Ferie]): Unit
}


/////////////////////////////////////////////////////////////////// Companion object
/**
 * @author Francesco Cassano
 *
 * Companion object of [[view.fxview.component.HumanResources.HRHome]]
 *
 */
object HRHome{

  def apply(): HRHome = new HomeFX()

  /**
   * HRHome Fx implementation. It shows Humane resource home view
   *
   */
  private class HomeFX() extends AbstractComponent[HRHomeParent] ("humanresources/BaseHumanResource")
    with HRHome {

    @FXML
    var nameLabel: Label = _
    @FXML
    var baseHR: BorderPane = _
    @FXML
    var recruitButton: Button = _
    @FXML
    var firesButton: Button = _
    @FXML
    var changePassword: Button = _
    @FXML
    var zonaManage: Button = _
    @FXML
    var illness:Button = _
    @FXML
    var holidays:Button = _


    var recruitView: RecruitBox = _
    var fireView: FireBox = _
    var zonaView: ZonaBox = _
    var illBox:IllBox = _
    var holidayBox:HolidayBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText("sono il Re delle risorse umane, e ti BENEDICO")

      recruitButton.setText(resources.getString("recruit-button"))
      firesButton.setText(resources.getString("fire-button"))
      illness.setText(resources.getString("illness-button"))
      holidays.setText(resources.getString("holiday-button"))
      zonaManage.setText(resources.getString("zonaManage"))
      changePassword.setText(resources.getString("changePassword"))

      recruitButton.setOnAction(_ => parent.drawRecruitPanel)
      firesButton.setOnAction(_ => parent.drawEmployeePanel(EmployeeView.fire))

      zonaManage.setOnAction(_ => parent.drawZonePanel)
      changePassword.setOnAction(_ => parent.drawChangePassword)
      illness.setOnAction(_ =>parent.drawEmployeePanel(EmployeeView.ill))
      holidays.setOnAction(_ =>parent.drawHoliday())
    }

    /////////////////////////////////////////////////////////////////////////////////// panel drawing method

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      baseHR.setCenter(recruitBox(zones, contracts, shifts))

    override def drawRecruitTerminals(terminals: List[Terminale]): Unit =
      recruitView.setTerminals(terminals)

    override def drawFire(employees: List[Persona]): Unit =
      baseHR.setCenter(fireBox(employees))

    override def drawZona(zones: List[Zona]): Unit =
      baseHR.setCenter(zonaBox(zones))

    override def drawIllBox(employees: List[Persona]): Unit =
      baseHR.setCenter(illBox(employees))

    override def drawHolidayBox(employees: List[Ferie]): Unit =
      baseHR.setCenter(holidayBox(employees))
    ////////////////////////////////////////////////////////////////////////////////////// View Initializer

    private def recruitBox(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Pane = {
      recruitView = RecruitBox(contracts, shifts, zones)
      recruitView.setParent(parent)
      recruitView.pane
    }

    private def fireBox(employees: List[Persona]): Pane = {
      fireView = FireBox(employees)
      fireView.setParent(parent)
      fireView.pane
    }

    private def zonaBox(zones: List[Zona]): Pane = {
      zonaView = ZonaBox(zones)
      zonaView.setParent(parent)
      zonaView.pane
    }

    private def illBox(employees: List[Persona]): Pane = {
      illBox = IllBox(employees)
      illBox.setParent(parent)
      illBox.pane
    }

    private def holidayBox(employees: List[Ferie]): Pane = {
      holidayBox = HolidayBox(employees)
      holidayBox.setParent(parent)
      holidayBox.pane
    }
  }
}