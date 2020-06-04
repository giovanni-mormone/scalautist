package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle
import java.util.stream.Collectors

import caseclass.CaseClassDB.Persona
import javafx.fxml.FXML
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{Button, TableColumn, TableView}
import view.fxview.component.HumanResources.subcomponent.parent.FiresParent
import view.fxview.component.{AbstractComponent, Component}

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
    var employeeTable: TableView[PersonaTable] = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      fireButton.setText(resources.getString("fire"))

      fireButton.setOnAction(_ => elementSelected)

      initializeTable
      employees.foreach(employee => insertIntoTable(employee))
    }

    private def elementSelected(): Unit = {

      println(employeeTable.getItems.filtered(person => person.isSelected)
        .stream().map[Int](person => person.getId.toInt).collect(Collectors.toList[Int]))
    }

    private def insertIntoTable(guy: Persona): Unit = {
      employeeTable.getItems.add(new PersonaTable(guy.matricola.head.toString, guy.nome, guy.cognome))
    }

    private def initializeTable: Unit = {

      val id: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("Id")
      id.setMinWidth(100)
      id.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("id"))

      val firstNameCol: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("Name")
      firstNameCol.setMinWidth(100)
      firstNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("name"))

      val secondNameCol: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("Surname")
      secondNameCol.setMinWidth(100)
      secondNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("surname"))

      /*val contractNameCol: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("Contract")
      contractNameCol.setMinWidth(100)
      contractNameCol.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("contract"))*/

      val selectCol: TableColumn[PersonaTable, String] = new TableColumn[PersonaTable, String]("selected")
      selectCol.setMaxWidth(50)
      selectCol.setCellValueFactory(new PropertyValueFactory[PersonaTable, String]("selected"))

      employeeTable.getColumns.addAll(id, firstNameCol, secondNameCol, selectCol)
    }
  }

}
