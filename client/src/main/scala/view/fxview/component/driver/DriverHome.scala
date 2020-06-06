package view.fxview.component.driver

import java.net.URL
import java.util.ResourceBundle

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.control.{ButtonBuilder, Label, Menu, MenuBar}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.driver.subcomponent.parent.DriverHomeParent

trait DriverHome extends Component[DriverHomeParent]{

}
object DriverHome{
  def apply(): DriverHome = new DriverHomeFX()

  private class DriverHomeFX() extends AbstractComponent[DriverHomeParent] ("driver/DriverHome")
    with DriverHome{

    @FXML
    var driverHome:BorderPane = _
    @FXML
    var home:Menu = _
    @FXML
    var turni:Menu = _
    @FXML
    var stipendi:Menu = _
    @FXML
    var labelHome:Label = _
    @FXML
    var labelTurni:Label = _
    @FXML
    var labelStipendio:Label = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      home.setGraphic(labelHome)
      turni.setGraphic(labelTurni)
      stipendi.setGraphic(labelStipendio)
      labelHome.setOnMouseClicked((t:MouseEvent)=>showMessage("Juanito"))
      labelTurni.setOnMouseClicked((t:MouseEvent)=>showMessage("Juanito"))
      labelStipendio.setOnMouseClicked((t:MouseEvent)=>showMessage("Juanito"))
    }
    private def showMessage(string:String):Unit={
      println(string)
    }
  }
}