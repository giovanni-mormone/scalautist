package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle
import view.fxview.component.driver.subcomponent.util.Days._
import caseclass.CaseClassDB.Turno
import javafx.fxml.FXML
import javafx.scene.control.{Accordion, Label}
import view.fxview.component.driver.subcomponent.parent.ShiftBoxParent
import view.fxview.component.driver.subcomponent.util.Days
import view.fxview.component.{AbstractComponent, Component}

trait ShiftBox extends Component[ShiftBoxParent]{

}
object ShiftBox{
  def apply(shift: List[Turno]): ShiftBox = new ShiftBoxFX(shift)

  /**
   * ShiftBox Fx implementation. It shows Salary for one person, all salary for one person
   *
   * @param shift
   *                  list of shift in db
   */
  private class ShiftBoxFX(shift: List[Turno])
    extends AbstractComponent[ShiftBoxParent]("driver/subcomponent/ShiftBox") with ShiftBox {

    @FXML
    var shiftAccordion: Accordion  = _
    @FXML
    var title:Label =_
    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      title.setText(resources.getResource("title"))
      Days.createAccordion(resources,shiftAccordion,shift)
    }
  }
}