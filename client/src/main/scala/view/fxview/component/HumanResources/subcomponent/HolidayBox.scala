package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Persona
import caseclass.CaseClassHttpMessage.Ferie
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.HolidayBoxParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, PersonaTable}
import view.fxview.component.{AbstractComponent, Component}


//metodi controller -> view
trait HolidayBox extends Component[HolidayBoxParent]{

}

object HolidayBox{

  //button che chiama openModal setOnAction
  def apply(persona:List[Ferie]): HolidayBox = new HolidayBoxFX(persona)

  private class HolidayBoxFX(employees: List[Ferie]) extends AbstractComponent[HolidayBoxParent]("humanresources/subcomponent/AbsenceBox") with HolidayBox {

    @FXML
    var employeeTable: TableView[Ferie] = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      val columnFields = List("id", "name", "surname","day in holiday")
      CreateTable.createColumns[Ferie](employeeTable, columnFields)
      CreateTable.fillTable[Ferie](employeeTable, employees)

      initializeSearch(resources)
      CreateTable.clickListener[Ferie](
        employeeTable,
        item => parent.openModal(item.id.get().toInt,item.name.get(),item.surname.get(),isMalattia = false))

    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getString("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[Ferie](
          employeeTable, employees.filter(person => person.nomeCognome.contains(word) ||
            person.idPersona.toString==word ||
            person.giorniVacanza.toString==word))
      })
    }
  }
}
