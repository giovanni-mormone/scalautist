package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import controller.HumanResourceController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.component.HumanResources.subcomponent.parent.{HRHomeParent, ModalTrait}
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
import view.fxview.component.HumanResources.{HRHome, MainModalResource}
import view.fxview.{AbstractFXDialogView, FXHelperFactory}
 

/**
 * @author Francesco Cassano
 *
 * A view to manage human resource Views functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait HumanResourceView extends DialogView {

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
   * @param employeesList
   *                      List of [[caseclass.CaseClassDB.Persona]] represent employees
   * @param viewToDraw
   *                   The string represent the view code requesting the data
   */
  def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit

  /**
   *
   * @param employeesList
   */
  def drawHolidayView(employeesList: List[Ferie]):Unit
  /**
   * Show the zone view
   *
   * @param zones
   *              List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZonaView(zones: List[Zona]): Unit

  /**
   * show terminal view
   *
   * @param zones
   *              Listo of [[caseclass.CaseClassDB.Zona]]
   * @param terminals
   *                   Listo of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit

  /**
   *
   * @param message
   */
  def result(message:String):Unit
}

/**
 * @author Francesco Cassano
 *
 * Companion object of [[view.fxview.mainview.HumanResourceView]]
 *
 */
object HumanResourceView {

  def apply(stage: Stage): HumanResourceView = new HumanResourceHomeFX(stage)

  /**
   * HumanResourceView FX implementation
   *
   * @param stage
   *              Stage that load view
   */
  private class HumanResourceHomeFX(stage: Stage) extends AbstractFXDialogView(stage)
    with HumanResourceView with HRHomeParent with ModalTrait{

    private var myController: HumanResourceController = _
    private var modalResource: MainModalResource = _
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
      FXHelperFactory.modalWithMessage(myStage,"Strunz").show()
    }

    ///////////////////////////////////////////////////////////////// Da VIEW A CONTROLLER impl HRViewParent

    override def recruitClicked(persona: Assumi): Unit =
      myController.recruit(persona)

    override def fireClicked(employees: Set[Int]): Unit =
      myController.fires(employees)

    override def newZona(zona: Zona): Unit =
      myController.saveZona(zona)

    override def saveAbscense(assenza: Assenza): Unit =
      myController.saveAbsence(assenza)

    override def newTerminale(terminal: Terminale): Unit =
      myController.saveTerminal(terminal)

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.getTerminals(zona)

    override def drawRecruitPanel: Unit =
      myController.getRecruitData
 
    override def drawEmployeePanel(viewToDraw: String): Unit =
      myController.getAllPersona(viewToDraw)

    override def drawZonePanel: Unit =
      myController.getZonaData()

<<<<<<< HEAD
    override def drawTerminalPanel: Unit =
      myController.getTerminalData()

=======
    override def drawHoliday(): Unit =  myController.getAllPersona()
>>>>>>> e5e9be816b0ca2982f1d8c22b58c11b5157d9eca
    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl HumanResourceView

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
     hrHome.drawRecruit(zones, contracts, shifts)
    

    override def drawTerminal(terminals: List[Terminale]): Unit =
      hrHome.drawRecruitTerminals(terminals)

 
    override def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit = viewToDraw match {
      case EmployeeView.fire =>Platform.runLater(()=>hrHome.drawFire(employeesList))
      case EmployeeView.ill => Platform.runLater(()=>hrHome.drawIllBox(employeesList))
    }

    override def drawZonaView(zones: List[Zona]): Unit =
      hrHome.drawZona(zones)

    override def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit =
      hrHome.drawTerminal(zones, terminals)

    override def drawChangePassword: Unit =
      ChangePasswordView(stage, Some(stage.getScene))

<<<<<<< HEAD
    override def result(message: String): Unit =
      modalResource.showMessage(message)
=======
    override def saveAbscense(assenza: Assenza): Unit = myController.saveAbsence(assenza)

    override def result(message: String): Unit = Platform.runLater(()=>modalResource.showMessage(message))

    override def openModal(id: Int, name: String, surname: String, isMalattia: Boolean): Unit = {
      modalResource = MainModalResource(id,name,surname,myStage,this,isMalattia)
      modalResource.show()
    }

    override def drawHolidayView(employeesList: List[Ferie]): Unit = hrHome.drawHolidayBox(employeesList)
>>>>>>> e5e9be816b0ca2982f1d8c22b58c11b5157d9eca
  }
}