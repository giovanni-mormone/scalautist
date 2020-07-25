package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Persona
import caseclass.CaseClassHttpMessage.Ferie
import javafx.fxml.FXML
import javafx.scene.control.{TableView, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.IllBoxParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, PersonaTable}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
/**
 * @author Fabian Aspee Encina
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.IllBoxParent]]
 */
trait IllBox extends Component[IllBoxParent]{

}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.IllBox]]
 */
object IllBox{

  def apply(persona:List[Persona]): IllBox = new IllBoxFX(persona)

  /**
   * javaFX private implementation of [[view.fxview.component.HumanResources.subcomponent.IllBox]]
   *
   * @param employees
   *                  List of [[caseclass.CaseClassDB.Persona]] to whom to assign period of illness
   */
  private class IllBoxFX(employees: List[Persona]) extends AbstractComponent[IllBoxParent]("humanresources/subcomponent/AbsenceBox") with IllBox {

    @FXML
    var employeeTable: TableView[PersonaTable] = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      val columnFields = List("id", "name", "surname")
      CreateTable.createColumns[PersonaTable](employeeTable, columnFields)
      CreateTable.fillTable[PersonaTable](employeeTable, employees)

      initializeSearch(resources)
      CreateTable.clickListener[PersonaTable](
        employeeTable,
        item => parent.openModal(Ferie(item.getId.toInt,item.getName+" "+item.getSurname)))
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getResource("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[PersonaTable](
          employeeTable, employees.filter(person => person.cognome.contains(word) ||
            person.nome.contains(word) ||
            person.matricola.exists(id=>id.toString.contains(word))))
      })
    }
  }
}
