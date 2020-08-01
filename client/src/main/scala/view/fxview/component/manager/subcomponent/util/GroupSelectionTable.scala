package view.fxview.component.manager.subcomponent.util

import javafx.beans.property.SimpleStringProperty
import view.fxview.component.HumanResources.subcomponent.util.{SelectionTableField, TableArgument}

class GroupSelectionTable(ruleg: String, dateg: String) extends TableArgument{

  var rule = new SimpleStringProperty(ruleg)
  var date = new SimpleStringProperty(dateg)

  def getRule: String = rule.get
  def getDate: String = date.get

  def setRule(v: String): Unit = rule.set(v)
  def setDate(v: String): Unit = date.set(v)

}

object GroupSelectionTable {

  implicit def groupToGroupTable(group: GroupToTable): GroupSelectionTable = {
    val date: String = group.date.map(_.toLocalDate)
      .map(date => date.getDayOfMonth.toString + "/" + date.getMonth.toString + "/" + date.getYear.toString)
      .reduce((dl, d) => dl + "; " + d)
    new GroupSelectionTable( group.regola, date)
  }

  implicit def listParamsToListParamsTable(group: List[GroupToTable]): List[GroupSelectionTable] =
    group.map(gruppo => groupToGroupTable(gruppo))
}