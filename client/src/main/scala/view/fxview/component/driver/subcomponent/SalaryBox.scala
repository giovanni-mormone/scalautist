package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.StipendioInformations
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.layout.Pane
import view.fxview.component.driver.subcomponent.parent.SalaryBoxParent
import view.fxview.component.driver.utils.StipendiCellFactory
import view.fxview.component.{AbstractComponent, Component}

trait SalaryBox extends Component[SalaryBoxParent]{
  def paneInfoSalary(information:StipendioInformations):Unit
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
    var salaryList: ListView[Stipendio] = _
    @FXML
    var salaryInfo: Pane = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      salary.foreach(stipendi => salaryList.getItems.add(stipendi))
      salaryList.setCellFactory(new StipendiCellFactory())
      salaryList.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Stipendio]()
      {
        def changed(ov:ObservableValue[_<:Stipendio], oldValue:Stipendio, newValue:Stipendio)
        {
          newValue.idStipendio.foreach(x=>parent.infoSalary(x))
          personChanged(ov, oldValue, newValue)
        }
      })
    }
    private def personChanged(ov:ObservableValue[_<:Stipendio], oldValue:Stipendio, newValue:Stipendio)
    {
      val oldText:String  = if(oldValue == null) "null" else oldValue.toString
      val newText:String  = if(newValue == null) "null" else newValue.toString

      println("Change: old = " + oldText + ", new = " + newText + "\n")
    }

    override def paneInfoSalary(information: StipendioInformations): Unit = {
          println(information)
    }
  }
}