package view.fxview.component.HumanResources

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane
import view.fxview.component.HumanResources.subcomponent.RecruitBox
import view.fxview.component.{AbstractComponent, Component}

trait HRViewParent {

  def recruitClicked(persona: Persona): Unit

  def loadTerminals(zona: Zona): Unit

  def drawRecruitPanel: Unit

}

trait HRHome extends Component[HRViewParent]{
  def drawRecruit(zone: List[Zona], contratti: List[Contratto], turni: List[Turno])
  def drawRecruitTerminals(terminali: List[Terminale])
}

object HRHome{

  def apply(): HRHome = new HomeFX()

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