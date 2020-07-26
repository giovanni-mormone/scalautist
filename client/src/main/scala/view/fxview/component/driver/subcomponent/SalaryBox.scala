package view.fxview.component.driver.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB.Stipendio
import caseclass.CaseClassHttpMessage.{InfoPresenza, StipendioInformations}
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.{Label, ListView}
import javafx.scene.layout.{AnchorPane, HBox, Pane, VBox}
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
    var internalVBox: VBox = _
    @FXML
    var title: Label = _
    @FXML
    var datePickerA: AnchorPane = _

    private var datepicker:Node=_

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      salary.foreach(stipendi => salaryList.getItems.add(stipendi))
      salaryList.setCellFactory(StipendiCellFactory)
      listenerListView()

    }
    private def listenerListView():Unit={

      salaryList.getSelectionModel.selectedItemProperty().addListener(new ChangeListener[Stipendio]()
      {
        def changed(ov:ObservableValue[_<:Stipendio], oldValue:Stipendio, newValue:Stipendio):Unit=
        {
          newValue.idStipendio.foreach(x=>{
            datePickerA.getChildren.remove(datepicker)
            datePickerA.getChildren.add(FXHelperFactory.loadingBox)
            parent.infoSalary(x)
          })
        }
      })

    }
    override def paneInfoSalary(information: StipendioInformations): Unit = {
      datePickerA.getChildren.remove(FXHelperFactory.loadingBox)
      datepicker=createDatePicker(information)
      datePickerA.getChildren.add(datepicker)
      generalInfo(information)
    }
    private def createDatePicker(information: StipendioInformations):Node={

      val myCalendar = information.turni.map(value=>Option(value,CreateDatePicker.sqlDateToCalendar(value.data)))
      val (datePickerSkin,finishDate) = myCalendar match {
        case ::(first, _) => first match {
          case Some(value) => CreateDatePicker.createDatePickerSkin(value._2)
          case None => CreateDatePicker.createDatePickerSkin(LocalDate.now)
        }
        case Nil => CreateDatePicker.createDatePickerSkin(LocalDate.now)
      }
      finishDate.setDayCellFactory(_=>CreateDatePicker.drawDatePicker(myCalendar))
      datePickerSkin.getPopupContent
    }

    private def generalInfo(informations: StipendioInformations): Unit ={
      internalVBox.getChildren.clear()
      title.setText(resources.println("general-info",""))
      val (extra,normal) = totalExtraAndNormal(informations.turni)
      val fExtra = if(extra==0)0 else informations.infoValore.valoreTotaleTurni/extra
      val fNormal =if(normal==0)0 else informations.infoValore.valoreTotaleTurni/normal
      val dayM = LabelAndValueBox("extra-day",extra.toString).setParent(parent).pane
      val normalDay = LabelAndValueBox("normal-day",normal.toString).setParent(parent).pane
      val shiftValue = LabelAndValueBox("shift-value",fNormal.toString).setParent(parent).pane
      val extraValue = LabelAndValueBox("extra-value",fExtra.toString).setParent(parent).pane
      val shiftTotalValue = LabelAndValueBox("shift-total-value",informations.infoValore.valoreTotaleTurni.toString).setParent(parent).pane
      val extraTotalValue = LabelAndValueBox("extra-total-value",informations.infoValore.valoreTotaleStraordinari.toString).setParent(parent).pane
      val totalValue = LabelAndValueBox("total",(informations.infoValore.valoreTotaleStraordinari+informations.infoValore.valoreTotaleTurni).toString).setParent(parent).pane
      val illNess = LabelAndValueBox("illness-day",informations.infoAssenza.assenzePerMalattia.toString).setParent(parent).pane
      val holiday = LabelAndValueBox("holiday",informations.infoAssenza.assenzePerFerie.toString).setParent(parent).pane
      internalVBox.getChildren.addAll(title,dayM,normalDay,shiftValue,extraValue,shiftTotalValue,extraTotalValue,totalValue,illNess,holiday)

    }

    private def totalExtraAndNormal(turni:List[InfoPresenza]):(Int,Int)= {

      val isStraordinario=true
      val map = turni.groupBy(_.straordinario).map(key=>key._1->key._2.length)
      (map.getOrElse(isStraordinario,0),map.getOrElse(!isStraordinario,0))

    }

  }
}