package view.fxview.component.HumanResources.subcomponent


import java.net.URL
import java.sql.{Date => dateSql}
import java.time.{Instant, LocalDate, ZoneId}
import java.util.{ResourceBundle, Date => dateUtil}

import caseclass.CaseClassDB.Assenza
import caseclass.CaseClassHttpMessage.Ferie
import javafx.fxml.FXML
import javafx.scene.control.{Button, DateCell, DatePicker, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.ModalAbsenceParent
import view.fxview.component.{AbstractComponent, Component}

trait ModalAbsence extends Component[ModalAbsenceParent]{

}

object ModalAbsence{

  def apply(item:Ferie, isMalattia:Boolean=true): ModalAbsence =new ModalAbsenceFX(item,isMalattia)

  private class ModalAbsenceFX(item:Ferie, isMalattia:Boolean)
    extends AbstractComponent[ModalAbsenceParent]("humanresources/subcomponent/ModalAbsence")
      with ModalAbsence {

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
      nameSurname.setText(s"${item.nomeCognome}")
      setInitDate(LocalDate.now())
      initDate.setOnAction(_=>enableFinishDate())
      finishDate.setOnAction(_=>enableButton())
      button.setOnAction(_=>saveAbscence())

    }
    private def saveAbscence(): Unit ={
      parent.saveAbsence(Assenza(item.idPersona,createDataSql(initDate),createDataSql(finishDate),isMalattia))
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
        if(isMalattia)
          setDisable(empty || date.compareTo(today) < 0)
        else
          setDisable(empty || date.compareTo(today) < 0 || date.compareTo(today.plusDays(30-item.giorniVacanza)) > 0)
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
