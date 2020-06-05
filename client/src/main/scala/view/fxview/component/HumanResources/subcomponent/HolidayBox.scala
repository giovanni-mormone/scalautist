package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Persona
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
  def apply(persona:List[Persona]): HolidayBox = new HolidayBoxFX(persona)

  private class HolidayBoxFX(employees: List[Persona]) extends AbstractComponent[HolidayBoxParent]("humanresources/subcomponent/AbsenceBox") with HolidayBox {

    @FXML
    var button:Button = _
    @FXML
    var employeeTable: TableView[PersonaTable] = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      val columnFields = List("id", "name", "surname")
      CreateTable.createColumns[PersonaTable](employeeTable, columnFields)
      CreateTable.fillTable[PersonaTable](employeeTable, employees)

      initializeSearch(resources)
      button.setText(resources.getString("holiday"))
      button.setOnAction(_=>parent.openModal(2,"nome","cognome"))
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getString("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[PersonaTable](
          employeeTable, employees.filter(person => person.cognome.contains(word) ||
            person.nome.contains(word) ||
            person.matricola.head.toString.contains(word)))
      })
    }
  }
}
