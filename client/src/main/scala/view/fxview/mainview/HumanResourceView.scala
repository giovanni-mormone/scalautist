package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import controller.HumanResourceController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.HumanResources.HRHome
import view.fxview.component.HumanResources.subcomponent.parent._
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
import view.fxview.component.HumanResources.subcomponent.{ModalAbsence, ModalTerminal, ModalZone}
import view.fxview.component.modal.{Modal, ModalParent}
 

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
   * @param zoneList
   * @param terminal
   */
  def openTerminalModal(zoneList: List[Zona], terminal: Terminale): Unit

  /**
   *
   * @param message
   */
  def result(message:String):Unit

  /**
   *
   * @param message
   */
  def message(message: String): Unit

  /**
   * method that send list of assenza for a person within a year
   * @param item represent ferie of a person, this case class contains day that remain to the person and your name and surname and idPerson
   * @param isMalattia boolean that represent if modal is open for illness or holiday
   * @param assenza list of assenza for one person
   */
  def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit
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
    with HumanResourceView with HRHomeParent with HRModalBoxParent {

    private var myController: HumanResourceController = _
    private var modalResource: Modal = _
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

    /////////////////////////////////////////////////////////   assumi
    override def recruitClicked(persona: Assumi): Unit =
      myController.recruit(persona)

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.getTerminals(zona)

    /////////////////////////////////////////////////////////   licenzia
    override def fireClicked(employees: Set[Int]): Unit =
      myController.fires(employees)

    /////////////////////////////////////////////////////////   zona
    override def newZona(zona: Zona): Unit =
      myController.saveZona(zona)

    override def deleteZona(zona: Zona): Unit = {
      println(zona + "-> cancella" )
      modalResource.showMessage("cancellato")
      //myController.delete
    }

    override def updateZona(zona: Zona): Unit = {
      println(zona + "-> modifica" )
      modalResource.showMessage("modificato")
    }

    //myController.update

    /////////////////////////////////////////////////////////   assenza
    override def saveAbsence(assenza: Assenza): Unit =
      myController.saveAbsence(assenza)

    /////////////////////////////////////////////////////////   terminale
    override def newTerminale(terminal: Terminale): Unit =
      myController.saveTerminal(terminal)

    override def deleteTerminal(terminal: Terminale): Unit = {
      myController.deleteTerminal(terminal)
      modalResource.showMessage("cancellato")
    }

    override def updateTerminal(terminal: Terminale): Unit = {
      myController.updateTerminal(terminal)
      modalResource.showMessage("modificato")
    }
    /////////////////////////////////////////////////////////ModalAbsence
    override def openModal(item:Ferie, isMalattia: Boolean): Unit =myController.absencePerson(item,isMalattia)

    /////////////////////////////////////////////////////////   disegni pannelli
    override def drawRecruitPanel: Unit =
      myController.getRecruitData()
 
    override def drawEmployeePanel(viewToDraw: String): Unit =
      myController.getAllPersona(viewToDraw)

    override def drawZonePanel: Unit =
      myController.getZonaData()

    override def drawTerminalPanel: Unit =
      myController.getTerminalData()

    override def drawHoliday(): Unit =
      myController.getAllPersona()

    override def openTerminalModal(terminal: Int): Unit =
      myController.terminalModalData(terminal)

    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl HumanResourceView

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      Platform.runLater(() => hrHome.drawRecruit(zones, contracts, shifts))
    

    override def drawTerminal(terminals: List[Terminale]): Unit =
      Platform.runLater(() => hrHome.drawRecruitTerminals(terminals))

 
    override def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit = viewToDraw match {
      case EmployeeView.fire =>Platform.runLater(() => hrHome.drawFire(employeesList))
      case EmployeeView.ill => Platform.runLater(() => hrHome.drawIllBox(employeesList))
    }

    override def drawZonaView(zones: List[Zona]): Unit =
      Platform.runLater(() => hrHome.drawZona(zones))

    override def drawTerminaleView(zones: List[Zona], terminals: List[Terminale]): Unit =
      Platform.runLater(() => hrHome.drawTerminal(zones, terminals))

    override def drawHolidayView(employeesList: List[Ferie]): Unit =
      Platform.runLater(() => hrHome.drawHolidayBox(employeesList))

    override def drawChangePassword: Unit =
      Platform.runLater(() => ChangePasswordView(stage, Some(stage.getScene)))

    override def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit =
      Platform.runLater(() => {
        modalResource = Modal[ModalAbsenceParent, Component[ModalAbsenceParent], HRModalBoxParent](myStage, this, ModalAbsence(item, isMalattia,assenza))
        modalResource.show()
      })
    /////////////////////////////////////////////////////////   disegni modal



    override def openZonaModal(zona: Zona): Unit =
      Platform.runLater(() => {
        modalResource = Modal[ModalZoneParent, Component[ModalZoneParent], HRModalBoxParent](myStage, this, ModalZone(zona))
        modalResource.show()
      })


    def openTerminalModal(zoneList: List[Zona], terminal: Terminale): Unit =
      Platform.runLater(() => {
        modalResource = Modal[ModalTerminalParent, Component[ModalTerminalParent], HRModalBoxParent](myStage, this, ModalTerminal(zoneList, terminal))
        modalResource.show()
      })



    ////////////////////////////////////////////////////////////// esito modal

    override def message(message: String): Unit =
      Platform.runLater(()=> this.showMessage(message))

    override def result(message: String): Unit =
      Platform.runLater(() => modalResource.showMessage(message))

  }
}