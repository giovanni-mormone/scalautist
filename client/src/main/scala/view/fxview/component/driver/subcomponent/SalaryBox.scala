package view.fxview.component.driver.subcomponent

import java.net.URL
import java.util.ResourceBundle
import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoPresenza, StipendioInformations}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Label, ListView}
import javafx.scene.layout.{HBox, Pane}
import view.fxview.FXHelperFactory
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
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
    var salaryInfo: HBox = _
    @FXML
    var tittle:Label=_
    @FXML
    var day:Pane=_
    @FXML
    var dayM:Label=_
    @FXML
    var normalDay:Label=_
    @FXML
    var shift:Pane=_
    @FXML
    var shiftValue:Label=_
    @FXML
    var extraValue:Label=_
    @FXML
    var absence:Pane=_
    @FXML
    var Illness:Label=_
    @FXML
    var holiday:Label=_
    private var datepicker:Node=_
    private var resources:ResourceBundle=_

    override def initialize(location: URL, resources: ResourceBundle): Unit = {

      this.resources=resources
      salary.foreach(stipendi => salaryList.getItems.add(stipendi))
      salaryList.setCellFactory(StipendiCellFactory)
      listenerListView()

    }
    private def listenerListView():Unit={

      salaryList.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Stipendio]()
      {
        def changed(ov:ObservableValue[_<:Stipendio], oldValue:Stipendio, newValue:Stipendio)
        {
          newValue.idStipendio.foreach(x=>{
            salaryInfo.getChildren.remove(datepicker)
            salaryInfo.getChildren.add(FXHelperFactory.loadingBox)
            parent.infoSalary(x)
          })
        }
      })

    }
    override def paneInfoSalary(information: StipendioInformations): Unit = {
      salaryInfo.getChildren.remove(FXHelperFactory.loadingBox)
      datepicker=createDatePicker(information)
      salaryInfo.getChildren.add(datepicker)
      generalInfo(information)
    }
    private def createDatePicker(information: StipendioInformations):Node={

      val myCalendar = information.turni.map(value=>Option(value,CreateDatePicker.sqlDateToCalendar(value.data)))
      val (datePickerSkin,finishDate) = myCalendar.head.map(day=>CreateDatePicker.createDatePickerSkin(day._2)).head
      finishDate.setDayCellFactory(_=>CreateDatePicker.drawDatePicker(myCalendar))
      datePickerSkin.getPopupContent
    }

    private def generalInfo(informations: StipendioInformations): Unit ={

      tittle.setText(resources.getResource("general-info"))
      val (extra,normal) = totalExtraAndNormal(informations.turni)
      dayM.setText(resources.println("extra-day",extra))
      normalDay.setText(resources.println("normal-day",normal))
      infoShiftAndExtra(extra,normal,informations)
      infoAssenza(informations)
    }

    private def infoShiftAndExtra(normal:Int,extra:Int,informations: StipendioInformations):Unit={

      shiftValue.setText(resources.println("shift-value",informations.infoValore.valoreTotaleTurni/normal))
      extraValue.setText(resources.println("extra-value",informations.infoValore.valoreTotaleTurni/extra))
      shiftValue.setText(resources.println("shift-total-value",informations.infoValore.valoreTotaleTurni))
      extraValue.setText(resources.println("extra-total-value",informations.infoValore.valoreTotaleStraordinari))
      extraValue.setText(resources.println("total",informations.infoValore.valoreTotaleStraordinari+informations.infoValore.valoreTotaleTurni))

    }
    private def infoAssenza(informations: StipendioInformations):Unit={

      Illness.setText(resources.println("illness-day",informations.infoAssenza.assenzePerMalattia))
      holiday.setText(resources.println("holiday",informations.infoAssenza.assenzePerFerie))

    }
    private def totalExtraAndNormal(turni:List[InfoPresenza]):(Int,Int)= {

      val isStraordinario=true
      val map = turni.groupBy(_.straordinario).map(key=>key._1->key._2.length)
      (map.getOrElse(!isStraordinario,0),map.getOrElse(isStraordinario,0))

    }

  }
}