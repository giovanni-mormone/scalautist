package view.fxview.component.driver.utils

import caseclass.CaseClassDB.Stipendio
import javafx.scene.control.{ListCell, Tooltip}

class StipendiCell extends ListCell[Stipendio]{
  override def updateItem(item: Stipendio, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    val index = this.getIndex
    var name = ""
    var toolTip=0.0
    if(item==null || empty){}else{
      name=item.data.toString
      toolTip = item.valore
    }
    this.setText(name)
    this.setTooltip(new Tooltip(toolTip.toString))
  }
}
