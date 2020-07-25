package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle
import java.util.stream.Collectors

import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TableView.ResizeFeatures
import javafx.util.Callback
import view.fxview.component.HumanResources.subcomponent.util.CreateTable
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ChangeSettimanaRichiestaParent
import view.fxview.component.manager.subcomponent.util.ShiftTable

import scala.jdk.CollectionConverters

trait TableParamSettimana extends Component[ChangeSettimanaRichiestaParent] {

  def getElements(): Set[ShiftTable]
}

object TableParamSettimana {

  def apply(elements: List[ShiftTable]): TableParamSettimana = new TableParamSettimanaFX(elements)

  private class TableParamSettimanaFX(elements: List[ShiftTable])
    extends AbstractComponent[ChangeSettimanaRichiestaParent]("manager/subcomponent/TableParamSettimana")
    with TableParamSettimana {

    @FXML
    var table: TableView[ShiftTable] = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      CreateTable.fillTable[ShiftTable](table, elements)
      CreateTable.createColumns[ShiftTable](table, List("shift"))
      CreateTable.createEditableColumns[ShiftTable](table, ShiftTable.editableShiftTable)
      CreateTable.createColumns[ShiftTable](table, List("combo"),150)
    }

    override def getElements(): Set[ShiftTable] = {
      new CollectionConverters.ListHasAsScala[ShiftTable](
        table.getItems.stream().map[ShiftTable](x => x)
          .collect(Collectors.toList[ShiftTable])).asScala.toSet
    }
  }
}
