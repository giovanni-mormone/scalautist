package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

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
          Days.createAccordion(resources,shiftAccordion,List(Turno("Manana","8:00-12:00",40,Some(1)),
            Turno("Manana","12:00-16:00",40,Some(1)),
            Turno("Manana","8:00-12:00",40,Some(1)),
            Turno("Manana","12:00-16:00",40,Some(1)),
            Turno("Manana","8:00-12:00",40,Some(1)),
            Turno("Manana","12:00-16:00",40,Some(1)),
            Turno("Manana","8:00-12:00",40,Some(1)),
            Turno("Manana","12:00-16:00",40,Some(1)),
            Turno("Manana","8:00-12:00",40,Some(1)),
            Turno("Manana","12:00-16:00",40,Some(1))))
    }
  }
}