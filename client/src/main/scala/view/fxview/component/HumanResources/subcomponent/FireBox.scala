package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle
import java.util.stream.Collectors

import caseclass.CaseClassDB.Persona
import javafx.fxml.FXML
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Button, TableColumn, TableView, TextField}
import view.fxview.component.HumanResources.subcomponent.employee.PersonaTableWithSelection
import view.fxview.component.HumanResources.subcomponent.parent.FiresParent
import view.fxview.component.{AbstractComponent, Component}

import scala.jdk.CollectionConverters
import scala.language.postfixOps

/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.FiresParent]]
 */
trait FireBox extends Component[FiresParent]{

}

/**
 * Companion object of [[view.fxview.component.HumanResources.subcomponent.FireBox]]
 *
 */
object FireBox {

  def apply(employeesList: List[Persona]): FireBox = new FireBoxFX(employeesList)

  /**
   * FireBox Fx implementation. It shows Human resource Recruit panel in home view
   *
   * @param employees
   *                  list of employees in db
   */
  private class FireBoxFX(employees: List[Persona])
    extends AbstractComponent[FiresParent]("humanresources/subcomponent/FiresBox") with FireBox {

    @FXML
    var fireButton: Button = _
    @FXML
    var employeeTable: TableView[PersonaTableWithSelection] = _
    @FXML
    var searchBox: TextField = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      initializeButton(resources)

      initializeTable
      fillTable(employees)

      initializeSearch(resources)
    }

    private def initializeButton(resources: ResourceBundle) = {
      fireButton.setText(resources.getString("fire"))
      fireButton.setOnAction(_ => parent.fireClicked(selectedElements))
    }

    private def initializeTable: Unit = {

      val id: TableColumn[PersonaTableWithSelection, String] = new TableColumn[PersonaTableWithSelection, String]("Id")
      id.setMinWidth(100)
      id.setCellValueFactory(new PropertyValueFactory[PersonaTableWithSelection, String]("id"))

      val firstNameCol: TableColumn[PersonaTableWithSelection, String] = new TableColumn[PersonaTableWithSelection, String]("Name")
      firstNameCol.setMinWidth(100)
      firstNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTableWithSelection, String]("name"))

      val secondNameCol: TableColumn[PersonaTableWithSelection, String] = new TableColumn[PersonaTableWithSelection, String]("Surname")
      secondNameCol.setMinWidth(100)
      secondNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTableWithSelection, String]("surname"))

      /*val contractNameCol: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("Contract")
      contractNameCol.setMinWidth(100)
      contractNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("contract"))*/

      val selectCol: TableColumn[PersonaTableWithSelection, String] = new TableColumn[PersonaTableWithSelection, String]("selected")
      selectCol.setMaxWidth(50)
      selectCol.setCellValueFactory(new PropertyValueFactory[PersonaTableWithSelection, String]("selected"))

      employeeTable.getColumns.addAll(id, firstNameCol, secondNameCol, selectCol)
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getString("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        fillTable(employees.filter(person => person.cognome.contains(word) ||
                                              person.nome.contains(word) ||
                                              person.matricola.head.toString.contains(word)))
      })
    }

    def fillTable(employees: List[PersonaTableWithSelection]): Unit = {
      employeeTable.getItems.clear()
      employees.foreach(employee => insertIntoTable(employee))
    }

    private def selectedElements(): Set[Int] = {
      new CollectionConverters.ListHasAsScala[Int](
        employeeTable.getItems.filtered(person => person.isSelected)
          .stream().map[Int](person => person.getId.toInt).collect(Collectors.toList[Int])).asScala.toSet
    }

    private def insertIntoTable(guy: PersonaTableWithSelection): Unit = {
      employeeTable.getItems.add(guy)
    }

  }

}