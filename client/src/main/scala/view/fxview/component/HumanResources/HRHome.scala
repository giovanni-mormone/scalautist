package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle
import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.Ferie
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, Pane}
import view.fxview.component.HumanResources.subcomponent._
import view.fxview.component.HumanResources.subcomponent.parent.HRHomeParent
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
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
  def drawRecruit(zone: List[Zona], contratti: List[Contratto], turni: List[Turno]): Unit

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

  def apply(userName: String, userId:String): HRHome = new HomeFX(userName,userId)

  /**
   * HRHome Fx implementation. It shows Humane resource home view
   *
   */
  private class HomeFX(userName: String, userId:String) extends AbstractComponent[HRHomeParent] ("humanresources/BaseHumanResource")
    with HRHome {

    @FXML
    var nameLabel: Label = _
    @FXML
    var idLabel: Label = _
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
    var terminalManger: Button = _
    @FXML
    var illness: Button = _
    @FXML
    var holidays: Button = _

    var recruitView: RecruitBox = _
    var fireView: FireBox = _
    var illBox: IllBox = _
    var holidayBox: HolidayBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText("I am HR king, and I BENEDICO to you!")

      recruitButton.setText(resources.getResource("recruit-button"))
      firesButton.setText(resources.getResource("fire-button"))
      illness.setText(resources.getResource("illness-button"))
      holidays.setText(resources.getResource("holiday-button"))
      zonaManage.setText(resources.getResource("zonaManage"))
      terminalManger.setText(resources.getResource("terminalManager"))
      changePassword.setText(resources.getResource("changePassword"))
      nameLabel.setText(resources.println("username-label",userName))
      idLabel.setText(resources.println("id-label",userId))


      recruitButton.setOnAction(_ => parent.drawRecruitPanel)
      firesButton.setOnAction(_ => parent.drawEmployeePanel(EmployeeView.fire))
      changePassword.setOnAction(_ => parent.drawChangePassword)
      illness.setOnAction(_ => parent.drawEmployeePanel(EmployeeView.ill))
      holidays.setOnAction(_ => parent.drawHoliday())
    }

    /////////////////////////////////////////////////////////////////////////////////// panel drawing method

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      baseHR.setCenter(recruitBox(zones, contracts, shifts))

    override def drawRecruitTerminals(terminals: List[Terminale]): Unit =
      recruitView.setTerminals(terminals)

    override def drawFire(employees: List[Persona]): Unit =
      baseHR.setCenter(fireBox(employees))

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

    private def illBox(employees: List[Persona]): Pane = {
      illBox = IllBox(employees)
      illBox.setParent(parent).pane
      illBox.pane
    }

    private def holidayBox(employees: List[Ferie]): Pane = {
      holidayBox = HolidayBox(employees)
      holidayBox.setParent(parent)
      holidayBox.pane
    }
  }
}