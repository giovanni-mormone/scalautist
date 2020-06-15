package view.fxview.component.driver.utils

import caseclass.CaseClassDB.Stipendio
import javafx.scene.control.ListCell

class StipendiCell extends ListCell[Stipendio]{
  override def updateItem(item: Stipendio, empty: Boolean): Unit = {
    super.updateItem(item, empty)
    val index = this.getIndex
    var name = ""
    if(item==null || empty){}else name=item.data.toString
    this.setText(name)
  }
}
