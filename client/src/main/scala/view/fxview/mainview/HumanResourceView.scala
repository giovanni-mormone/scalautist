package view.fxview.mainview

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import controller.HumanResourceController
import javafx.application.Platform
import javafx.scene.layout.Pane
import javafx.stage.Stage
import view.BaseView
import view.fxview.AbstractFXDialogView
import view.fxview.component.HumanResources.{HRHome, HRViewParent}

/**
 * @author Francesco Cassano
 *
 * A view to manage human resource Views functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait HumanResourceView extends BaseView {
  def drawRecruit()
}


object HumanResourceView {

  def apply(stage: Stage): HumanResourceView = new HumanResourceViewFX(stage)

  private class HumanResourceViewFX(stage: Stage) extends AbstractFXDialogView(stage) with HumanResourceView with HRViewParent{

    private var myController: HumanResourceController = _
    private var hrHome: HRHome = _

    /**
     * Closes the view.
     */
    override def close(): Unit = stage.close()

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
      myController = HumanResourceController()
      myController.setView(this)
      hrHome = HRHome()
      hrHome.setParent(this)
      pane.getChildren.add(hrHome.pane)
    }

    override def recruitClicked(persona: Persona): Unit = myController.recruit(persona)

    override def loadTerminals(zona: CaseClassDB.Zona): Unit = {  //TODO fare tutto in chiamate al controller
      val terminale = List(Terminale("termos", 3, Some(18)), Terminale("mentos", 10, Some(1)),
        Terminale("somret", 3, Some(11)), Terminale("zozza", 3, Some(99)))
      hrHome.drawRecruitTerminals(terminale.filter(t => t.idZona == zona.idZone.head))
    }

    override def drawRecruit(): Unit = {  //TODO fare tutto in chiamate al controller
      Platform.runLater(() =>{
        val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
        val contratti = List(Contratto("Full-Time-5x2",Byte.MaxValue), Contratto("Part-Time-5x2",Byte.MaxValue),
          Contratto("Part-Time-6x1",Byte.MinValue), Contratto("Full-Time-6x1",Byte.MinValue))
        val turni = List(Turno("mattina","04-08"), Turno("mattina2","08-14"),
          Turno("pomer","14-19"), Turno("sera","19-23"), Turno("notte","23-04"))
        hrHome.drawRecruit(zone, contratti, turni)
      })
    }

    override def drawRecruitPanel: Unit = drawRecruit
  }
}
