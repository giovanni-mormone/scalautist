package view.fxview.component.HumanResources.subcomponent


import java.net.URL
import java.sql.Date
import java.time.{Instant, LocalDate, ZoneId}
import java.util.ResourceBundle

import caseclass.CaseClassDB.Assenza
import javafx.fxml.FXML
import javafx.scene.control.{Button, DatePicker, TextField}
import view.fxview.component.{AbstractComponent, Component}

trait ModalAbssenceParent {
  def saveAbsence(absence: Assenza)
}
//metodi view -> controller

//metodi controller -> view
trait ModalAbssence extends Component[ModalAbssenceParent]{

}
object ModalAbssence{

  def apply(idDriver:Int): ModalAbssenceFX =new ModalAbssenceFX(idDriver)

  private class ModalAbssenceFX(id:Int) extends AbstractComponent[IllBoxParent]("humanresources/subcomponent/qualcosa :)") with IllBox {

    @FXML
    var idDriver:TextField = _
    @FXML
    var initDate:DatePicker = _
    @FXML
    var finishDate:DatePicker = _
    @FXML
    var illness:Button = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      idDriver.setText(id.toString)

      illness.setOnAction(_=>parent.saveAbsence(Assenza(id,createDataSql(initDate),createDataSql(finishDate),true)))
    }
    private def createDataSql(dateP:DatePicker)={
      val localDate: LocalDate = dateP.getValue;
      val instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
      val date = java.util.Date.from(instant);
      new java.sql.Date(date.getTime);
    }
  }
}