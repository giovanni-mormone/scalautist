package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import controller.HumanResourceController
import javafx.application.Platform
import javafx.stage.Stage
import view.DialogView
import view.fxview.{AbstractFXDialogView, Popup}
import view.fxview.component.Component
import view.fxview.component.HumanResources.HRHome
import view.fxview.component.HumanResources.subcomponent.parent._
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
import view.fxview.component.HumanResources.subcomponent.ModalAbsence
import view.fxview.component.modal.Modal

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
   * @param zones
   *              list of [[caseclass.CaseClassDB.Zona]] to show
   * @param contracts
   *                  list of [[caseclass.CaseClassDB.Contratto]] to show
   * @param shifts
   *               list of [[caseclass.CaseClassDB.Turno]] to show
   */
  def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit

  /**
   * Show terminals into child's recruit view
   *
   * @param terminals
   *                  list of [[caseclass.CaseClassDB.Terminale]] to show
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
   * show a list of employee to assign holiday
   *
   * @param employeesList
   *                      list of [[caseclass.CaseClassHttpMessage.Ferie]] to show
   */
  def drawHolidayView(employeesList: List[Ferie]):Unit

  /**
   * show a message in the modal like a pop-up
   *
   * @param message
   *                String of message to show
   */
  def result(message:String):Unit

  /**
   * show a message in the view
   *
   * @param message
   *                String of message to show
   */
  def message(message: String): Unit

  /**  
   * method that send list of assenza for a person within a year
   * @param item represent ferie of a person, this case class contains day that remain to the person and your name and surname and idPerson
   * @param isMalattia boolean that represent if modal is open for illness or holiday
   * @param assenza list of assenza for one person
   */
  def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit
  /**
   * show a message in the main view
   *
   * @param message
   *                String of message to show
   */
  def dialog(message: String): Unit

  /**
   * show a specific message in the main view
   *
   * @param className
   *                  String of the entity name
   * @param message
   *                String of type of message to print
   */
  def dialog(className: String, message: String): Unit
}

/**
 * @author Francesco Cassano
 *
 * Companion object of [[view.fxview.mainview.HumanResourceView]]
 *
 */
object HumanResourceView {

  def apply(stage: Stage,userName: String, userId:String): HumanResourceView = new HumanResourceHomeFX(stage,userName, userId)

  /**
   * HumanResourceView FX implementation
   *
   * @param stage
   *              Stage that load view
   */
  private class HumanResourceHomeFX(stage: Stage,userName: String, userId:String) extends AbstractFXDialogView(stage)
    with HumanResourceView with HRHomeParent with HRModalBoxParent {

    private var myController: HumanResourceController = _
    private var modalResource: Modal = _
    private var hrHome: HRHome = _
    private var popup: Popup = _

    /**
     * Closes the view.
     */
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = HumanResourceController()
      myController.setView(this)
      homeView()
    }

    ///////////////////////////////////////////////////////////////// Da VIEW A CONTROLLER impl HRViewParent

    private def homeView(): Unit = {
      hrHome = HRHome(userName,userId)
      hrHome.setParent(this)
      pane.getChildren.clear()
      pane.getChildren.add(hrHome.pane)
    }

    /////////////////////////////////////////////////////////   assumi
    override def recruitClicked(persona: Assumi): Unit =
      myController.recruit(persona)

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.getTerminals(zona)

    /////////////////////////////////////////////////////////   licenzia
    override def fireClicked(employees: Set[Int]): Unit =
      myController.fires(employees)


    /////////////////////////////////////////////////////////   assenza
    override def saveAbsence(assenza: Assenza): Unit =
      myController.saveAbsence(assenza)

    /////////////////////////////////////////////////////////ModalAbsence
    override def openModal(item:Ferie, isMalattia: Boolean): Unit =myController.absencePerson(item,isMalattia)


    /////////////////////////////////////////////////////////   disegni pannelli
    override def drawRecruitPanel: Unit =
      myController.dataToRecruit()
 
    override def drawEmployeePanel(viewToDraw: String): Unit = {
      hrHome.startLoading()
      myController.dataToFireAndIll(viewToDraw)
    }

    override def drawHoliday(): Unit =
      myController.dataToHoliday()


    ///////////////////////////////////////////////////////////////// Da CONTROLLER A VIEW impl HumanResourceView

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      Platform.runLater(() => hrHome.drawRecruit(zones, contracts, shifts))
    

    override def drawTerminal(terminals: List[Terminale]): Unit =
      Platform.runLater(() => hrHome.drawRecruitTerminals(terminals))

 
    override def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit = Platform.runLater(() =>{
      hrHome.endLoading()
      viewToDraw match {
        case EmployeeView.fire =>hrHome.drawFire(employeesList)
        case EmployeeView.ill => hrHome.drawIllBox(employeesList)
      }
    })

    override def drawHolidayView(employeesList: List[Ferie]): Unit =
      Platform.runLater(() => hrHome.drawHolidayBox(employeesList))

    override def drawChangePassword: Unit =
      Platform.runLater(() => ChangePasswordView(stage, Some(stage.getScene)))

    override def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit =
      Platform.runLater(() => {
        modalResource = Modal[ModalAbsenceParent, Component[ModalAbsenceParent], HRModalBoxParent](myStage, this, ModalAbsence(item, isMalattia,assenza))
        modalResource.show()
      })


    override def message(message: String): Unit =
      Platform.runLater(()=> this.showMessage(message))

    override def result(message: String): Unit =
      Platform.runLater(() => this.showMessageFromKey(message))

    override def dialog(message: String): Unit = {
      Platform.runLater(() => {
        homeView()
        popup = new Popup(myStage)
        popup.showMessage(message)
      })
    }

    override def dialog(className: String, message: String): Unit =
      Platform.runLater(() => {
        homeView()
        popup = new Popup(myStage)
        popup.showMessage(generalResources.getString(className + "-" + message))
      })
      
    override def errorMessage(message: String): Unit = showMessage(message)
  }
}