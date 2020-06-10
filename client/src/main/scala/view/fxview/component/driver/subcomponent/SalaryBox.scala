package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Stipendio
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.Pane
import view.fxview.component.driver.subcomponent.parent.SalaryBoxParent
import view.fxview.component.{AbstractComponent, Component}

trait SalaryBox extends Component[SalaryBoxParent]{

}
object SalaryBox{
  def apply(salary: List[Stipendio]): SalaryBox = new SalaryBoxFX(salary)

  /**
   * SalaryBox Fx implementation. It shows Salary for one person, all salary for one person
   *
   * @param salary
   *                  list of salary in db
   */
  private class SalaryBoxFX(salary: List[Stipendio])
    extends AbstractComponent[SalaryBoxParent]("driver/subcomponent/SalaryBox") with SalaryBox {

    @FXML
    var salaryList: ListView[String] = _
    @FXML
    var salaryInfo: Pane = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

    }
  }
}