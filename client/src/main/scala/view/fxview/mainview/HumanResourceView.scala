package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import controller.HumanResourceController
import javafx.application.Platform
import javafx.scene.layout.Pane
import javafx.stage.Stage
import view.BaseView
import view.fxview.AbstractFXDialogView
import view.fxview.component.HumanResources.subcomponent.EmployeeView
import view.fxview.component.HumanResources.{HRHome, HRViewParent}

/**
 * @author Francesco Cassano
 *
 * A view to manage human resource Views functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait HumanResourceView extends BaseView {

  /**
   * Show child's recruit view
   *
   */
  def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit

  /**
   * Show terminals into child's recruit view
   *
   */
  def drawTerminal(terminals: List[Terminale]): Unit

  /**
   * Show the view that requested the list of employees
   *
   */
  def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit
}

/**
 * @author Francesco Cassano
 *
 * Companion object of [[view.fxview.mainview.HumanResourceView]]
 *
 */
object HumanResourceView {

  def apply(stage: Stage): HumanResourceView = new HumanResourceViewFX(stage)

  /**
   * HumanResourceView FX implementation
   *
   * @param stage
   *              Stage that load view
   */
  private class HumanResourceViewFX(stage: Stage) extends AbstractFXDialogView(stage)
    with HumanResourceView with HRViewParent {

    private var myController: HumanResourceController = _
    private var hrHome: HRHome = _

    /**
     * Closes the view.
     */
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = HumanResourceController()
      myController.setView(this)
      hrHome = HRHome()
      hrHome.setParent(this)
      pane.getChildren.add(hrHome.pane)
    }

    ///////////////////////////////////////////////////////////////// Da VIEW A CONTROLLER impl HRViewParent

    override def recruitClicked(persona: Assumi): Unit =
      myController.recruit(persona)

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.getTerminals(zona)

    override def drawRecruitPanel: Unit =
      myController.getRecruitData

    override def drawEmployeePanel(viewToDraw: String): Unit =
      myController.getAllPersona(viewToDraw)

    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl HumanResourceView

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      hrHome.drawRecruit(zones, contracts, shifts)

    override def drawTerminal(terminals: List[Terminale]): Unit =
      hrHome.drawRecruitTerminals(terminals)

    override def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit = viewToDraw match {
      case EmployeeView.fire => hrHome.drawFire(employeesList)
    }

  }
}