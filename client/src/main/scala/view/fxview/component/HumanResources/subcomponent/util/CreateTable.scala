package view.fxview.component.HumanResources.subcomponent.util

import java.util.stream.Collectors

import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{TableColumn, TableView}

import scala.jdk.CollectionConverters

object CreateTable {

  def createColumns[A <: TableArgument](table: TableView[A], columns: List[String]): Unit = {

    columns.foreach(name => {
      val column: TableColumn[A, String] = new TableColumn[A, String](name.toUpperCase)
      column.setMinWidth(100)
      column.setCellValueFactory(new PropertyValueFactory[A, String](name))
      table.getColumns.add(column)
    })
  }

  def fillTable[A <: TableArgument](table: TableView[A], employees: List[A]): Unit = {
    table.getItems.clear()
    employees.foreach(employee => table.getItems.add(employee))
  }
}
