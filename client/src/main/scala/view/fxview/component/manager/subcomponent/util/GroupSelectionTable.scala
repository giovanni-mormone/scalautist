package view.fxview.component.manager.subcomponent.util

import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.{SelectionTableField, TableArgument}

class GroupSelectionTable(idg: String, ruleg: String, dateg: String) extends SelectionTableField with TableArgument{

  var id = new SimpleStringProperty(idg)
  var rule = new SimpleStringProperty(ruleg)
  var date = new SimpleStringProperty(dateg)

  def getId: String = id.get
  def getRule: String = rule.get
  def getDate: String = date.get

  def setId(v: String): Unit = id.set(v)
  def setRule(v: String): Unit = rule.set(v)
  def setDate(v: String): Unit = date.set(v)

}

object GroupSelectionTable {

  implicit def groupToGroupTable(group: GroupToTable): GroupSelectionTable = {
    val date: String = group.date.map(_.toLocalDate)
      .map(date => date.getDayOfMonth + "/" + date.getMonth + "/" + date.getYear)
      .reduce((dl, d) => dl + "; " + d)
    new GroupSelectionTable(group.id, group.regola, date)
  }

  implicit def listParamsToListParamsTable(group: List[GroupToTable]): List[GroupSelectionTable] =
    group.map(gruppo => groupToGroupTable(gruppo))
}