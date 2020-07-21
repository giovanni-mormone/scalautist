package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Terminale
import caseclass.CaseClassHttpMessage.InfoAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView}
import view.fxview.component.AbstractComponent
import view.fxview.component.manager.subcomponent.parent.ModalParamParent
import view.fxview.component.manager.subcomponent.util.ParamsTable
import view.fxview.util.ResourceBundleUtil._

trait ParamsModal extends AbstractComponent[ModalParamParent] {

}

object ParamsModal {

  def apply(oldsParam: List[InfoAlgorithm], terminals: List[Terminale]): ParamsModal = new ParamsModalFX(oldsParam, terminals)

  private class ParamsModalFX(paramsList: List[InfoAlgorithm], temrinals: List[Terminale])
    extends AbstractComponent[ModalParamParent]("manager/subcomponent/ParamsModal")
    with ParamsModal {

    @FXML
    var params: TableView[ParamsTable] = _
    @FXML
    var open: Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initButton()
    }

    private def initButton(): Unit = {
      open.setText(resources.getResource(key = "buttontxt"))
      open.setOnAction(_ => parent.loadParam(paramsList.head))
    }

  }
}
