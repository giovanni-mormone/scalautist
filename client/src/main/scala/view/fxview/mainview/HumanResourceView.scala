package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{Assenza, Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import controller.HumanResourceController
import javafx.application.Platform
import javafx.scene.layout.Pane
import javafx.stage.Stage
import view.{BaseView, DialogView}
import view.fxview.{AbstractFXDialogView, FXHelperFactory}
import view.fxview.component.HumanResources.subcomponent.IllBoxParent
import view.fxview.component.HumanResources.{HRHome, HRViewParent}

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
  def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno])

  /**
   * Show terminals into child's recruit view
   *
   */
  def drawTerminal(terminals: List[Terminale])
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
      FXHelperFactory.modalWithMessage(myStage,"Strunz").show()
    }

    override def recruitClicked(persona: Assumi): Unit = myController.recruit(persona)

    override def loadRecruitTerminals(zona: Zona): Unit =
      myController.getTerminals(zona)

    override def drawRecruitPanel: Unit =
      myController.getRecruitData

    override def drawRecruit(zones: List[Zona], contracts: List[Contratto], shifts: List[Turno]): Unit = {
      hrHome.drawRecruit(zones, contracts, shifts)
    }

    override def drawTerminal(terminals: List[Terminale]): Unit =
      hrHome.drawRecruitTerminals(terminals)

    override def saveAbsence(absence: Assenza): Unit = myController.saveAbsence(absence)
  }
}
/*override def drawRecruit(): Unit = {
      Platform.runLater(() =>{
        /*val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
        val contratti = List(Contratto("Full-Time-5x2", true,Some(1)), Contratto("Part-Time-5x2", true,Some(2)),
          Contratto("Part-Time-6x1", false,Some(3)), Contratto("Full-Time-6x1", true,Some(4)))
        val turni = List(Turno("mattina","04-08",Some(1)), Turno("mattina2","08-14",Some(2)),
          Turno("pomer","14-19",Some(3)), Turno("sera","19-23",Some(4)), Turno("notte","23-04",Some(5)))*/

      })
    }*/