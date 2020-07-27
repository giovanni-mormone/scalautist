package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import utils.TransferObject.InfoRichiesta
import view.fxview.component.manager.subcomponent.ManagerRichiestaBox.ExtraInfo
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

  def apply(listShift: List[Turno],idDay:Int,nameDay:String,valueForDay: (Int, List[(Int, Int)])): RichiestaForDayBox = {
    {
      val richiestaBox= new RichiestaForDayBoxFX(idDay,nameDay)
      richiestaBox.addChildren(listShift,valueForDay)
      richiestaBox
    }
  }

  def apply(infoRequest:InfoRichiesta,extraInfo: ExtraInfo): RichiestaForDayBox = {
    {
      val richiestaBox= new RichiestaForDayBoxFX(extraInfo.idSummary,extraInfo.nameDay)
      richiestaBox.addFinalChildren(infoRequest,extraInfo)
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
    val POSITION_SON=1
    val ALL_COMPLETE=6
    val EMPTY = false

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      titleDay.setText(nameDay)
      cancel.setText(resources.getResource("cancel-button"))
      back.setText(resources.getResource("back-button"))
      next.setText(resources.getResource("next-button"))
      next.setOnAction(_=>nextAction())
      back.setOnAction(_=>backAction())
      cancel.setOnAction(_=>cancelAction())
    }

    def addChildren(listShift: List[Turno]): Unit ={
      son=listShift.reverse.map(shift=>{
        addChildrenLabelTextField(shift)
      })
    }

    private def addChildrenLabelTextField(shift:Turno): LabelTextFieldBox ={
      val son=LabelTextFieldBox(shift)
      pane.getChildren.add(POSITION_SON,son.setParent(this).pane)
      shift.id.foreach(value=> pane.getChildren.get(POSITION_SON).setId(value.toString))
      son
    }

    def addChildren(listShift: List[Turno],valueForDay: (Int, List[(Int, Int)])): Unit ={
      son=listShift.reverse.map(shift=>{
        val son = addChildrenLabelTextField(shift)
        valueForDay._2.filter(value=>shift.id.contains(value._1))
          .foreach(value=>son.setInfo( value._2))
        son
      })
    }

    def addFinalChildren(infoRequest:InfoRichiesta,extraInfo: ExtraInfo): Unit ={
      next.setText(resources.getResource("finish-button"))
      val son = SummaryBox(infoRequest,extraInfo)
      pane.getChildren.add(POSITION_SON,son.setParent(this).pane)
      next.setOnAction(_=>finishAction(infoRequest))
      back.setOnAction(_=>backFromSummaryAction())

    }

    def finishAction(infoRequest:InfoRichiesta):Unit=
      parent.nextAction(infoRequest)

    def nextAction(): Unit =
      checkTextField()

    def backFromSummaryAction(): Unit =
      parent.backAction(idDay->List.empty)


    def backAction(): Unit =
      parent.backAction(idDay->son.map(son=>son.getQuantity))

    def cancelAction(): Unit =
      parent.cancelAction()

    private def checkTextField(): Unit ={
      son.map(son => son->son.verifyData) match {
        case list if list.count(_._2 == EMPTY)==ALL_COMPLETE => parent.nextAction(idDay->son.map(son=>son.getQuantity))
        case list => list.collect{ case (box, true) =>box.setTextError(); case (box, _) =>box.clearText();}
      }
    }

  }
}