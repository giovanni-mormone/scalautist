package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.ResultAlgorithm
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import view.fxview.component.manager.subcomponent.parent.SelectResultParent
import view.fxview.component.{AbstractComponent, Component}

trait DriverResultBox extends Component[SelectResultParent]{
 def createShiftResult():Unit
}

object DriverResultBox {

  def apply(result: ResultAlgorithm): DriverResultBox = {
    val driver = new DriverResultBoxFX(result)
    driver.createShiftResult()
    driver
  }

  private class DriverResultBoxFX(result: ResultAlgorithm) extends AbstractComponent[SelectResultParent]("manager/subcomponent/DriverResultBox")
    with DriverResultBox {

    @FXML
    var driverResult: HBox = _
    @FXML
    var driverId: Label = _
    @FXML
    var driverTerminal: Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit =
      super.initialize(location, resources)

    override def createShiftResult():Unit={
      driverId.setText(result.idDriver.toString)
      driverTerminal.setText(result.terminale)
      result.dateIDateF.foreach(res=>{
        val shift = ShiftResultBox(res)
        driverResult.getChildren.add(shift.setParent(parent).pane)
      })
    }
  }
}

