package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
trait RichiestaForDayBox extends Component[ManagerRichiestaBox]{

}
object RichiestaForDayBox{

  def apply(listShift: List[Turno],idDay:Int,nameDay:String): RichiestaForDayBox = {
    {
      val richiestaBox= new RichiestaForDayBoxFX(idDay,nameDay)
      richiestaBox.addChildren(listShift)
      richiestaBox
    }
  }

  private class RichiestaForDayBoxFX(idDay:Int,nameDay:String) extends AbstractComponent[ManagerRichiestaBox]("manager/subcomponent/RichiestaForDayBox")
    with RichiestaForDayBox{

    @FXML
    var titleDay: Label = _
    @FXML
    var back: Button = _
    @FXML
    var cancel: Button= _
    @FXML
    var next: Button = _

    var son: List[LabelTextFieldBox] = _
 
    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      titleDay.setText(nameDay)
      cancel.setText(resources.getResource("cancel-button"))
      back.setText(resources.getResource("back-button"))
      next.setText(resources.getResource("next-button"))
      next.setOnAction(_=>nextAction())
    }
    def addChildren(listShift: List[Turno]): Unit ={
      son=listShift.reverse.map(shift=>{
        val son=LabelTextFieldBox(shift)
        pane.getChildren.add(1,son.setParent(this).pane)
        son
      })
    }
    def nextAction(): Unit = {
      parent.nextAction(idDay->son.map(son=>son.getQuantity))
    }
  }
}