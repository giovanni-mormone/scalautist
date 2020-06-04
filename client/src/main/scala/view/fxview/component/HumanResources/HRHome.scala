package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Contratto, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane
import view.fxview.component.HumanResources.subcomponent.{IllBoxParent, RecruitBox}
import view.fxview.component.{AbstractComponent, Component}

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by views to make requests to controller
 *
 */
trait HRViewParent  extends IllBoxParent {

  /**
   * If recruit button is clicked the controller is asked to save the instance of persona
   *
   * @param persona
   *                instance of assumi. It's the employee to save
   */
  def recruitClicked(persona: Assumi): Unit

  /**
   * If the Zona was choosen the controller is asked the list of terminale
   *
   * @param zona
   *             instance of terminale's Zona to return
   */
  def loadRecruitTerminals(zona: Zona): Unit

  /**
   * It notify parent that recruitView must be shown
   */
  def drawRecruitPanel: Unit

}

/**
 * @author Francesco Cassano
 *
 * Interface allows to communicate with the internal view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.HRViewParent]]
 */
trait HRHome extends Component[HRViewParent]{

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
  def drawRecruitTerminals(terminali: List[Terminale])
}

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
  private class HomeFX() extends AbstractComponent[HRViewParent] ("humanresources/BaseHumanResource") with HRHome {

    @FXML
    var baseHR: BorderPane = _
    @FXML
    var recruitButton: Button = _
    @FXML
    var nameLabel: Label = _

    var recruitView: RecruitBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText("Buongiorno Stronzo")
      recruitButton.setText(resources.getString("recuit-button"))
      recruitButton.setOnAction(_ => parent.drawRecruitPanel)
    }

    private def recruitBox(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]) = {
      recruitView = RecruitBox(contracts, shifts, zones)
      recruitView.setParent(parent)
      recruitView.pane
    }

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit = {
      baseHR.setCenter(recruitBox(zones, contracts, shifts))
    }

    override def drawRecruitTerminals(terminali: List[Terminale]): Unit =
      recruitView.setTerminals(terminali)
  }
}