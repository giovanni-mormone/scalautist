package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie}
import controller.HumanResourceController
import javafx.application.Platform
import javafx.stage.Stage
import view.fxview.AbstractFXDialogView
import view.fxview.component.Component
import view.fxview.component.HumanResources.HRHome
import view.fxview.component.HumanResources.subcomponent.parent._
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
import view.fxview.component.HumanResources.subcomponent.ModalAbsence
import view.fxview.component.modal.{Modal, Popup}
import view.mainview.HumanResourceView

/**
 * @author Francesco Cassano
 *
 * FX implementation of [[view.mainview.HumanResourceView]]
 *
 */
object HumanResourceViewFX {

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

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      myController = HumanResourceController()
      myController.setView(this)
      homeView()
    }

    private def homeView(): Unit = {
      hrHome = HRHome(userName,userId)
      hrHome.setParent(this)
      pane.getChildren.clear()
      pane.getChildren.add(hrHome.pane)
    }

    override def recruitClicked(persona: Assumi): Unit = {
      hrHome.startLoading()
      myController.recruit(persona)
    }

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.selectTerminals(zona)

    override def fireClicked(employees: Set[Int]): Unit = {
      hrHome.startLoading()
      myController.fires(employees)
    }

    override def saveAbsence(assenza: Assenza): Unit = {
      modalResource.startLoading()
      myController.saveAbsence(assenza)
    }

    override def openModal(item:Ferie, isMalattia: Boolean): Unit =myController.absencePerson(item,isMalattia)

    override def drawRecruitPanel: Unit = {
      hrHome.startLoading()
      myController.dataToRecruit()
    }

    override def drawEmployeePanel(viewToDraw: String): Unit = {
      hrHome.startLoading()
      myController.dataToFireAndIll(viewToDraw)
    }

    override def drawHoliday(): Unit = {
      hrHome.startLoading()
      myController.dataToHoliday()
    }

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit =
      Platform.runLater(() => hrHome.drawRecruit(zones, contracts, shifts))
    

    override def drawTerminal(terminals: List[Terminale]): Unit =
      Platform.runLater(() => hrHome.drawRecruitTerminals(terminals))

    override def drawEmployeeView(employeesList: List[Persona], viewToDraw: String): Unit = Platform.runLater(() =>{
      viewToDraw match {
        case EmployeeView.fire =>hrHome.drawFire(employeesList)
        case EmployeeView.ill => hrHome.drawIllBox(employeesList)
      }
    })

    override def drawHolidayView(employeesList: List[Ferie]): Unit =
      Platform.runLater(() => hrHome.drawHolidayBox(employeesList))

    override def drawChangePassword: Unit =
      Platform.runLater(() => ChangePasswordViewFX(stage, Some(stage.getScene)))

    override def drawModalAbsenceHoliday(item:Ferie,isMalattia:Boolean,assenza: List[Assenza]): Unit =
      Platform.runLater(() => {
        modalResource = Modal[ModalAbsenceParent, Component[ModalAbsenceParent], HRModalBoxParent](myStage, this, ModalAbsence(item, isMalattia,assenza))
        modalResource.show()
      })

    override def resultAbsence(keyMessage: String, isMalattia: Boolean): Unit = {
      this.showMessageFromKey(keyMessage)
      Platform.runLater(()=>{
        modalResource.close()
        if(isMalattia) drawEmployeePanel(EmployeeView.ill)
        else drawHoliday()
      })
    }
    override def result(message: String): Unit = {
      this.showMessageFromKey(message)
      Platform.runLater(()=>{modalResource.close()})
    }

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