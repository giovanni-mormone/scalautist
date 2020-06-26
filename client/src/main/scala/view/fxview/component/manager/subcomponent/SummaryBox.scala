package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{HBox, Pane, VBox}
import view.fxview.component.manager.subcomponent.ManagerRichiestaBox.{ExtraInfo, InfoRichiesta}
import view.fxview.component.{AbstractComponent, Component}

trait SummaryBox extends Component[RichiestaForDayBox]{
  def designPage():Unit
}
object SummaryBox{

  def apply(infoRequest:InfoRichiesta,extraInfo: ExtraInfo): SummaryBox = {

    val son = new SummaryBoxFX(infoRequest,extraInfo)
    son.designPage()
    son
  }

  private class SummaryBoxFX(infoRequest:InfoRichiesta,extraInfo: ExtraInfo) extends AbstractComponent[RichiestaForDayBox]("manager/subcomponent/SummaryBox")
    with SummaryBox {

    @FXML
    var dataI: Label = _
    @FXML
    var dataF: Label = _
    @FXML
    var terminalLabel: Label = _
    @FXML
    var dayLabel: Label = _
    @FXML
    var infoBox: HBox = _
    @FXML
    var indietro: Button = _
    @FXML
    var shiftInfoLabel: VBox = _
    @FXML
    var avanti: Button = _

    var position:Int=1

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location,resources)
      dataI.setText("infoRequest.date.toString")
      dataF.setText("infoRequest.date1.toString")
      indietro.setText("Indietro")
      avanti.setText("Avanti")
      avanti.setOnAction(_=>nextPage())
      indietro.setOnAction(_=>backPage())
      terminalLabel.setText("")
    }

    def nextPage(): Unit = { 
      if(infoRequest.info.exists(value=>value._1>=position+1)){
        position=position+1
        shiftInfoLabel.getChildren.clear()
        designPage()
      }
    }

    def backPage(): Unit = {
      if(infoRequest.info.exists(value=>value._1<=position-1)){
        position=position-1
        shiftInfoLabel.getChildren.clear()
        designPage()
      }
    }

    override def designPage():Unit={
      val info = createInfo()
      dayLabel.setText(info._1)
      info._2.reverse.foreach(value=>shiftInfoLabel.getChildren.add(ShiftAndQuantity(value._1,value._2).pane))
    }

    private def createInfo():(String,List[(String,String)])=
      infoRequest.info.filter(day => day._1 == position).flatMap(dayInt => extraInfo.days.filter(day => day._1 == dayInt._1)
        .map(day => day._2 -> dayInt._2)).map(value => value._1 -> value._2.flatMap(shift => extraInfo.listShift.filter(id => id.id.contains(shift._1))
        .map(nameTurno => nameTurno.nomeTurno -> shift._2.toString))) match {
        case List(element) => element
        case Nil =>("",List.empty)
      }
  }
}