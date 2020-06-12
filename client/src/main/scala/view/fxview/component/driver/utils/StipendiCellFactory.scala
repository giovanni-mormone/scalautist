package view.fxview.component.driver.utils
import caseclass.CaseClassDB.Stipendio
import javafx.scene.control.{ListCell, ListView}
import javafx.util.Callback
class StipendiCellFactory extends Callback[ListView[Stipendio],ListCell[Stipendio]]{
  override def call(param: ListView[Stipendio]): ListCell[Stipendio] = new StipendiCell
}
