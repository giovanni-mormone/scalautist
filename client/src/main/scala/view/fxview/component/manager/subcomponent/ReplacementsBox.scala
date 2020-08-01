package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoReplacement
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.FillHolesParent

trait ReplacementsBox extends Component[FillHolesParent]


object ReplacementsBox{

  def apply(dataToShow: InfoReplacement): ReplacementsBox = new ReplacementsBoxFX(dataToShow)

  private class ReplacementsBoxFX(replacement: InfoReplacement) extends AbstractComponent[FillHolesParent]("manager/subcomponent/TwoFieldBox")
    with ReplacementsBox{

    @FXML
    var field1: Label = _
    @FXML
    var field2: Label = _
    @FXML
    var baseBox: HBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      field1.setText(replacement.nome)
      field2.setText(replacement.cognome)
      baseBox.setOnMouseClicked(e => if(e.getClickCount == 2) parent.replacementSelected(replacement.idRisultato, replacement.idPersona))
    }
  }
}
