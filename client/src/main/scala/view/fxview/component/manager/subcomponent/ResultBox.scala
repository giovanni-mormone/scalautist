package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import view.fxview.util.ResourceBundleUtil._
import caseclass.CaseClassHttpMessage.ResultAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.{Label, ScrollBar, ScrollPane}
import javafx.scene.layout.{HBox, VBox}
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.SelectResultParent

trait ResultBox extends Component[SelectResultParent]{
  def createDriverResult():Unit

}
object ResultBox {

  def apply(result: List[ResultAlgorithm],listDate:List[Date]): ResultBox ={
    val results:ResultBox = new ResultBoxFX(result,listDate)
    results.createDriverResult()
    results
  }

  private class ResultBoxFX(result: List[ResultAlgorithm],listDate:List[Date]) extends AbstractComponent[SelectResultParent]("manager/subcomponent/ResultBox")
    with ResultBox {

    @FXML
    var father: VBox = _
    @FXML
    var header: HBox= _
    @FXML
    var id: Label = _
    @FXML
    var terminal: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      id.setText(resources.getResource("id-driver"))
      terminal.setText(resources.getResource("terminal"))
      listDate.foreach(value => header.getChildren.add(new Label(value.toString)))

    }

    override def createDriverResult(): Unit = {
      result.foreach(res=>{
        val driver = DriverResultBox(res)
        father.getChildren.add(driver.setParent(parent).pane)
      })
    }
  }

}