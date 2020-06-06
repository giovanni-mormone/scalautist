package view.fxview.component.HumanResources.subcomponent


import java.net.URL
import java.sql.{Date => dateSql}
import java.util.{Date => dateUtil}
import java.time.{Instant, LocalDate, ZoneId}
import java.util.ResourceBundle

import caseclass.CaseClassDB.Assenza
import javafx.fxml.FXML
import javafx.scene.control.{Button, DateCell, DatePicker, Label, TextField}
import javafx.util.Callback
import view.fxview.component.HumanResources.subcomponent.parent.ModalAbsenceParent
import view.fxview.component.{AbstractComponent, Component}

//metodi view -> controller

//metodi controller -> view
trait ModalAbsence extends Component[ModalAbsenceParent]{

}
object ModalAbsence{

  def apply(idDriver:Int,name:String,surname:String, isMalattia:Boolean=true): ModalAbsence =new ModalAbsenceFX(idDriver,name,surname,isMalattia)

  private class ModalAbsenceFX(id:Int,name:String,surname:String, isMalattia:Boolean) extends AbstractComponent[ModalAbsenceParent]("humanresources/subcomponent/ModalAbsence") with ModalAbsence {

    @FXML
    var nameSurname:TextField = _
    @FXML
    var initDate:DatePicker = _
    @FXML
    var finishDate:DatePicker = _
    @FXML
    var button:Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      if(isMalattia)button.setText(resources.getString("absence-button"))else button.setText(resources.getString("holiday-button"))
      nameSurname.setText(s"$name "+s" $surname")
      setInitDate(LocalDate.now())
      initDate.setOnAction(_=>enableFinishDate())
      finishDate.setOnAction(_=>enableButton())
      button.setOnAction(_=>saveAbscence())
    }
    private def saveAbscence(): Unit ={
      parent.saveAbsence(Assenza(id,createDataSql(initDate),createDataSql(finishDate),isMalattia))
    }
    private def enableFinishDate(): Unit ={
      val today = initDate.getValue
      finishDate.setValue(today)
      setFinishDate(today)
      finishDate.setDisable(false)
    }
    def setDate(today:LocalDate): DateCell = new DateCell() {
      override def updateItem(date:LocalDate, empty:Boolean) {
        super.updateItem(date, empty)
        setDisable(empty || date.compareTo(today) < 0 )
      }
    }
    def setFinalDate(today:LocalDate):DateCell = new DateCell(){
      override def updateItem(date:LocalDate, empty:Boolean) {
        super.updateItem(date, empty)
        setDisable(empty || date.compareTo(today) < 0 || date.minusDays(30).compareTo(today) >= 0)
      }
    }
    private def setInitDate(today:LocalDate): Unit ={
      initDate.setDayCellFactory(_=>setDate(today.minusDays(3)))
    }

    private def setFinishDate(today:LocalDate): Unit ={
      finishDate.setDayCellFactory(_=>setFinalDate(today))
    }

    private def enableButton(): Unit ={
      button.setDisable(false)
    }
    private def createDataSql(dateP:DatePicker)={
      val localDate: LocalDate = dateP.getValue
      val instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()))
      val date = dateUtil.from(instant)
      new dateSql(date.getTime)

    }
  }
}
