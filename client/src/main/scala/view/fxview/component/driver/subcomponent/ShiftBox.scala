package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoShift
import javafx.fxml.FXML
import javafx.scene.control.{Accordion, Label}
import view.fxview.component.driver.subcomponent.parent.ShiftBoxParent
import view.fxview.component.driver.subcomponent.util.Days
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait ShiftBox extends Component[ShiftBoxParent]{

}
object ShiftBox{
  def apply(shift: InfoShift): ShiftBox = new ShiftBoxFX(shift)

  /**
   * ShiftBox Fx implementation. It shows Salary for one person, all salary for one person
   *
   * @param shift
   *                  list of shift in db
   */
  private class ShiftBoxFX(shift: InfoShift)
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