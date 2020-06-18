package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Persona
import javafx.fxml.FXML
import javafx.scene.control.{TableView, TextField}
import view.fxview.component.manager.subcomponent.parent.FillHolesParent
import view.fxview.component.{AbstractComponent, Component}

trait FillHolesBox extends Component[FillHolesParent]{

}

object FillHolesBox{

  def apply(): FillHolesBox = new FillHolesFX()

  private class FillHolesFX extends AbstractComponent[FillHolesParent]("manager/subcomponent/FillHolesBox")
    with FillHolesBox{

    @FXML
    var searchField: TextField = _
    @FXML
    var terminalsAndTurnsTable: TableView[Persona] = _
    @FXML
    var substitutesTable: TableView[Persona] = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

    }
  }
}
