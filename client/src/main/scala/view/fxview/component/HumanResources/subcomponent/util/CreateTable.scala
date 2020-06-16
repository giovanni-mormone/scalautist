package view.fxview.component.HumanResources.subcomponent.util

import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.{TableColumn, TableRow, TableView}

object CreateTable {

  def createColumns[A <: TableArgument](table: TableView[A], columns: List[String]): Unit = {

    columns.foreach(name => {
      val column: TableColumn[A, String] = createTableColumn(name.toUpperCase())
      setFactoryAndWidth(column, name)
      table.getColumns.add(column)
    })
  }

  def createNestedColumns[A <: TableArgument](table: TableView[A], columns: Map[String,List[String]]): Unit = {

    columns.foreach(name => {
      val column: TableColumn[A, String] = createTableColumn(name._1.toUpperCase)
      setFactoryAndWidth(column, name._1)
      name._2.foreach(nestedColumn=>{
        val columnNested: TableColumn[A, String] = createTableColumn(nestedColumn.toUpperCase)
        setFactoryAndWidth(columnNested, nestedColumn)
        column.getColumns.add(columnNested)
      })
      table.setMaxSize(430,200)
      table.getColumns.add(column)
    })
  }
  private def createTableColumn[A](name: String)=new TableColumn[A, String](name)

  private def setFactoryAndWidth[A](column: TableColumn[A, String], name: String,dim:Int=177): Unit ={
    column.setId(name.toLowerCase())
    column.setMinWidth(dim)
    column.setCellValueFactory(new PropertyValueFactory[A, String](name))
  }

  def fillTable[A <: TableArgument](table: TableView[A], employees: List[A]): Unit = {
    table.getItems.clear()
    employees.foreach(employee => table.getItems.add(employee))
  }

  def clickListener[A <: TableArgument](table: TableView[A], action: A => Unit): Unit = {
    table.setRowFactory(_ => {
      val row: TableRow[A] = new TableRow[A]()
      row.setOnMouseClicked(event => {
        if(event.getClickCount == 2 && !row.isEmpty)
          action(row.getItem)
      })
      row
    })
  }
}
