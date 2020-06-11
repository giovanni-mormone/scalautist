package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.Ferie
import javafx.fxml.FXML
import javafx.scene.control.{TableView, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.HolidayBoxParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, FerieTable}
import view.fxview.component.{AbstractComponent, Component}

/**
 * @author Fabian Aspee Encina
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.HolidayBoxParent]]
 */
trait HolidayBox extends Component[HolidayBoxParent]{

}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.HolidayBox]]
 */
object HolidayBox {

  def apply(persona:List[Ferie]): HolidayBox = new HolidayBoxFX(persona)

  /**
   * javaFX private implementation of [[view.fxview.component.HumanResources.subcomponent.HolidayBox]]
   *
   * @param employees
   *                  List of [[caseclass.CaseClassDB.Persona]] to whom to assign holiday period
   */
  private class HolidayBoxFX(employees: List[Ferie])
    extends AbstractComponent[HolidayBoxParent]("humanresources/subcomponent/AbsenceBox") with HolidayBox {

    @FXML
    var employeeTable: TableView[FerieTable] = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      val columnFields = List("id","nameSurname","holiday")

      CreateTable.createColumns[FerieTable](employeeTable, columnFields)
      CreateTable.fillTable[FerieTable](employeeTable, employees)

      initializeSearch(resources)
      CreateTable.clickListener[FerieTable](
        employeeTable,
        item => parent.openModal(Ferie(item.getId,item.getNameSurname,item.getHoliday),isMalattia = false))

    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getString("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[FerieTable](
          employeeTable, employees.filter(person => person.nomeCognome.contains(word) ||
            person.idPersona.toString==word ||
            person.giorniVacanza.toString==word))
      })
    }
  }
}
