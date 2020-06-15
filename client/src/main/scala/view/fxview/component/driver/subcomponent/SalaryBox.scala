package view.fxview.component.driver.subcomponent

import java.net.URL
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.{LocalDate, MonthDay}
import java.util.ResourceBundle

import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoAssenza, InfoPresenza, InfoValorePresenza, StipendioInformations}
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{DateCell, DatePicker, Label, ListView, Tooltip}
import javafx.scene.layout.{HBox, Pane}
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker
import view.fxview.component.HumanResources.subcomponent.util.CreateDatePicker.{MoveDatePeriod, sqlDateToCalendar}
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
    var resources:ResourceBundle=_
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      this.resources=resources
      salary.foreach(stipendi => salaryList.getItems.add(stipendi))

      salaryList.setCellFactory(StipendiCellFactory)
      salaryList.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Stipendio]()
      {
        def changed(ov:ObservableValue[_<:Stipendio], oldValue:Stipendio, newValue:Stipendio)
        {
          newValue.idStipendio.foreach(x=>parent.infoSalary(x))
        }
      })
    }
    override def paneInfoSalary(information: StipendioInformations): Unit = {
      val finishDate = dimensionDatePicker(new DatePicker())
      val myCalendar = information.turni.map(value=>Option(value,CreateDatePicker.sqlDateToCalendar(value.data)))
      finishDate.setDayCellFactory(_=>CreateDatePicker.drawDatePicker(myCalendar))
      val datePickerSkin:DatePickerSkin = CreateDatePicker.createDatePickerSkin(finishDate)
      val node:Node = datePickerSkin.getPopupContent
      salaryInfo.getChildren.add(node)
      generalInfo(information)
    }
    private def dimensionDatePicker(finishDate:DatePicker):DatePicker={
      finishDate.setPrefSize(389,195)
      finishDate.setLayoutX(7)
      finishDate.setLayoutY(10)
      finishDate
    }

    private def generalInfo(informations: StipendioInformations): Unit ={
      tittle.setText(resources.getString("general-info"))
      val (extra,normal) = totalExtraAndNormal(informations.turni)
      dayM.setText(resources.getString("extra-day") +extra)
      normalDay.setText(resources.getString("normal-day") +normal)

      infoShiftAndExtra(extra,normal,informations)
      infoAssenza(informations)
    }
    private def infoShiftAndExtra(normal:Int,extra:Int,informations: StipendioInformations):Unit={
      shiftValue.setText(resources.getString("shift-value") +informations.infoValore.valoreTotaleTurni/normal)
      extraValue.setText(resources.getString("extra-value") +informations.infoValore.valoreTotaleTurni/extra)
      shiftValue.setText(resources.getString("shift-total-value") +informations.infoValore.valoreTotaleTurni)
      extraValue.setText(resources.getString("extra-total-value") +informations.infoValore.valoreTotaleStraordinari)
      extraValue.setText(resources.getString("total") +informations.infoValore.valoreTotaleStraordinari+informations.infoValore.valoreTotaleTurni)

    }
    private def infoAssenza(informations: StipendioInformations):Unit={
      Illness.setText(resources.getString("illness-day") +informations.infoAssenza.assenzePerMalattia)
      holiday.setText(resources.getString("holiday") +informations.infoAssenza.assenzePerFerie)

    }
    private def totalExtraAndNormal(turni:List[InfoPresenza]):(Int,Int)= {
      val isStraordinario=true
      val map = turni.groupBy(_.straordinario).map(key=>key._1->key._2.length)
      (map.getOrElse(!isStraordinario,0),map.getOrElse(isStraordinario,0))
    }
  }
}