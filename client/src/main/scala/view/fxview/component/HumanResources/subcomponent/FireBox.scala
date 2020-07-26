package view.fxview.component.HumanResources.subcomponent

import java.net.URL
import java.util.ResourceBundle
import java.util.stream.Collectors
import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassDB.Persona
import javafx.fxml.FXML
import javafx.scene.control.{Button, TableView, TextField}
import view.fxview.component.HumanResources.subcomponent.parent.FiresParent
import view.fxview.component.HumanResources.subcomponent.util.{CreateTable, PersonaTableWithSelection}
import view.fxview.component.{AbstractComponent, Component}

import scala.jdk.CollectionConverters
import scala.language.postfixOps

/**
 * @author Francesco Cassano
 *
 * Interface used for communicate with the view. It extends [[view.fxview.component.Component]]
 * of [[view.fxview.component.HumanResources.subcomponent.parent.FiresParent]]
 */
trait FireBox extends Component[FiresParent]
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

      val columnFields = List("id", "name", "surname", "selected")
      CreateTable.createColumns[PersonaTableWithSelection](employeeTable, columnFields)
      CreateTable.fillTable[PersonaTableWithSelection](employeeTable, employees)

      initializeSearch(resources)
    }

    private def initializeButton(resources: ResourceBundle): Unit = {
      fireButton.setText(resources.getResource("fire"))
      fireButton.setOnAction(_ => parent.fireClicked(getSelectedElements))
    }

    private def initializeSearch(resourceBundle: ResourceBundle): Unit = {
      searchBox.setPromptText(resourceBundle.getResource("search"))

      searchBox.textProperty().addListener((_, _, word) => {
        CreateTable.fillTable[PersonaTableWithSelection](
          employeeTable,  employees.filter(person => person.cognome.toLowerCase.contains(word.toLowerCase) ||
                                              person.nome.toLowerCase.contains(word.toLowerCase) ||
                                              person.matricola.exists(id=>id.toString.contains(word))))
      })
    }

    private def getSelectedElements: Set[Int] = {
      new CollectionConverters.ListHasAsScala[Int](
        employeeTable.getItems.filtered(person => person.isSelected)
          .stream().map[Int](person => person.getId.toInt).collect(Collectors.toList[Int])).asScala.toSet
    }

  }

}
