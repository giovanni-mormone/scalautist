package view.fxview.component.driver.subcomponent

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoHome
import com.sun.javafx.scene.control.skin.DatePickerSkin
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.TableView
import javafx.scene.layout.AnchorPane
import view.fxview.component.HumanResources.subcomponent.util.{CreateDatePicker, CreateTable}
import view.fxview.component.driver.subcomponent.parent.HomeBoxParent
import view.fxview.component.driver.utils.{DisponibilitaTable, InfoHomeTable}
import view.fxview.component.{AbstractComponent, Component}

trait HomeBox extends Component[HomeBoxParent]{

}
object HomeBox{
  def apply(infoHome: InfoHome): HomeBox = new HomeBoxFX(infoHome)

  /**
   * HomeBox Fx implementation. It shows shift for one person and general info
   *
   * @param infoHome
   *                  list of shift in db
   */
  private class HomeBoxFX(infoHome: InfoHome)
    extends AbstractComponent[HomeBoxParent]("driver/subcomponent/HomeBox") with HomeBox {

    @FXML
    var datepicker: AnchorPane = _
    @FXML
    var tableTurno:TableView[InfoHomeTable]=_
    @FXML
    var tableDisponibilita:TableView[DisponibilitaTable]=_

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      costructInfoShiftTable()
      costructSecondTable()
      costructDatePicker()
    }
    private def costructInfoShiftTable():Unit = {

      val columnFields=List("Orario","Turno")
      CreateTable.createColumns[InfoHomeTable](tableTurno, columnFields)
      CreateTable.fillTable[InfoHomeTable](tableTurno, infoHome.turno)
      tableTurno.setStyle("Tableview.css")

    }
    private def costructSecondTable():Unit = {

      val disponibilita=Map("Giorno Disponibilita Straordinari"->List("Giorno", "Disponibilita"))
      CreateTable.createNestedColumns[DisponibilitaTable](tableDisponibilita, disponibilita)
      val disponibilitaList =infoHome.disponibilita.map(values=>List[(String,Boolean)]((values.giorno1,true),(values.giorno2,true))).toList.flatten
      CreateTable.fillTable[DisponibilitaTable](tableDisponibilita,disponibilitaList)

    }
    private def costructDatePicker():Unit={

      val datePickerSkin:DatePickerSkin = CreateDatePicker.createDatePickerSkin(LocalDate.now())._1
      val node:Node = datePickerSkin.getPopupContent
      datepicker.getChildren.add(node)

    }
  }
}