package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, Pane}
import view.fxview.component.HumanResources.subcomponent.employee.EmployeeView
import view.fxview.component.HumanResources.subcomponent.parent.HRHomeParent
import view.fxview.component.HumanResources.subcomponent.{FireBox, RecruitBox}
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
   *             List of zona to use in view
   * @param contratti
   *                  List of contratti type to use in view
   * @param turni
   *              List of turni type to use in view
   */
  def drawRecruit(zone: List[Zona], contratti: List[Contratto], turni: List[Turno])

  /**
   * Show Terminale after Zona is chosen
   *
   * @param terminali
   */
  def drawRecruitTerminals(terminali: List[Terminale]): Unit

  /**
   * Initialize Fire view before show
   *
   * @param employees
   */
  def drawFire(employees: List[Persona]): Unit
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
   */
  private class HomeFX() extends AbstractComponent[HRHomeParent] ("humanresources/BaseHumanResource")
    with HRHome {

    @FXML
    var baseHR: BorderPane = _
    @FXML
    var recruitButton: Button = _
    @FXML
    var firesButton: Button = _
    @FXML
    var nameLabel: Label = _

    var recruitView: RecruitBox = _
    var fireView: FireBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText("Buongiorno Stronzo")

      recruitButton.setText(resources.getString("recuit-button"))
      firesButton.setText(resources.getString("fire-button"))

      recruitButton.setOnAction(_ => parent.drawRecruitPanel)
      firesButton.setOnAction(_ => parent.drawEmployeePanel(EmployeeView.fire))
    }

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      baseHR.setCenter(recruitBox(zones, contracts, shifts))

    override def drawRecruitTerminals(terminals: List[Terminale]): Unit =
      recruitView.setTerminals(terminals)

    override def drawFire(employees: List[Persona]): Unit =
      baseHR.setCenter(fireBox(employees))

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
  }
}