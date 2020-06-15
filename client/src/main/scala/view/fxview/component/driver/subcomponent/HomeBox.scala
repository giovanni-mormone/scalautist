package view.fxview.component.driver.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import com.sun.javafx.scene.control.skin.{DatePickerContent, DatePickerSkin}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.DatePicker
import javafx.scene.layout.{AnchorPane, VBox}
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
import view.fxview.component.driver.subcomponent.parent.HomeBoxParent
import view.fxview.component.{AbstractComponent, Component}

trait HomeBox extends Component[HomeBoxParent]{

}
object HomeBox{
  def apply(shift: List[Turno]): HomeBox = new HomeBoxFX(shift)

  /**
   * HomeBox Fx implementation. It shows shift for one person and general info
   *
   * @param shift
   *                  list of shift in db
   */
  private class HomeBoxFX(shift: List[Turno])
    extends AbstractComponent[HomeBoxParent]("driver/subcomponent/HomeBox") with HomeBox {

    @FXML
    var info: VBox = _
    @FXML
    var datepicker: AnchorPane = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      val date_picker:DatePicker = new DatePicker(LocalDate.now())
      val datePickerSkin:DatePickerSkin = CreateDatePicker.createDatePickerSkin(date_picker)
      val node:Node = datePickerSkin.getPopupContent
      datepicker.getChildren.add(node)

    }
  }
}