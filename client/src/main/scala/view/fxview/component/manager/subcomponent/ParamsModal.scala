package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView}
import view.fxview.component.AbstractComponent
import view.fxview.component.manager.subcomponent.parent.ModalParamParent

trait ParamsModal extends AbstractComponent[ModalParamParent] {

}

object ParamsModal {

  def apply(oldsParam: List[InfoAlgorithm]): ParamsModal = new ParamsModalFX(oldsParam)

  private class ParamsModalFX(value: List[InfoAlgorithm])
    extends AbstractComponent[ModalParamParent]("manager/subcomponent/ParamsModal")
    with ParamsModal {

    @FXML
    var params: TableView[_] = _
    @FXML
    var open: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)

    }

  }
}
